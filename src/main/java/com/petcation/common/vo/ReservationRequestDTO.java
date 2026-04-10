package com.petcation.common.vo;

import lombok.Data;

@Data
public class ReservationRequestDTO {
    private String  paymentId;
    private String  orderId;
    private int     price;
    private String  status; // READY, DONE, FAIL, CANCELED 
    
    private int     reserv_no;
    private String  reserv_name;
    private String  reserv_phone;
    private int     reserv_people;
    private int     reserv_status; // READY, DONE, CANCELED
    private String  reserv_etc;

    private int     userNo;
    private int     hotelNo;
    private String  hotelName;
    
    private String checkin;
    private String checkout;
}
