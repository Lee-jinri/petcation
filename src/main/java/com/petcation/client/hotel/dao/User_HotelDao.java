package com.petcation.client.hotel.dao;

import java.util.List;

import com.petcation.admin.hotel.vo.HotelVO;
import com.petcation.client.hotel.vo.User_HotelVO;

public interface User_HotelDao {
	public List<HotelVO> hotelList(HotelVO hvo);

	public User_HotelVO hotelDetail(User_HotelVO uhvo);
	public int hotelListCnt(HotelVO hvo);
    public int getPriceByHotelNo(int hotelNo);
}
