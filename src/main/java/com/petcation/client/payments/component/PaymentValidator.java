package com.petcation.client.payments.component;

import org.springframework.stereotype.Component;

import com.petcation.client.payments.vo.PaymentsVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Component
@RequiredArgsConstructor
@Log4j
public class PaymentValidator {
    
    public ValidationResult validate(PaymentsVO payment, String orderId, Long amount) {
        if (payment == null) {
            log.warn("결제 검증 실패: 주문번호 없음" + orderId);
            return ValidationResult.NOT_FOUND;
        }
        if ("DONE".equals(payment.getStatus())) {
            log.info("이미 결제 완료된 주문입니다: " + orderId);
            return ValidationResult.ALREADY_DONE;
        }
        if ("PROCESSING".equals(payment.getStatus())) {
            log.info("처리 중인 주문입니다: " + orderId);
            return ValidationResult.IN_PROGRESS;
        }
        if (!((long) payment.getPrice() == amount)) {
            log.error("ERROR: 금액 불일치! [주문번호: " + orderId + "] DB 저장 금액: " + (long)payment.getPrice() + ", 실제 결제 금액: " + amount);
            return ValidationResult.AMOUNT_MISMATCH;
        }
        return ValidationResult.VALID;
    }
}
