package com.petcation.client.reservation.vo;

import com.petcation.common.vo.CommonVO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ReservVO extends CommonVO {
	private	int		reserv_no 		= 0; 	// 예약번호
	private String	reserv_name 	= ""; 	// 예약자명
	private String 	reserv_phone 	= "";	// 예약자 핸드폰 번호
	private int		reserv_people 	= 0;	// 예약인원
	private String 	reserv_status ;	        // 예약 상태 (READY, DONE, CANCLED)
	private String	checkin			= "";	// 체크인 날짜
	private String	checkout		= ""; 	// 체크아웃 날짜
	private int 	reserv_price	= 0;	// 예약 가격
	private String	reserv_date		= "";	// 예약일
	private String  orderId = "";	        // 결제번호
	private int		user_no			= 0;	// 회원 일련번호
	private String	hotel_no		= ""; 	// 숙소 일련번호
	private String 	hotel_name;
	private String 	reserv_etc;
	private String 	user_id			= "";   // 회원 아이디
	
	private int		reserv_status_val = 0;
	private int		reserv_cnt 		= 0; 	// 당일 결제 건수
	private int		reserv_pay_cnt 	= 0;	// 당일 결제 금액
	
}