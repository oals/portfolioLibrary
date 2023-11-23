package com.project.library.dto;

import com.project.library.entity.BookInfo;
import com.project.library.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookLikeHistoryDTO {

    private BookInfoDTO bookInfoDTO;

    private boolean likeRentalState; //대여 가능 여부

    private String likeDate;

    private String memberId;




}
