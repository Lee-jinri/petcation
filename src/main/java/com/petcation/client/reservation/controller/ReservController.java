package com.petcation.client.reservation.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.petcation.client.hotel.vo.User_HotelVO;
import com.petcation.client.join.vo.MemberVO;
import com.petcation.client.reservation.service.ReservService;
import com.petcation.client.reservation.vo.ReservVO;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/reserv/*")
@AllArgsConstructor
public class ReservController {

	private ReservService reservService;

	@RequestMapping(value = "/reservForm")
	public String reservForm(@ModelAttribute("data") User_HotelVO uhvo, ReservVO rvo, MemberVO mvo, HttpServletRequest request,Model model) throws Exception {
		String url ="";
		
		HttpSession session = request.getSession();
		
		MemberVO userID =(MemberVO)session.getAttribute("lmember");
		if(userID != null) {
            User_HotelVO hotelVO = reservService.hotelVO(uhvo);
            rvo.setUser_no(userID.getUser_no());
            model.addAttribute("hotelVO",hotelVO);

             url = "reserv/reservForm";
          } else {
        	  url = "member/login";
          }

		return url;
	}
	
	@ResponseBody
	@GetMapping(value = "/reservDate/{hotel_no}", produces=MediaType.APPLICATION_JSON_VALUE)
	   public List<ReservVO> reservDate(@PathVariable("hotel_no") @ModelAttribute("data") int hotel_no){
	      List<ReservVO> date = null;
	      date = reservService.reservDate(hotel_no);
	      return date;
	 }
}
