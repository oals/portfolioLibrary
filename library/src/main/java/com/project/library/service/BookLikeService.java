package com.project.library.service;

import com.project.library.dto.BookInfoDTO;
import com.project.library.dto.BookLikeHistoryDTO;
import com.project.library.dto.BookRentalHistoryDTO;
import com.project.library.dto.PageRequestDTO;
import com.project.library.entity.BookInfo;
import com.project.library.entity.BookLikeHistory;
import com.project.library.entity.BookRentalHistory;
import com.project.library.entity.Member;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public interface BookLikeService {


    default List<BookLikeHistoryDTO> BookLikeEntityToDto(List<BookLikeHistory> bookLikeHistories){


        List<BookLikeHistoryDTO> bookLikeHistoryDTOS = new ArrayList<>();



        for(int i = 0 ; i < bookLikeHistories.size(); i++){

            BookInfoDTO bookInfoDTO = BookInfoDTO.builder()
                    .bookName(bookLikeHistories.get(i).getBookInfo().getBookName())
                    .bookAuthor(bookLikeHistories.get(i).getBookInfo().getBookAuthor())
                    .bookImg(bookLikeHistories.get(i).getBookInfo().getBookImg())
                    .bookCategory(bookLikeHistories.get(i).getBookInfo().getBookCategory())
                    .bookPublisher(bookLikeHistories.get(i).getBookInfo().getBookPublisher())
                    .build();

            BookLikeHistoryDTO bookLikeHistoryDTO = BookLikeHistoryDTO.builder()
                    .bookInfoDTO(bookInfoDTO)
                    .memberId(bookLikeHistories.get(i).getMember().getMemberId())
                    .likeRentalState(bookLikeHistories.get(i).isLikeRentalState())
                    .likeDate(bookLikeHistories.get(i).getLikeDate())
                    .build();


            bookLikeHistoryDTOS.add(bookLikeHistoryDTO);
        }

        return bookLikeHistoryDTOS;

    }



    public List<BookLikeHistoryDTO> BookLikeHistory(String memberId, PageRequestDTO pageRequestDTO);


    public Long BookLikeCount(String memberId);


    public Long LikeBookDel(String memberId,String bookName);


}
