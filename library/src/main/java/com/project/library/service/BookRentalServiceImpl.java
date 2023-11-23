package com.project.library.service;

import com.project.library.dto.*;
import com.project.library.entity.*;
import com.project.library.repository.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class BookRentalServiceImpl implements BookRentalService{

    @PersistenceContext
    EntityManager em;

    private final BookInfoRepository bookInfoRepository;

    private final BookLikeRepository bookLikeRepository;
    private final BookRentalRepository bookRentalRepository;

    private final ModelMapper modelMapper;

    private final BookRentalReturnRepository bookRentalReturnRepository;

    private final MemberRepository memberRepository;


    @Override
    public boolean likeBook(BookInfoDTO bookInfoDTO, String memberId) {

        boolean returnChk = false;

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBookLikeHistory qBookLikeHistory = QBookLikeHistory.bookLikeHistory;
        
        //도서 명과 사용자명으로 이미 관심 도서에 넣었는지 체크
         Boolean likeChk =  queryFactory.selectFrom(qBookLikeHistory)
                .where(qBookLikeHistory.member.memberId.eq(memberId).and(qBookLikeHistory.bookInfo.bookName.eq(bookInfoDTO.getBookName())))
                 .fetchOne() == null;

        if(likeChk == true) {

            if(!bookInfoRepository.findById(bookInfoDTO.getBookName()).isPresent()){
                BookInfo bookInfo = modelMapper.map(bookInfoDTO,BookInfo.class);
                bookInfoRepository.save(bookInfo);
            }

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String today = now.format(formatter);

            BookLikeHistoryDTO bookLikeHistoryDTO = BookLikeHistoryDTO.builder()
                    .bookInfoDTO(bookInfoDTO)
                    .likeRentalState(true)
                    .likeDate(today)
                    .memberId(memberId)
                    .build();

            BookLikeHistory bookLikeHistory = this.likeBookDtoToEntity(bookLikeHistoryDTO);


            bookLikeRepository.save(bookLikeHistory);
            returnChk = true;
        }else{
            returnChk = false;
        }


        return returnChk;
    }

    @Override
    public String rentalBook(BookInfoDTO bookInfoDTO,String memberId) {

        String returnChk = "";

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBookRentalHistory qBookRentalHistory = QBookRentalHistory.bookRentalHistory;


        Boolean rentalChk =  queryFactory.selectFrom(qBookRentalHistory)
                .where(qBookRentalHistory.member.memberId.eq(memberId).and(qBookRentalHistory.bookInfo.bookName.eq(bookInfoDTO.getBookName())))
                .fetchOne() == null;

        if(rentalChk == true) {


            if(!bookInfoRepository.findById(bookInfoDTO.getBookName()).isPresent()){
                BookInfo bookInfo = modelMapper.map(bookInfoDTO,BookInfo.class);
                bookInfoRepository.save(bookInfo);
            }

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String today = now.format(formatter); // 오늘 날짜를 문자열로 변환

            LocalDateTime later = now.plusDays(30); // 30일 후 날짜를 LocalDateTime 객체로 생성
            String nextMonth = later.format(formatter); // 30일 후 날짜를 문자열로 변환


            BookRentalHistoryDTO bookRentalHistoryDTO = BookRentalHistoryDTO.builder()
                    .bookInfoDTO(bookInfoDTO)
                    .rentalState(false)
                    .rentalStartDate(today)
                    .rentalEndDate(nextMonth)
                    .memberId(memberId)
                    .build();

            BookRentalHistory bookRentalHistory = this.rentalBookDtoToEntity(bookRentalHistoryDTO);


            bookRentalRepository.save(bookRentalHistory);


            //도서 대여 횟수 기록
            Member member = memberRepository.findById(memberId).orElseThrow();
            member.setBookRentalCount(member.getBookRentalCount() + 1);
            memberRepository.save(member);


            returnChk = "대여 가능";
        }else{
            returnChk = "대여 불가";
        }


        return returnChk;




    }

    @Override
    public String rentalCountChk(String memberId) {

        String chk = "대여 불가";

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBookRentalHistory qBookRentalHistory = QBookRentalHistory.bookRentalHistory;

        //도서 명과 사용자명으로 이미 관심 도서에 넣었는지 체크
        Long rentalCount =  queryFactory.selectFrom(qBookRentalHistory)
                .where(qBookRentalHistory.member.memberId.eq(memberId))
                .stream().count();

        if(rentalCount < 8){
            chk = "대여 가능";
        }else{
            chk = "대여 불가";
        }



        return chk;
    }

    @Override
    public List<BookRentalHistoryDTO> rentalBookHistory(String memberId) {


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBookRentalHistory qBookRentalHistory = QBookRentalHistory.bookRentalHistory;

        List<BookRentalHistory> list =  queryFactory.selectFrom(qBookRentalHistory)
                .orderBy(qBookRentalHistory.rentalStartDate.desc())
                .where(qBookRentalHistory.member.memberId.eq(memberId))
                .stream().toList();


       List<BookRentalHistoryDTO> bookRentalHistoryDTOList = this.rentalBookEntityToDto(list);



        return bookRentalHistoryDTOList;
    }


}
