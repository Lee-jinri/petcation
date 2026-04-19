package com.petcation.client.reservation.service;

import java.time.LocalDateTime;
import java.util.List;

import com.petcation.client.hotel.vo.User_HotelVO;
import com.petcation.client.reservation.vo.ReservVO;
import com.petcation.common.vo.ReservationRequestDTO;

public interface ReservService {
	
	public User_HotelVO hotelVO(User_HotelVO uhvo); 
	// public List<ReservVO> reservList(ReservVO rvo) throws Exception;
	public int reservInsert(ReservationRequestDTO req);
	public ReservVO reservResult(String orderId);

	public List<ReservVO> reservDate(int hotel_no);
    public boolean isBooked(int hotelNo, LocalDateTime checkin, LocalDateTime checkout);
    public void completeReservation(String orderId);
    public ReservVO getReservForCancel(String orderId);
    void updateReservStatus(String orderId, String status);
}
