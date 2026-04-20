package com.petcation.admin.member.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petcation.admin.member.dao.MemberDao;
import com.petcation.admin.member.vo.MemberVO;
import com.petcation.client.community.vo.CommunityBoardVO;
import com.petcation.client.reservation.vo.ReservVO;
import com.petcation.client.traveldiary.vo.DiaryVO;

import lombok.Setter;

@Service
public class Admin_MemberServiceImpl implements MemberService {
	
	@Setter(onMethod_ = @Autowired)
	private MemberDao memberDao;
	
	@Override
	public List<MemberVO> AdminMemberList(MemberVO mvo) {
		List<MemberVO> list = memberDao.AdminMemberList(mvo);
		return list;
	}
	
	
	@Override
	public MemberVO AdminMemberDetail(MemberVO mvo) {
		MemberVO detail = memberDao.memberDetail(mvo);
		
		memberDao.memberDetail(mvo);
		return detail;
	}
	
	
	
	@Override
	public int AdminMemberDelete(MemberVO mvo) {
		int result = 0;
		result = memberDao.AdminMemberDelete(mvo);
		return result;
	}


	@Override
	public List<CommunityBoardVO> memberBoardList(MemberVO mvo) {
		List<CommunityBoardVO> boardList = memberDao.memberBoardList(mvo);
		return boardList;
	}


	@Override
	public CommunityBoardVO memberBoardDetail(CommunityBoardVO cbvo) {
		CommunityBoardVO detail = memberDao.memberBoardDetail(cbvo);
		
		if(detail != null) {
			detail.setB_content(detail.getB_content().toString().replace("\n", "<br/>"));
		}
		return detail;
	}


	@Override
	public List<ReservVO> reservList(ReservVO rvo) {
		List<ReservVO> reservList = null;
		reservList = memberDao.reservList(rvo);
		
		return reservList;
	}
	


	@Override
	public int memberBoardDelete(CommunityBoardVO cbvo) {
		int result = memberDao.memberBoardDelete(cbvo);
		return result;
	}


	@Override
	public List<DiaryVO> memberDiaryList(MemberVO mvo) {
		List<DiaryVO> diaryList = memberDao.memberDiaryList(mvo);
		return diaryList;
	}
	
	@Override
	 public DiaryVO memberDiaryDetail(DiaryVO dvo) {
	      DiaryVO detail = memberDao.memberDiaryDetail(dvo);
	      if(detail != null) {
	         detail.setD_content(detail.getD_content().toString().replace("\n", "<br/>"));
	      }
	      return detail;
	   }

	@Override
	   public int memberDiaryDelete(DiaryVO dvo) {
	      int result = 0;
	      result = memberDao.memberDiaryDelete(dvo);
	      return result;
	   }


	@Override
	public int reservDelete(ReservVO rvo) {
		int result = 0;
		result = memberDao.memberReservDelete(rvo);
		return result;
	}

	
	@Override
	public int memberListCnt(MemberVO mvo) { 
		return memberDao.memberListCnt(mvo);
	}
	
	@Override
	public int reservListCnt(ReservVO rvo) {
		return memberDao.reservListCnt(rvo);
	}
}
