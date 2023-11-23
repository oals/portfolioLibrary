package com.project.library.service;

import com.project.library.dto.MemberDTO;
import com.project.library.entity.Member;
import com.project.library.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService{


    private final MemberRepository memberRepository;
    @Override
    public MemberDTO myPageInfo(String memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow();

        MemberDTO memberDTO = this.EntityToDto(member);



        return memberDTO;
    }

    @Override
    public boolean searchId(String memberId) {


       Long count =  memberRepository.findById(memberId).stream().count();
       boolean chk = false;


       if(count == 0){
        chk =true;

       }


        return chk;
    }
}
