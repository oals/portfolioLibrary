package com.project.library.entity;


import com.project.library.constant.Role;
import com.project.library.dto.MemberDTO;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="member")
public class Member {

    @Id
    @Column(name="member_id")
    private String memberId; // 아이디

    private String memberName;

    private String memberPswd;

    private String memberAge;

    private String memberAddress;

    private String memberPhone;

    private int bookRentalCount;

    private int studyRentalCount;

    private String memberDate;

    @Enumerated(EnumType.STRING)
    private Role role;  //권한 설정
    public static Member createMember(MemberDTO memberDTO, PasswordEncoder passwordEncoder){

        Member member = new Member();
        member.setMemberId(memberDTO.getMemberId());
        member.setMemberName(memberDTO.getMemberName());
        member.setMemberPhone(memberDTO.getMemberPhone());
        member.setMemberAddress(memberDTO.getMemberAddress());
        member.setMemberAge(memberDTO.getMemberAge());
        member.setBookRentalCount(memberDTO.getBookRentalCount());
        member.setStudyRentalCount(memberDTO.getStudyRentalCount());
        member.setMemberDate(memberDTO.getMemberDate());

        member.setRole(Role.USER);

        // 암호화
        String password = passwordEncoder.encode(memberDTO.getMemberPswd());
        member.setMemberPswd(password);

        return member;
    }



}







