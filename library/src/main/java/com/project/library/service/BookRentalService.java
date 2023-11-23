package com.project.library.service;

import com.project.library.dto.*;
import com.project.library.entity.BookInfo;
import com.project.library.entity.BookLikeHistory;
import com.project.library.entity.BookRentalHistory;
import com.project.library.entity.Member;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface BookRentalService {

    default BookLikeHistory likeBookDtoToEntity(BookLikeHistoryDTO bookLikeHistoryDTO){


        Member member = Member.builder()
                .memberId(bookLikeHistoryDTO.getMemberId())
                .build();


        BookInfo bookInfo = BookInfo.builder()
                .bookName(bookLikeHistoryDTO.getBookInfoDTO().getBookName())
                .build();


        BookLikeHistory bookLikeHistory = BookLikeHistory.builder()
                .member(member)
                .bookInfo(bookInfo)
                .likeRentalState(bookLikeHistoryDTO.isLikeRentalState())
                .likeDate(bookLikeHistoryDTO.getLikeDate())
                .build();


        return bookLikeHistory;
    }




    default BookRentalHistory rentalBookDtoToEntity(BookRentalHistoryDTO bookRentalHistoryDTO){


        Member member = Member.builder()
                .memberId(bookRentalHistoryDTO.getMemberId())
                .build();

        BookInfo bookInfo = BookInfo.builder()
                .bookName(bookRentalHistoryDTO.getBookInfoDTO().getBookName())
                .build();


        BookRentalHistory bookRentalHistory = BookRentalHistory.builder()
                .member(member)
                .bookInfo(bookInfo)
                .rentalState(bookRentalHistoryDTO.isRentalState())
                .rentalStartDate(bookRentalHistoryDTO.getRentalStartDate())
                .rentalEndDate(bookRentalHistoryDTO.getRentalEndDate())
                .build();


        return bookRentalHistory;
    }


    default List<BookRentalHistoryDTO> rentalBookEntityToDto(List<BookRentalHistory> bookRentalHistory){


        List<BookRentalHistoryDTO> list = new ArrayList<>();

        for(int i = 0; i<bookRentalHistory.size(); i++) {

            BookInfoDTO bookInfoDTO = BookInfoDTO.builder()
                    .bookName(bookRentalHistory.get(i).getBookInfo().getBookName())
                    .bookAuthor(bookRentalHistory.get(i).getBookInfo().getBookAuthor())
                    .bookImg(bookRentalHistory.get(i).getBookInfo().getBookImg())
                    .bookCategory(bookRentalHistory.get(i).getBookInfo().getBookCategory())
                    .bookPublisher(bookRentalHistory.get(i).getBookInfo().getBookPublisher())
                    .build();

            BookRentalHistoryDTO bookRentalHistoryDTO = BookRentalHistoryDTO.builder()
                    .memberId(bookRentalHistory.get(i).getMember().getMemberId())
                    .bookInfoDTO(bookInfoDTO)
                    .rentalState(bookRentalHistory.get(i).isRentalState())
                    .rentalStartDate(bookRentalHistory.get(i).getRentalStartDate())
                    .rentalEndDate(bookRentalHistory.get(i).getRentalEndDate())
                    .build();

            list.add(bookRentalHistoryDTO);

        }
        return list;
    }




    public boolean likeBook(BookInfoDTO bookInfoDTO, String memberId);

    public String rentalBook(BookInfoDTO bookInfoDTO,String memberId);


    public String rentalCountChk(String memberId);

    public List<BookRentalHistoryDTO> rentalBookHistory(String memberId);




}
