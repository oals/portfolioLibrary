package com.project.library.service;

import com.project.library.dto.MemberDTO;
import com.project.library.entity.Member;
import com.project.library.repository.RegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService{

    private final PasswordEncoder passwordEncoder;
    private final RegisterRepository registerRepository;



    @Override
    public void register(MemberDTO memberDTO) {

        Member member = Member.createMember(memberDTO,passwordEncoder);

        registerRepository.save(member);


    }
}
