package com.petcation.client.payments.service;

import org.springframework.stereotype.Service;

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
	
    // DB 업데이트 로직
    public void updatePaymentComplete(String orderId, String paymentKey, String method) {
        paymentsDao.completePayment(orderId, paymentKey, method);
        log.info("결제 완료: 주문번호 "+ orderId);
    }
    
    public void failPayment(String orderId) {
        paymentsDao.failPayment(orderId);
    }

    public void cancelPayment(String orderId) {
        paymentsDao.cancelPayment(orderId);
    }
    
    public PaymentsVO getPayment(String orderId) {
        return paymentsDao.getPayment(orderId);
    }

    public String getPaymentKey(String orderId) {
        return paymentsDao.getPaymentKey(orderId);
    }
}
