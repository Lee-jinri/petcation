package com.petcation.client.payments.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.petcation.client.payments.dao.PaymentsDAO;
import com.petcation.client.payments.vo.PaymentsVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Service
@RequiredArgsConstructor
@Log4j
public class PaymentsService {

	private final PaymentsDAO paymentsDao;
	
	public void createPayment(String paymentId, int price, int userNo) {
		paymentsDao.createPayment(paymentId, price, userNo);
	}
	
	@Value("${toss.secret-key}")
	private String secretKey;
	
	@Transactional
    public boolean confirmPayment(String paymentKey, String orderId, Long amount) {
        PaymentsVO payment = paymentsDao.getPayment(orderId);
        if (payment == null) {
            return false;
        }
        
        Long originalAmount = (long)payment.getPrice();
        if (!originalAmount.equals(amount)) {
            return false;
        }
        
        if ("DONE".equals(payment.getStatus())) {
            return false;
        }
        
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
                    log.info("결제 승인 완료 주문번호: " + orderId + "결제수단: " + method);
                    
                    paymentsDao.completePayment(paymentKey, method, orderId);
                    return true;
                }
            }
            
            log.error("결제 승인 실패: ="+ response.body());
            return false;
        } catch (IOException | InterruptedException e) {
            log.error("토스 API 호출 실패: "+ e.getMessage());
            return false;
        }
    }

    public void failPayment(String orderId) {
        paymentsDao.failPayment(orderId);
    }

    @Transactional
    public void cancel(String orderId) {
        String encoded = Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));
        String paymentKey = paymentsDao.getPaymentKey(orderId);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel"))
                .header("Authorization", "Basic " + encoded)
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString("{\"cancelReason\":\"구매자 변심\"}"))
                .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            
            log.info("응답 상태 코드: " + response.statusCode());
            log.info("응답 바디: " + response.body());
            
            if (response.statusCode() == 200) {
                JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
                String status = jsonObject.get("status").getAsString();
                if ("CANCELED".equals(status)) {
                    paymentsDao.cancelPayment(orderId);
                } else {
                    throw new RuntimeException("결제 취소 상태가 올바르지 않습니다.");
                }
            } else {
                log.error("토스 취소 실패: " + response.body());
                throw new RuntimeException("결제 취소 API 호출에 실패했습니다.");
            }
        }catch (IOException | InterruptedException e) {
            log.error("토스 API 호출 실패: "+ e.getMessage());
            throw new RuntimeException("결제 취소 중 오류가 발생했습니다.");
        }
    }
}
