package com.project.library.restController;


import com.project.library.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequiredArgsConstructor
public class MemberResController {


    private final MemberService memberService;

    @GetMapping("/memberIdCheck")
    public boolean memberIdCheck(String memberId){



       boolean chk = memberService.searchId(memberId);

       return chk;

    }



}
