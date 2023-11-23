package com.project.library.service;

import com.project.library.dto.BookHopeHistoryDTO;
import com.project.library.dto.BookInfoDTO;
import com.project.library.dto.PageRequestDTO;
import com.project.library.entity.BookHopeHistory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public interface BookHopeService {


    default List<BookHopeHistoryDTO> bookHopeEntityToDto(List<BookHopeHistory> bookHopeHistories){

        List<BookHopeHistoryDTO> bookHopeHistoryDTOS = new ArrayList<>();


        for(int i = 0; i < bookHopeHistories.size(); i++){

            BookInfoDTO bookInfoDTO = BookInfoDTO.builder()
                    .bookName(bookHopeHistories.get(i).getBookInfo().getBookName())
                    .bookAuthor(bookHopeHistories.get(i).getBookInfo().getBookAuthor())
                    .bookImg(bookHopeHistories.get(i).getBookInfo().getBookImg())
                    .bookCategory(bookHopeHistories.get(i).getBookInfo().getBookCategory())
                    .bookPublisher(bookHopeHistories.get(i).getBookInfo().getBookPublisher())
                    .build();

            BookHopeHistoryDTO bookHopeHistoryDTO = BookHopeHistoryDTO.builder()
                    .bookInfoDTO(bookInfoDTO)
                    .hopeDate(bookHopeHistories.get(i).getHopeDate())
                    .hopeState(bookHopeHistories.get(i).isHopeState())
                    .memberId(bookHopeHistories.get(i).getMember().getMemberId())
                    .build();

            bookHopeHistoryDTOS.add(bookHopeHistoryDTO);


        }



        return bookHopeHistoryDTOS;

    }



    public boolean memberHopeBook(BookInfoDTO bookInfoDTO, String memberId);


    public List<BookHopeHistoryDTO> bookHopeHistory(String memberId, PageRequestDTO pageRequestDTO);


    public Long bookHopeCount(String memberId);

    public Long bookHopeDel(String memberId,String bookName);
}
