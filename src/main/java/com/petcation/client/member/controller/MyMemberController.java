package com.petcation.client.member.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petcation.client.community.vo.CommunityBoardVO;
import com.petcation.client.hotel.vo.User_HotelVO;
import com.petcation.client.join.vo.MemberVO;
import com.petcation.client.member.service.MyMemberService;
import com.petcation.client.member.vo.MyMemberVO;
import com.petcation.client.reservation.vo.ReservVO;
import com.petcation.client.traveldiary.vo.DiaryVO;
import com.petcation.common.vo.PageDTO;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/mypage/*")
@AllArgsConstructor
public class MyMemberController {
	private MyMemberService memberService;
	
	
	
	@Autowired(required = true)
	private BCryptPasswordEncoder encoder;
	
	@RequestMapping(value="/main", method=RequestMethod.GET)
    public String mypageMain(@ModelAttribute("data")MemberVO mvo, HttpServletRequest request, Model model) {
		
		String url = "";
		
       HttpSession session = request.getSession();
       MemberVO userID =(MemberVO)session.getAttribute("lmember");
          if(userID != null) {
    
             mvo = memberService.mypageMain(userID);
             
             mvo.setUser_no(userID.getUser_no());
             model.addAttribute("mainPage", mvo);
             System.out.println("mainPage 값 : "+mvo);
             
             url = "mypage/main";
          } else {
        	  url = "/";
          }
          
       return url;
       
    }
	
    
  //회원정보 수정전 비밀번호 확인 
    @RequestMapping(value = "/pwdCheck", method = RequestMethod.POST)
    @ResponseBody
    public String pwdCheckPOST(String user_pw, HttpServletRequest request) throws Exception {

       log.info("pwdCheck 진입");
       
       HttpSession session = request.getSession();
       MemberVO lvo = (MemberVO)session.getAttribute("lmember"); //로그인된 사용자 정보
       MemberVO pwVO = memberService.mypageMain(lvo); //로그인시 세션에 비밀번호 제외하고 담기 때문에 DB에서 가져오기 위함
       
       String encodePw = pwVO.getUser_pw(); //로그인한 사용자의 비밀번호
       log.info("로그인사용자의 비밀번호" + encodePw);
       String inputPw = user_pw; // 사용자가 입력한 비밀번호
       log.info("입력비밀번호" + inputPw);
       
       if (encoder.matches(inputPw, encodePw)) { //인코딩 비번과 입력비번이 일치하는지
          return "success";
       } else {
          return "fail";
       }
    }
    
    @RequestMapping(value = "updateInfoForm", method = RequestMethod.GET) public
    void updateInfoFormGET() {
    
       log.info("회원정보 수정 폼 이동");
    
    }

    @RequestMapping(value="/updateInfo", method=RequestMethod.POST)
    public String updateInfo(@ModelAttribute MemberVO vo, HttpServletRequest request) throws Exception {
       
       int result = 0;
       String url = "";
       HttpSession session = request.getSession();
       MemberVO lvo = (MemberVO)session.getAttribute("lmember");
       vo.setUser_no(lvo.getUser_no());
       
       String inputPw = vo.getUser_pw(); //입력비밀번호
       String encodePw = encoder.encode(inputPw); //인코딩 비밀번호
       vo.setUser_pw(encodePw); //인코딩비밀번호를 다시 vo에 저장
       
       result = memberService.updateInfo(vo); 
       //ras.addFlashAttribute("data", vo);
       
       if(result == 1) {
          url="/mypage/main";
       } else {
          url="/mypage/updateInfoForm";
       }
       
       return "redirect:"+url;
    }
    
    @RequestMapping(value="/deleteInfo", method=RequestMethod.POST)
    public String memberDelete(@ModelAttribute MemberVO vo) throws Exception {
    	int result = memberService.memberDelete(vo);
    	System.out.println("회원 탈퇴 결과 : " +result);
    	
    	return "/";
    }
    
	 @RequestMapping(value = "/reservList", method = RequestMethod.GET) 
	 public String reservList(@ModelAttribute("data") MemberVO mvo, ReservVO rvo, User_HotelVO hvo, HttpServletRequest request, Model model) {
		 log.info("마이페이지 예약내역 호출 성공 "); 
		 HttpSession session = request.getSession();
		 MemberVO userID = (MemberVO) session.getAttribute("lmember");
		 mvo.setUser_no(userID.getUser_no()); 
		 mvo.setUser_id(userID.getUser_id());
		 System.out.println("userID : " + mvo);
		 
		 List<ReservVO> reservList = memberService.reservList(mvo);
		 model.addAttribute("reservList", reservList);
		 int total = memberService.reservListCnt(rvo);
		 model.addAttribute("pageMaker", new PageDTO(rvo, total));

		 return "mypage/reservList"; 
	 }
	 
	 @RequestMapping(value = "/boardList", method = RequestMethod.GET) 
	 public String boardList(@ModelAttribute("data") MemberVO mvo, CommunityBoardVO cbvo, DiaryVO dvo, HttpServletRequest request, Model model) {
		 log.info("마이페이지 게시글내역 호출 성공 "); HttpSession session = request.getSession();
		 MemberVO userID = (MemberVO) session.getAttribute("lmember");
		 mvo.setUser_no(userID.getUser_no()); 
		 mvo.setUser_id(userID.getUser_id());
		 System.out.println("userID : " + mvo); 
		 List<CommunityBoardVO> communityBoardList = memberService.communityBoardList(mvo); 
		 List<DiaryVO> diaryList = memberService.diaryList(mvo);
		 model.addAttribute("communityBoardList", communityBoardList);
		 model.addAttribute("diaryList", diaryList);
		 System.out.println(communityBoardList); 
		 System.out.println(diaryList); 
		 int total = memberService.diaryListCnt(dvo);
		model.addAttribute("pageMaker", new PageDTO(dvo, total));
		 return "mypage/boardList"; 
	 }
	 
	

}
