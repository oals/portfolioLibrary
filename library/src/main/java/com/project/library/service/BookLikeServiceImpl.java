package com.project.library.service;

import com.project.library.dto.BookInfoDTO;
import com.project.library.dto.BookLikeHistoryDTO;
import com.project.library.dto.BookRentalHistoryDTO;
import com.project.library.dto.PageRequestDTO;
import com.project.library.entity.BookLikeHistory;
import com.project.library.entity.BookRentalHistory;
import com.project.library.entity.QBookLikeHistory;
import com.project.library.entity.QBookRentalHistory;
import com.project.library.repository.BookRentalRepository;
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
@RequiredArgsConstructor
@Log4j2
public class BookLikeServiceImpl implements BookLikeService{

    @PersistenceContext
    EntityManager em;

    private final BookRentalRepository bookRentalRepository;


    @Override
    public List<BookLikeHistoryDTO> BookLikeHistory(String memberId, PageRequestDTO pageRequestDTO) {


        Pageable pageable =pageRequestDTO.getPageable();


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBookLikeHistory qBookLikeHistory = QBookLikeHistory.bookLikeHistory;

        List<BookLikeHistory> list =  queryFactory.selectFrom(qBookLikeHistory)
                .where(qBookLikeHistory.member.memberId.eq(memberId)).limit(5)
                .orderBy(qBookLikeHistory.likeDate.desc())
                .offset(pageable.getOffset())   //N 번부터 시작
                .limit(pageable.getPageSize()) //조회 갯수
                .fetch();


        List<BookLikeHistoryDTO> bookLikeHistoryDTOS = this.BookLikeEntityToDto(list);



        return bookLikeHistoryDTOS;
    }



    @Override
    public Long BookLikeCount(String memberId) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBookLikeHistory qBookLikeHistory = QBookLikeHistory.bookLikeHistory;

        Long count = queryFactory.selectFrom(qBookLikeHistory)
                .where(qBookLikeHistory.member.memberId.eq(memberId)).stream().count();


        return count;
    }

    @Override
    public Long LikeBookDel(String memberId, String bookName) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBookLikeHistory qBookLikeHistory = QBookLikeHistory.bookLikeHistory;

        queryFactory.delete(qBookLikeHistory)
                .where(qBookLikeHistory.member.memberId.eq(memberId)
                        .and(qBookLikeHistory.bookInfo.bookName.eq(bookName))).execute();



       Long count = queryFactory.selectFrom(qBookLikeHistory)
                .where(qBookLikeHistory.member.memberId.eq(memberId)).stream().count();


        return count;
    }


}
