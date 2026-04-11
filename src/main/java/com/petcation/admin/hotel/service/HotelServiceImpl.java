package com.petcation.admin.hotel.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.petcation.admin.hotel.vo.HotelVO;
import com.petcation.common.file.FileUploadUtil;
import com.petcation.admin.hotel.dao.HotelDao;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HotelServiceImpl implements HotelService {

	private HotelDao hotelDao;

	@Override
	public List<HotelVO> hotelList(HotelVO hvo) {
		List<HotelVO> list = null;
		list = hotelDao.hotelList(hvo);
		return list;
	}

	@Override
	public HotelVO hotelDetail(HotelVO hvo) {
		HotelVO detail = new HotelVO();
			
		detail = hotelDao.hotelDetail(hvo);
		if(detail != null) {
			detail.setHotel_info(detail.getHotel_info().toString().replace("\n", "<br/>"));
		}

		return detail;
	}

	@Override
	public int hotelInsert(HotelVO hvo) throws Exception {
		int result = 0;
		
		if(hvo.getFile().getSize() > 0) {
			String fileName = FileUploadUtil.fileUpload(hvo.getFile(), "hotel");
			hvo.setHotel_file(fileName);
			
			String thumbName = FileUploadUtil.makeThumbnail(fileName);
			hvo.setHotel_thumb(thumbName);
		}
		
		result = hotelDao.hotelInsert(hvo);
		return result;	
	}

	@Override
	public int hotelDelete(HotelVO hvo) throws Exception {
		int result = 0;
		result = hotelDao.hotelDelete(hvo.getHotel_no());
		return result;
	}
	
	@Override
	public HotelVO updateForm(HotelVO hvo){
		HotelVO detail = hotelDao.hotelDetail(hvo);
		return detail;
	}
	
	@Override
	public int hotelUpdate(HotelVO hvo) throws Exception {
		int result = 0;
		if(!hvo.getFile().isEmpty()) {
	         if(!hvo.getHotel_file().isEmpty()) {
	            FileUploadUtil.fileDelete(hvo.getHotel_thumb());
	         }
	         String fileName = FileUploadUtil.fileUpload(hvo.getFile(), "hotel");
	         hvo.setHotel_file(fileName);
	         
	         String thumbName = FileUploadUtil.makeThumbnail(fileName);
	         hvo.setHotel_thumb(thumbName);
	      }
		result = hotelDao.hotelUpdate(hvo);
		return result;
	}

	@Override
	public int hotelListCnt(HotelVO hvo) {
		return hotelDao.hotelListCnt(hvo);
	}



}
