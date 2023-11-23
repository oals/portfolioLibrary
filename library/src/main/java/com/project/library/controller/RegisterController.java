package com.project.library.controller;



import com.project.library.dto.MemberDTO;
import com.project.library.service.RegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Controller
@Log4j2
@RequiredArgsConstructor
public class RegisterController {


    private final RegisterService registerService;


    @GetMapping(value="/registerPage")
    public String registerPage(){

        log.info("register go ...");

        return "register/registerPage";
    }






    @PostMapping(value="/register")   //회원가입 및 계좌 생성 페이지로 전송
    public ModelAndView register(MemberDTO memberDTO, Model model){


        log.info("가입 정보 : "+memberDTO);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String registerDate = now.format(formatter);

        memberDTO.setBookRentalCount(0);
        memberDTO.setStudyRentalCount(0);
        memberDTO.setMemberDate(registerDate);

        //회원 정보 저장
        registerService.register(memberDTO);


        ModelAndView MAV = new ModelAndView();
        MAV.setViewName("redirect:/");
        return MAV;

    }



}
