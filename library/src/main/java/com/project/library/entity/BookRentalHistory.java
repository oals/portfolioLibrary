package com.project.library.entity;


import com.project.library.dto.BookInfoDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BookRentalHistory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "book_Name")
    private BookInfo bookInfo; //도서 정보


    private String rentalStartDate; // 대여 시작 날짜

    private String rentalEndDate; //대여 종료 날짜

    @Column(columnDefinition = "TINYINT(1)")
    private boolean rentalState; //반납 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //사용자 정보




}
