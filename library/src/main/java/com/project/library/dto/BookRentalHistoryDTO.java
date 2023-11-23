package com.project.library.dto;


import com.project.library.entity.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookRentalHistoryDTO {



    private BookInfoDTO bookInfoDTO;

    private String rentalStartDate; // 대여 시작 날짜

    private String rentalEndDate; //대여 종료 날짜

    private boolean rentalState; //반납 여부

    private String memberId;




}
