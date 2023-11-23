package com.project.library.dto;

import com.project.library.entity.Member;
import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookHopeHistoryDTO {

    private Long id;

    private BookInfoDTO bookInfoDTO;

    private String hopeDate;  //신청 날짜

    private boolean hopeState; //도서 상태

    private String memberId;


}
