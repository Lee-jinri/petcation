package com.petcation.client.reservation.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petcation.client.hotel.vo.User_HotelVO;
import com.petcation.client.reservation.dao.ReservDao;
import com.petcation.client.reservation.vo.ReservVO;
import com.petcation.common.exception.BadRequestException;
import com.petcation.common.vo.ReservationRequestDTO;

import lombok.Setter;

@Service
public class ReservServiceImpl implements ReservService {
	
	@Setter(onMethod_=@Autowired)
	private ReservDao reservDao;
	
	@Override
	public User_HotelVO hotelVO(User_HotelVO uhvo) {
		
		User_HotelVO hotelVO = reservDao.hotelVO(uhvo);
		return hotelVO;
	}
	
	
//	@Override
//	public List<ReservVO> reservList(ReservVO rvo) throws Exception {
//		List<ReservVO> reservList = null;
//		reservList = reservDao.reservList(rvo);
//		
//		return reservList;
//	}
	
	@Override
	public int reservInsert(ReservationRequestDTO req) {
		return reservDao.reservInsert(req);
	}

	@Override
	public ReservVO reservResult(String orderId) {
	    return reservDao.reservResult(orderId);
	}

	@Override
	public List<ReservVO> reservDate(int hotel_no) {
		List<ReservVO> reservVO = reservDao.reservDate(hotel_no);
		return reservVO;
	}


    @Override
    public boolean isBooked(int hotelNo, LocalDateTime checkin, LocalDateTime checkout) {
        int count = reservDao.isBooked(hotelNo, checkin, checkout);

        if (count > 0) {
            throw new BadRequestException("선택하신 날짜는 이미 예약이 완료되었습니다.");
        }
        return false;
    }

    @Override
    public void completeReservation(String orderId) {
        reservDao.completeReservation(orderId);
    }

    @Override
    public ReservVO getReservForCancel(String orderId) {
        return reservDao.getReservForCancel(orderId);
    }

    @Override
    public void cancelReservation(String orderId) {
        reservDao.cancelReservation(orderId);
    }
}
