package com.petcation.client.payments.dao;

import org.apache.ibatis.annotations.Param;

import com.petcation.client.payments.vo.PaymentsVO;

public interface PaymentsDAO {
    void createPayment(@Param("order_id") String orderId, @Param("price") int price, @Param("user_no") int userNo);
    PaymentsVO getPayment(String orderId);
    void completePayment(@Param("payment_key") String paymentKey, @Param("method") String method, @Param("orderId") String orderId);
    void failPayment(String orderId);
    void cancelPayment(String orderId);
    String getPaymentKey(String orderId);
}
