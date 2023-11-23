package com.project.library.entity;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BookRentalReturnHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "book_Name")
    private BookInfo bookInfo; //도서 정보

    private String returnDate; //반납 날짜

    @Column(columnDefinition = "TINYINT(1)")
    private boolean returnState; //반납 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //사용자 정보




}
