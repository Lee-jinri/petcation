package com.petcation.client.payments.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TossResponseDto {
    private boolean isSuccess;
    private String status;
    private String method;
    private String paymentKey;
    private String errorCode;
    private String errorMessage;

    public static TossResponseDto success(String status, String method, String paymentKey) {
        return new TossResponseDto(true, status, method, paymentKey, null, null);
    }

    public static TossResponseDto fail(String code, String message) {
        return new TossResponseDto(false, null, null, null, code, message);
    }
}
