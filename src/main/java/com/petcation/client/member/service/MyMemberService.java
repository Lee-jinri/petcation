package com.petcation.client.member.service;

import java.util.List;

import com.petcation.client.community.vo.CommunityBoardVO;
import com.petcation.client.join.vo.MemberVO;
import com.petcation.client.reservation.vo.ReservVO;
import com.petcation.client.traveldiary.vo.DiaryVO;

public interface MyMemberService {

	public MemberVO mypageMain(MemberVO mvo);

	public int pwdCheck(String user_pw);

	public int updateInfo(MemberVO mvo);

	public MemberVO updateInfoForm(MemberVO mvo);

	public List<ReservVO> reservList(MemberVO mvo);
	
	public List<CommunityBoardVO> communityBoardList(MemberVO mvo);
	
	public List<DiaryVO> diaryList (MemberVO mvo);

	public int memberDelete(MemberVO vo);

	public int reservListCnt(ReservVO rvo);
	
	public int diaryListCnt(DiaryVO qvo);
	
	public int communityBoardListCnt(CommunityBoardVO cbvo);
	
}
