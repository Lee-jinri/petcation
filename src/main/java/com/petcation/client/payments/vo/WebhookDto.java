package com.petcation.client.payments.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter 
@NoArgsConstructor
@ToString
public class WebhookDto {
    private String eventType;
    private String createdAt;
    private PaymentData data;

    @Getter
    @NoArgsConstructor
    @ToString
    public static class PaymentData {
        private String paymentKey;   // 토스 결제 고유 키
        private String orderId;      // 주문번호
        private String status;       // DONE, CANCELED 등
        private Long totalAmount;    // 결제 금액
        private String method;       // 카드, 가상계좌 등
        private String approvedAt;   // 결제 승인 시간
    }
}
