package com.petcation.client.payments.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petcation.client.payments.component.PaymentValidator;
import com.petcation.client.payments.component.ValidationResult;
import com.petcation.client.payments.dao.PaymentsDAO;
import com.petcation.client.payments.vo.PaymentsVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Service
@RequiredArgsConstructor
@Log4j
public class PaymentsService {

    private final PaymentValidator paymentValidator;
	private final PaymentsDAO paymentsDao;
	
	public void createPayment(String paymentId, int price, int userNo) {
		paymentsDao.createPayment(paymentId, price, userNo);
	}
	
    // DB 업데이트 로직
    public void updatePaymentComplete(String orderId, String paymentKey, String method) {
        int updated = paymentsDao.completePayment(paymentKey, method, orderId);
        if (updated == 0) {
            log.error("[경고] 결제 완료 처리 실패 - 예상 상태(PROCESSING) 아님. orderId: " + orderId);
            throw new IllegalStateException("결제 완료 처리 중 상태 불일치 발생: " + orderId);
        }
        log.info("결제 완료: 주문번호 "+ orderId);
    }
    
    public void failPayment(String orderId) {
        paymentsDao.failPayment(orderId);
    }

    public void cancelPayment(String orderId) {
        int updated = paymentsDao.cancelPayment(orderId);
        if (updated == 0) {
            log.error("[경고] 결제 상태를 CANCELED로 바꾸지 못했습니다. - 예상 상태(CANCEL_PROCESSING) 아님. orderId: " + orderId);
            throw new IllegalStateException("결제 취소 처리 중 상태 불일치 발생: " + orderId);
        }
        log.info("결제 취소 완료: 주문번호 "+ orderId);
    }
    
    public PaymentsVO getPayment(String orderId) {
        return paymentsDao.getPayment(orderId);
    }

    public String getPaymentKey(String orderId) {
        return paymentsDao.getPaymentKey(orderId);
    }

    @Transactional
    public ValidationResult claimForProcessing(String orderId, Long amount) {
        
        PaymentsVO payment = getPayment(orderId);
        log.info("[DEBUG] claimForProcessing 진입 - orderId: " + orderId + ", 현재 status: " + payment.getStatus() + ", thread: " + Thread.currentThread().getName());
        ValidationResult result = paymentValidator.validate(payment, orderId, amount);
        if (result == ValidationResult.VALID) {
            updatePaymentProcessing(orderId);
        }
        return result;
    }

    private void updatePaymentProcessing(String orderId) {
        int updated = paymentsDao.processingPayment(orderId);
        log.info("[DEBUG] processingPayment UPDATE 결과 - 영향받은 row 수: " + updated);
        if (updated == 0) {
            log.error("[경고] 결제 상태를 PROCESSING으로 바꾸지 못했습니다. - 예상 상태(READY) 아님. orderId: " + orderId);
        }
    }

    @Transactional
    public boolean claimForCancel(String orderId) {
        PaymentsVO payment = getPayment(orderId);
        String status = payment.getStatus();
        
        if (!"DONE".equals(status)) {
            // DONE이 아니면 취소 불가 - READY/PROCESSING/CANCELED/CANCEL_PROCESSING/FAIL 등
            log.info("취소 불가 상태 [status: " + status + "] - orderId: " + orderId);
            return false;
        }
        int updated = updatePaymentCancleProcessing(orderId);
        if (updated == 0) {
            log.error("[경고] 결제 상태를 CANCEL_PROCESSING으로 바꾸지 못했습니다. - 예상 상태(DONE) 아님. orderId: " + orderId);
            return false;
        }
        return true;
    }
    
    private int updatePaymentCancleProcessing(String orderId) {
        return paymentsDao.CancelProcessingPayment(orderId);
    }

    public void revertCancelProcessing(String orderId) {
        paymentsDao.revertCancelProcessing(orderId);
    }
}
