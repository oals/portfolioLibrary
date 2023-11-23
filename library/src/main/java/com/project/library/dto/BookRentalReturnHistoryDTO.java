package com.project.library.dto;


import com.project.library.entity.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookRentalReturnHistoryDTO {

    private Long id;

    private BookInfoDTO bookInfoDTO;

    private String returnDate;

    private boolean returnState; //반납 여부

    private String memberId;




}
