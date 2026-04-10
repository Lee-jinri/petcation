package com.petcation.client.payments.vo;

import lombok.Data;

@Data
public class PaymentsVO {
	private String  paymentId;
	private String  orderId;
	private int 	hotelNo;
	private int 	price;
	private String  status;
	private int     userNo;
	
    private String checkin;
    private String checkout;
}