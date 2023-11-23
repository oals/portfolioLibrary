package com.project.library.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j2
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;

    @GetMapping(value="/user/login")
    public String login(){
        log.info("login page view");

        return "login/LoginPage";
    }

    @PostMapping(value="/logout")
    public String logout(){
        log.info("logout... ");
        return "redirect :/user/login?logout";
    }

    @GetMapping("/user/login/error")
    public String loginerror(Model model){

        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호가 잘못되었습니다.");
        return "login/LoginPage";
    }









}
