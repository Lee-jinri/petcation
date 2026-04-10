package com.petcation.client.hotel.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.petcation.admin.hotel.vo.HotelVO;
import com.petcation.client.hotel.dao.User_HotelDao;
import com.petcation.client.hotel.vo.User_HotelVO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class User_HotelServiceImpl implements User_HotelService {

	private User_HotelDao User_hotelDao;

	@Override
	public List<HotelVO> hotelList(HotelVO hvo) {
		List<HotelVO> list = null;
		list = User_hotelDao.hotelList(hvo);
		return list;
	}

	@Override
	public User_HotelVO hotelDetail(User_HotelVO uhvo) {
		User_HotelVO detail = null;
		detail = User_hotelDao.hotelDetail(uhvo);
		if(detail != null) {
			detail.setHotel_info(detail.getHotel_info().toString().replace("\n", "<br/>"));
		}

		return detail;
	}
	
	@Override
	public int hotelListCnt(HotelVO hvo) {
		return User_hotelDao.hotelListCnt(hvo);
	}
	
    @Override
    public int getPriceByHotelNo(int hotelNo) {
        return User_hotelDao.getPriceByHotelNo(hotelNo);
    }
}
