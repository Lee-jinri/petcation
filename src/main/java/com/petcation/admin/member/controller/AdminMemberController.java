package com.petcation.admin.member.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petcation.admin.member.service.MemberService;
import com.petcation.admin.member.vo.MemberVO;
import com.petcation.client.community.vo.CommunityBoardVO;
import com.petcation.client.reservation.vo.ReservVO;
import com.petcation.client.traveldiary.vo.DiaryVO;
import com.petcation.common.vo.PageDTO;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/admin/member/*")
@AllArgsConstructor
public class AdminMemberController {
	
	private MemberService adminMemberService;
	
	@RequestMapping(value = "/memberList", method = RequestMethod.GET)
	public String AdminMemberList(@ModelAttribute("data") MemberVO mvo, Model model) {
		
		List<MemberVO> memberList = adminMemberService.AdminMemberList(mvo);
		model.addAttribute("memberList", memberList);
		
		
		int total = adminMemberService.memberListCnt(mvo);
		model.addAttribute("pageMaker", new PageDTO(mvo, total));
		
		return "admin/member/memberList";
	}
	
	@RequestMapping(value = "/memberDetail")
	public String memberDetail(@ModelAttribute("data") MemberVO mvo, Model model) {
		MemberVO detail = adminMemberService.AdminMemberDetail(mvo);
		model.addAttribute("detail", detail);
		
		return "admin/member/memberDetail";
	}
	

	@RequestMapping(value = "/memberDelete")
	public String AdminMemberDelete(@ModelAttribute MemberVO mvo, RedirectAttributes ras) throws Exception {
	    int result = 0;
	    String url = "";
	      
	    result = adminMemberService.AdminMemberDelete(mvo);
	    ras.addFlashAttribute("MemberVO", mvo);
	      
	     if(result == 1) {
	        url = "/admin/member/memberList";
	     }
	      
	      return "redirect:" + url;
	}
	
	@RequestMapping(value = "/memberBoardList", method = RequestMethod.GET)
	public String memberBoardList(@ModelAttribute("data") MemberVO mvo,Model model) {
		List<CommunityBoardVO> communityBoardList = adminMemberService.memberBoardList(mvo);
		List<DiaryVO> diaryList = adminMemberService.memberDiaryList(mvo);
		System.out.println(diaryList);
		model.addAttribute("communityBoardList", communityBoardList);
		model.addAttribute("diaryList", diaryList);
		return "admin/member/memberBoard";
	}
	
	@RequestMapping(value = "/memberBoardDetail", method = RequestMethod.GET)
	public String memberBoardDetail(@ModelAttribute("data") CommunityBoardVO cbvo, Model model) {
		CommunityBoardVO detail = adminMemberService.memberBoardDetail(cbvo);
		System.out.println(detail.toString());
		model.addAttribute("detail", detail);
		return "admin/member/memberBoardDetail";
	}
	@RequestMapping(value = "/memberBoardDelete", method = RequestMethod.GET)
	public String memberBoardDelete(@ModelAttribute("data") CommunityBoardVO cbvo, RedirectAttributes ras) throws Exception {
	   int result = 0;
	   String url = "";
	      
	   result = adminMemberService.memberBoardDelete(cbvo);
	   ras.addFlashAttribute("CommunityBoardVO", cbvo);
	      
	   if(result == 1) {
	      url = "/admin/member/memberBoardList?user_no="+cbvo.getUser_no();
	   }

	   return "redirect:"+url;
	}
	 
	@RequestMapping(value = "/reservList", method = RequestMethod.GET)
	public String memberReservList(@ModelAttribute("data") ReservVO rvo ,Model model) {
		 
		List<ReservVO> reservList = adminMemberService.reservList(rvo);
		model.addAttribute("reservList", reservList); 
		System.out.println(reservList);
		
		int total = adminMemberService.reservListCnt(rvo);
		model.addAttribute("pageMaker", new PageDTO(rvo, total));
		
		
		return "admin/member/reservList"; 
	}
	
	@RequestMapping(value = "/reservDelete", method = RequestMethod.GET)
	public String memberReservDelete(@ModelAttribute("data") ReservVO rvo ,Model model) {
		System.out.println("예약 취소 호출 성공");
		
		String url = "";
		
		int result = adminMemberService.reservDelete(rvo); 
		System.out.println(result);
		
		if(result == 1) {
			url = "/admin/member/reservList";
		}else {
			System.out.println("안됨");
		}
		
		return "redirect:"+url; 
	}
	
	@RequestMapping(value = "/memberDiaryDetail", method = RequestMethod.GET)
	public String memberDiaryDetail(@ModelAttribute("data") DiaryVO dvo, Model model) {
		DiaryVO detail = adminMemberService.memberDiaryDetail(dvo);
	      
		model.addAttribute("detail", detail);
		return "admin/member/memberDiaryDetail";
	}
	
	@RequestMapping(value = "/memberDiaryDelete", method = RequestMethod.POST)
	public String memberDiaryDelete(@ModelAttribute("data") DiaryVO dvo, RedirectAttributes ras) throws Exception {
		int result = 0;
		String url = "";
		result = adminMemberService.memberDiaryDelete(dvo);
		ras.addFlashAttribute("CommunityBoardVO", dvo);
	      
		if(result == 1) {
			url = "/admin/member/memberBoardList?user_id="+dvo.getUser_id();
		} else {
			url = "/admin/member/memberDiaryDetail";
		}

		return "redirect:"+url;
	}
	
}
