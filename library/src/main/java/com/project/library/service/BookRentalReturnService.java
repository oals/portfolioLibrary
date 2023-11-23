package com.project.library.service;

import com.project.library.dto.*;
import com.project.library.entity.BookInfo;
import com.project.library.entity.BookRentalHistory;
import com.project.library.entity.BookRentalReturnHistory;
import com.project.library.entity.Member;

import java.util.ArrayList;
import java.util.List;

public interface BookRentalReturnService {

    default BookRentalReturnHistory returnBookDtoToEntity(BookRentalReturnHistoryDTO bookRentalReturnHistoryDTO){


        Member member = Member.builder()
                .memberId(bookRentalReturnHistoryDTO.getMemberId())
                .build();

        BookInfo bookInfo = BookInfo.builder()
                .bookName(bookRentalReturnHistoryDTO.getBookInfoDTO().getBookName())
                .build();


        BookRentalReturnHistory bookRentalReturnHistory = BookRentalReturnHistory.builder()
                .member(member)
                .bookInfo(bookInfo)
                .returnDate(bookRentalReturnHistoryDTO.getReturnDate())
                .returnState(bookRentalReturnHistoryDTO.isReturnState())
                .member(member)
                .build();


        return bookRentalReturnHistory;
    }

    default List<BookRentalReturnHistoryDTO> returnBookEntityToDtoList (List<BookRentalReturnHistory> bookRentalReturnHistoryS){


       List<BookRentalReturnHistoryDTO> bookRentalReturnHistoryDTOS = new ArrayList<>();


       for(int i = 0;  i<bookRentalReturnHistoryS.size(); i++) {

           BookInfoDTO bookInfoDTO = BookInfoDTO.builder()
                   .bookName(bookRentalReturnHistoryS.get(i).getBookInfo().getBookName())
                   .bookAuthor(bookRentalReturnHistoryS.get(i).getBookInfo().getBookAuthor())
                   .bookImg(bookRentalReturnHistoryS.get(i).getBookInfo().getBookImg())
                   .bookCategory(bookRentalReturnHistoryS.get(i).getBookInfo().getBookCategory())
                   .bookPublisher(bookRentalReturnHistoryS.get(i).getBookInfo().getBookPublisher())
                   .build();

           BookRentalReturnHistoryDTO  bookRentalReturnHistoryDTO = BookRentalReturnHistoryDTO.builder()
                   .memberId(bookRentalReturnHistoryS.get(i).getMember().getMemberId())
                   .bookInfoDTO(bookInfoDTO)
                   .returnDate(bookRentalReturnHistoryS.get(i).getReturnDate())
                   .returnState(bookRentalReturnHistoryS.get(i).isReturnState())
                   .build();


           bookRentalReturnHistoryDTOS.add(bookRentalReturnHistoryDTO);
       }


        return bookRentalReturnHistoryDTOS;
    }




    public void returnBook(BookInfoDTO bookInfoDTO, String memberId);


    public List<BookRentalReturnHistoryDTO> rentalReturnHistory(String memberId, PageRequestDTO pageRequestDTO,boolean chk);

    public Long rentalReturnCount(String memberId,boolean chk);

    public void rentalBookComplete(String bookName,String memberId);

}
