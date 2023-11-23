package com.project.library.dto;

import lombok.*;

import java.util.Date;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberDTO {

    private String memberId; // 아이디

    private String memberName;

    private String memberPswd;

    private String memberAge;

    private String memberAddress;

    private String memberPhone;

    private int bookRentalCount;

    private int studyRentalCount;

    private String memberDate;


}
