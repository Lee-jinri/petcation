package com.petcation.client.reservation.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.petcation.client.hotel.vo.User_HotelVO;
import com.petcation.client.join.vo.MemberVO;
import com.petcation.client.reservation.vo.ReservVO;
import com.petcation.common.vo.ReservationRequestDTO;

public interface ReservDao {
	
	public User_HotelVO hotelVO(User_HotelVO uhvo);
	public int reservForm(MemberVO mvo);
	//public List<ReservVO> reservList(ReservVO rvo);
	
	public int reservInsert(ReservationRequestDTO req);
	public ReservVO reservResult(String orderId);
	
	public List<ReservVO> reservDate(int hotel_no);
    public int isBooked(@Param("hotelNo") int hotelNo, @Param("checkin") LocalDateTime checkin, @Param("checkout") LocalDateTime checkout);
    public void completeReservation(String orderId);
    public ReservVO getReservForCancel(String orderId);
    public void updateReservStatus(@Param("orderId") String orderId, @Param("status") String status);
}
