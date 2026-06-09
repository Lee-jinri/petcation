package com.petcation.client.payments.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petcation.client.hotel.service.User_HotelService;
import com.petcation.client.payments.component.PaymentValidator;
import com.petcation.client.payments.component.ValidationResult;
import com.petcation.client.payments.infra.TossProvider;
import com.petcation.client.payments.vo.PaymentsVO;
import com.petcation.client.payments.vo.TossResponseDto;
import com.petcation.client.payments.vo.WebhookDto;
import com.petcation.client.reservation.component.ReservationManager;
import com.petcation.client.reservation.service.ReservService;
import com.petcation.client.reservation.vo.ReservVO;
import com.petcation.common.exception.BadRequestException;
import com.petcation.common.vo.ReservationRequestDTO;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Service
@AllArgsConstructor
@Log4j
public class PaymentFacade {
    private final ReservService reservService;
    private final User_HotelService hotelService;
    private final PaymentsService paymentsService;
    private final ReservationManager reservationManager;
    private final PaymentValidator paymentValidator;
    private final TossProvider tossProvider;
    
    @Transactional
    public int createPayment(ReservationRequestDTO req) {
        LocalDateTime start = LocalDate.parse(req.getCheckin()).atStartOfDay();
        LocalDateTime end = LocalDate.parse(req.getCheckout()).atStartOfDay();
        
        reservationManager.validateOrder(req, start, end);
        boolean isBooked = reservService.isBooked(req.getHotelNo(), start, end);
        if(isBooked) {
            throw new BadRequestException("이미 예약된 날짜 입니다.");
        }
        int price = hotelService.getPriceByHotelNo(req.getHotelNo());
        int totalPrice = reservationManager.calculatePrice(price, start, end);
        req.setPrice(totalPrice);
        
        paymentsService.createPayment(req.getOrderId(), totalPrice, req.getUserNo());
        reservService.reservInsert(req);
        
        return totalPrice;
    }

    public boolean confirmPayment(String paymentKey, String orderId, Long amount) {
        PaymentsVO payment = paymentsService.getPayment(orderId);
        ValidationResult result = paymentValidator.validate(payment, orderId, amount);
        
        switch (result) {
            case VALID -> {
                TossResponseDto response = tossProvider.confirm(paymentKey, orderId, amount);
                if (response.isSuccess()) {
                    // 성공 시 DB 업데이트
                    processPaymentComplete(orderId, response.getPaymentKey(), response.getMethod());
                    return true;
                } else {
                    // 실패 시 로그 남기고 상태값 변경
                    log.error("결제 실패: " + response.getErrorMessage());
                    processPaymentFail(orderId);
                    return false;
                }
            }
            case ALREADY_DONE -> { log.info("중복 webhook 무시: " + orderId); return true; }
            case AMOUNT_MISMATCH, NOT_FOUND -> {
                log.error("결제 검증 실패 [" + result.name() + "] - orderId: " + orderId);
                processPaymentFail(orderId);
                return false;
            }
        }
        return false;
    }

    public ReservVO getReservationByOrderId(String orderId) {
        return reservService.reservResult(orderId);
    }

    public void cancelPaymentAndReservation(String orderId, int userNo) {
        ReservVO r = reservService.getReservForCancel(orderId);
        reservationManager.validateCancellation(r, orderId, userNo); // 1. 취소 가능 여부 검증
        
        String paymentKey = paymentsService.getPaymentKey(orderId); 
        TossResponseDto result = tossProvider.cancel(paymentKey, orderId); // 2. 결제 취소 (토스 API)
        
        if (result.isSuccess()) {
            try {
                processPaymentCancel(orderId);
            } catch (Exception e) {
                // 토스는 취소됐는데 DB 실패 → 수동 처리 필요
                log.error("[긴급] 토스 취소 완료 / DB 업데이트 실패. 수동 확인 필요. orderId: " + orderId);
                throw e;
            }
        } else {
            throw new BadRequestException(result.getErrorMessage());
        }
    }

    private boolean cancelAndUpdateStatus(String paymentKey, String orderId) {
        TossResponseDto res = tossProvider.cancel(paymentKey, orderId);
        if (res.isSuccess()) processPaymentCancel(orderId);
        else processPaymentFail(orderId);
        return false;
    }
    
    public void processWebhook(WebhookDto.PaymentData data) {
        PaymentsVO payment = paymentsService.getPayment(data.getOrderId());
        ValidationResult result = paymentValidator.validate(payment, data.getOrderId(), data.getTotalAmount());
        
        switch (data.getStatus()) {
            case "DONE" -> {
                switch (result) {
                    case VALID -> {
                        processPaymentComplete(data.getOrderId(), data.getPaymentKey(), data.getMethod());
                    }
                    case ALREADY_DONE -> log.debug("중복 webhook 무시: " + data.getOrderId());
                    case AMOUNT_MISMATCH, NOT_FOUND -> {
                        processPaymentFail(data.getOrderId());
                    }
                }
            }
            case "CANCELED" -> {
                if (!"CANCELED".equals(payment.getStatus())) {
                    processPaymentCancel(data.getOrderId());
                } else {
                    log.debug("이미 취소 처리된 주문 webhook 무시: " + data.getOrderId());
                }
            }
        }
    }
    
    @Transactional
    public void processPaymentComplete(String orderId, String paymentKey, String method) {
        reservService.updateReservComplete(orderId);
        paymentsService.updatePaymentComplete(orderId, paymentKey, method);
    }

    @Transactional
    public void processPaymentFail(String orderId) {
        paymentsService.failPayment(orderId);
        reservService.failReservation(orderId);
    }
    
    @Transactional
    public void processPaymentCancel(String orderId) {
        paymentsService.cancelPayment(orderId);
        reservService.cancelReservation(orderId);
    }
}
