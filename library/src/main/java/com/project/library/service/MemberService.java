package com.project.library.service;

import com.project.library.dto.MemberDTO;
import com.project.library.entity.Member;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {


    default MemberDTO EntityToDto(Member member){

        MemberDTO memberDTO = MemberDTO.builder()
                .memberId(member.getMemberId())
                .memberAddress(member.getMemberAddress())
                .memberAge(member.getMemberAge())
                .memberDate(member.getMemberDate())
                .memberName(member.getMemberName())
                .bookRentalCount(member.getBookRentalCount())
                .studyRentalCount(member.getStudyRentalCount())
                .memberPhone(member.getMemberPhone())
                .build();

        return memberDTO;

    }


    public MemberDTO myPageInfo(String memberId);


    boolean searchId(String memberId);




}
