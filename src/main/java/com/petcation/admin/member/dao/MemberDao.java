package com.petcation.admin.member.dao;

import java.util.List;

import com.petcation.admin.member.vo.MemberVO;
import com.petcation.client.community.vo.CommunityBoardVO;
import com.petcation.client.reservation.vo.ReservVO;
import com.petcation.client.traveldiary.vo.DiaryVO;

public interface MemberDao {
	public List<MemberVO> AdminMemberList(MemberVO mvo);
	//public int DeleteMember(MemberVO mvo);

	public int AdminMemberDelete(MemberVO mvo);

	public MemberVO memberDetail(MemberVO mvo);

	public List<CommunityBoardVO> memberBoardList(MemberVO mvo);

	public CommunityBoardVO memberBoardDetail(CommunityBoardVO cbvo);

	public List<ReservVO> reservList(ReservVO rvo);

	public int memberBoardDelete(CommunityBoardVO cbvo);

	public List<DiaryVO> memberDiaryList(MemberVO mvo);

	public DiaryVO memberDiaryDetail(DiaryVO dvo);

	public int memberDiaryDelete(DiaryVO dvo);

	public int memberReservDelete(ReservVO rvo);
	
	public int memberListCnt(MemberVO mvo);

	public int reservListCnt(ReservVO rvo);
	

}
