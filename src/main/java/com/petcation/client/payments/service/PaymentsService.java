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
import com.petcation.client.payments.vo.WebhookDto;

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
        if (!preValidate(orderId, amount)) return false;
        
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
                    
                    updatePaymentComplete(orderId, paymentKey, method);
                    return true;
                }
            }
            String responseBody = response.body();
            JsonObject errorObject = JsonParser.parseString(responseBody).getAsJsonObject();
            
            String errorCode = errorObject.get("code").getAsString();
            String errorMessage = errorObject.get("message").getAsString();
            
            log.error("결제 승인 실패 [코드: " + errorCode + "] 메시지: " + errorMessage);
            throw new RuntimeException(errorMessage);
        } catch (IOException | InterruptedException e) {
            log.error("토스 API 호출 실패: "+ e.getMessage());
            return false;
        }
    }
	
	@Transactional
	public void processWebhook(WebhookDto.PaymentData data) {
	    if (!preValidate(data.getOrderId(), data.getTotalAmount())) return;

	    updatePaymentComplete(data.getOrderId(), data.getPaymentKey(), data.getMethod());
	}

	private boolean preValidate(String orderId, Long amount) {
	    PaymentsVO payment = paymentsDao.getPayment(orderId);
	    
	    if (payment == null) {
	        log.warn("존재하지 않는 주문번호: " + orderId);
	        return false;
	    }
	    
	    if ("DONE".equals(payment.getStatus())) {
	        log.info("이미 결제 완료된 주문입니다: " +  orderId);
	        return false; 
	    }

	    Long originalAmount = (long)payment.getPrice();
	    if (!originalAmount.equals(amount)) {
	        log.error("ERROR: 금액 불일치! [주문번호: " + orderId + "] DB 저장 금액: " + originalAmount + ", 실제 결제 금액: " + amount);
	        return false;
	    }

	    return true;
    }

    // DB 업데이트 로직
	private void updatePaymentComplete(String orderId, String paymentKey, String method) {
	    paymentsDao.completePayment(orderId, paymentKey, method);
	    log.info("결제 완료: 주문번호 "+ orderId);
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
                    paymentsDao.updatePaymentStatus(orderId, "CANCELED");
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

    public void updatePaymentStatus(String orderId, String status) {
        paymentsDao.updatePaymentStatus(orderId, status);
    }
}
