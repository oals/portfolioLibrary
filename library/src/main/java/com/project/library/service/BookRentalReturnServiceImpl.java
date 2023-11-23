package com.project.library.service;

import com.project.library.dto.BookInfoDTO;
import com.project.library.dto.BookRentalReturnHistoryDTO;
import com.project.library.dto.PageRequestDTO;
import com.project.library.entity.*;
import com.project.library.repository.BookRentalRepository;
import com.project.library.repository.BookRentalReturnRepository;
import com.project.library.repository.MemberRepository;
import com.querydsl.core.types.Order;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
@Log4j2
@RequiredArgsConstructor
public class BookRentalReturnServiceImpl implements BookRentalReturnService{

    @PersistenceContext
    EntityManager em;

    private final BookRentalRepository bookRentalRepository;
    private final BookRentalReturnRepository bookRentalReturnRepository;

    private final MemberRepository memberRepository;


    @Override
    public void returnBook(BookInfoDTO bookInfoDTO, String memberId) {

        //rentalHistory data 삭제

        bookRentalRepository.returnRentalState(memberId,bookInfoDTO.getBookName());

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
        String returnTime = now.format(formatter);
        //returnhistory data 추가

        BookRentalReturnHistoryDTO bookRentalReturnHistoryDTO = BookRentalReturnHistoryDTO.builder()
                .bookInfoDTO(bookInfoDTO)
                .returnDate(returnTime)
                .returnState(false)
                .memberId(memberId)
                .build();

        BookRentalReturnHistory bookRentalReturnHistory = this.returnBookDtoToEntity(bookRentalReturnHistoryDTO);


        bookRentalReturnRepository.save(bookRentalReturnHistory);




    }

    @Override
    public List<BookRentalReturnHistoryDTO> rentalReturnHistory(String memberId, PageRequestDTO pageRequestDTO,boolean chk) {


        Pageable pageable =pageRequestDTO.getPageable();


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBookRentalReturnHistory qBookRentalReturnHistory = QBookRentalReturnHistory.bookRentalReturnHistory;
        List<BookRentalReturnHistory> list = null;

        if(memberId.equals("admin")) {
            if(chk){
                list = queryFactory.selectFrom(qBookRentalReturnHistory)
                        .where(qBookRentalReturnHistory.returnState.eq(true))
                        .orderBy(qBookRentalReturnHistory.returnDate.desc())
                        .offset(pageable.getOffset())   //N 번부터 시작
                        .limit(pageable.getPageSize()) //조회 갯수
                        .fetch();

            }else{
                list = queryFactory.selectFrom(qBookRentalReturnHistory)
                        .where(qBookRentalReturnHistory.returnState.eq(false))
                        .orderBy(qBookRentalReturnHistory.returnDate.desc())
                        .offset(pageable.getOffset())   //N 번부터 시작
                        .limit(pageable.getPageSize()) //조회 갯수
                        .fetch();
            }

        }else{

            if(chk){

                list = queryFactory.selectFrom(qBookRentalReturnHistory)
                        .where(qBookRentalReturnHistory.member.memberId.eq(memberId).and(qBookRentalReturnHistory.returnState.eq(true)))
                        .orderBy(qBookRentalReturnHistory.returnDate.desc())
                        .offset(pageable.getOffset())   //N 번부터 시작
                        .limit(pageable.getPageSize()) //조회 갯수
                        .fetch();
            }else{

                list = queryFactory.selectFrom(qBookRentalReturnHistory)
                        .where(qBookRentalReturnHistory.member.memberId.eq(memberId).and(qBookRentalReturnHistory.returnState.eq(false)))
                        .orderBy(qBookRentalReturnHistory.returnDate.desc())
                        .offset(pageable.getOffset())   //N 번부터 시작
                        .limit(pageable.getPageSize()) //조회 갯수
                        .fetch();
            }

        }


        List<BookRentalReturnHistoryDTO> rentalReturnHistoryDTOList = this.returnBookEntityToDtoList(list);


        return rentalReturnHistoryDTOList;
    }

    @Override
    public Long rentalReturnCount(String memberId,boolean chk) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBookRentalReturnHistory qBookRentalReturnHistory = QBookRentalReturnHistory.bookRentalReturnHistory;
        Long returnCount = 0L;

        if(memberId.equals("admin")) {
            if(chk){
                returnCount = queryFactory.selectFrom(qBookRentalReturnHistory)
                        .where(qBookRentalReturnHistory.returnState.eq(true))
                        .stream().count();
            }else{
                returnCount = queryFactory.selectFrom(qBookRentalReturnHistory)
                        .where(qBookRentalReturnHistory.returnState.eq(false))
                        .stream().count();
            }

        }else{
            if(chk){
                returnCount = queryFactory.selectFrom(qBookRentalReturnHistory)
                        .where(qBookRentalReturnHistory.member.memberId.eq(memberId).and(qBookRentalReturnHistory.returnState.eq(true)))
                        .stream().count();
            }else{
                returnCount = queryFactory.selectFrom(qBookRentalReturnHistory)
                        .where(qBookRentalReturnHistory.member.memberId.eq(memberId).and(qBookRentalReturnHistory.returnState.eq(false)))
                        .stream().count();

            }

        }


        return returnCount;
    }

    @Override
    public void rentalBookComplete(String bookName, String memberId) {

        bookRentalReturnRepository.returnComplete(bookName,memberId);

    }
}
