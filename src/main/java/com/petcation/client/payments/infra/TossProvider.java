package com.petcation.client.payments.infra;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.petcation.client.payments.vo.TossResponseDto;

import lombok.extern.log4j.Log4j;

@Component
@Log4j
public class TossProvider {

    @Value("${toss.secret-key}")
    private String secretKey;

    public TossResponseDto confirm(String paymentKey, String orderId, Long amount) {
        String jsonBody = String.format(
            "{\"paymentKey\":\"%s\",\"orderId\":\"%s\",\"amount\":%d}", 
            paymentKey, orderId, amount
        );
        
        String encoded = Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));
        
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/payments/confirm"))
                .header("Authorization", "Basic " + encoded)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
            
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            
            log.info("응답 상태 코드: " + response.statusCode());
            log.info("응답 바디: " + response.body());
            
            if (response.statusCode() == 200) {
                String responseBody = response.body();
                JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                String status = jsonObject.get("status").getAsString();
                if ("DONE".equals(status)) {
                    String method = jsonObject.get("method").getAsString();
                    log.info("결제 승인 완료/ 주문번호: " + orderId + " 결제수단: " + method);
                    return TossResponseDto.success(
                        status, 
                        jsonObject.get("method").getAsString(),
                        jsonObject.get("paymentKey").getAsString()
                    );
                }
            }
            String responseBody = response.body();
            JsonObject errorObject = JsonParser.parseString(responseBody).getAsJsonObject();
            
            String errorCode = errorObject.get("code").getAsString();
            String errorMessage = errorObject.get("message").getAsString();
            
            log.error("결제 승인 실패 [코드: " + errorCode + "] 메시지: " + errorMessage);
            return TossResponseDto.fail(errorCode, errorMessage);
        } catch (IOException | InterruptedException e) {
            log.error("토스 API 호출 실패: "+ e.getMessage());
            return TossResponseDto.fail("NETWORK_ERROR", "결제 통신 중 오류가 발생했습니다.");
        }
    }
    public TossResponseDto cancel(String paymentKey, String orderId) {
        String encoded = Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));
        
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel"))
                .header("Authorization", "Basic " + encoded)
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString("{\"cancelReason\":\"구매자 변심\"}"))
                .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            
            log.info("응답 상태 코드: " + response.statusCode());
            log.info("응답 바디: " + response.body());
            
            if (response.statusCode() == 200) {
                String status = jsonObject.get("status").getAsString();
                if ("CANCELED".equals(status)) {
                    return TossResponseDto.success(status, null, paymentKey);
                }
            }
            String code = jsonObject.has("code") ? jsonObject.get("code").getAsString() : "CANCEL_FAILED";
            String msg = jsonObject.has("message") ? jsonObject.get("message").getAsString() : "취소 실패";
            
            log.error("토스 취소 실패 [코드: " + code + "] 메시지: " + msg);
            return TossResponseDto.fail(code, msg);
        }catch (IOException | InterruptedException e) {
            log.error("토스 API 호출 실패: "+ e.getMessage());
            return TossResponseDto.fail("NETWORK_ERROR", "통신 장애로 취소에 실패했습니다.");
        }
    }
}
