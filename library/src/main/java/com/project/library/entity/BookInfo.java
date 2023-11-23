package com.project.library.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class BookInfo {

    @Id
    @Column(name="book_Name")
    private String bookName; //도서명
    private String bookImg; //도서 이미지
    private String  bookAuthor; // 저자
    private String  bookPublisher; // 출판사
    private String  bookCategory; //도서 카테고리




}
