package com.project.library.service;

import com.project.library.dto.BookHopeHistoryDTO;
import com.project.library.dto.BookInfoDTO;
import com.project.library.dto.PageRequestDTO;
import com.project.library.entity.*;
import com.project.library.repository.BookHopeRepository;
import com.project.library.repository.BookInfoRepository;
import com.project.library.repository.MemberRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.bytebuddy.implementation.bytecode.Throw;
import org.modelmapper.ModelMapper;
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
public class BookHopeServiceImpl  implements BookHopeService{

    @PersistenceContext
    EntityManager em;

    private final BookHopeRepository bookHopeRepository;
    private final MemberRepository memberRepository;

    private final BookInfoRepository bookInfoRepository;

    private final ModelMapper modelMapper;

    @Override
    public boolean memberHopeBook(BookInfoDTO bookInfoDTO, String memberId) {


        boolean chk = false;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBookHopeHistory qBookHopeHistory = QBookHopeHistory.bookHopeHistory;

        //도서 명과 사용자명으로 이미 신청 도서에 넣었는지 체크
        Boolean hopeChk =  queryFactory.selectFrom(qBookHopeHistory)
                .where(qBookHopeHistory.member.memberId.eq(memberId).and(qBookHopeHistory.bookInfo.bookName.eq(bookInfoDTO.getBookName())))
                .fetchOne() == null;

        if(hopeChk == true){


            BookInfo bookInfo = modelMapper.map(bookInfoDTO,BookInfo.class);

            if(!bookInfoRepository.findById(bookInfoDTO.getBookName()).isPresent()){
                bookInfoRepository.save(bookInfo);
            }


            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String hopeDate = now.format(formatter);

            Member member = memberRepository.findById(memberId).orElseThrow();

            BookHopeHistory bookHopeHistory = BookHopeHistory.builder()
                    .bookInfo(bookInfo)
                    .hopeState(false)
                    .hopeDate(hopeDate)
                    .member(member)
                    .build();

            bookHopeRepository.save(bookHopeHistory);


            chk = true;
        }else{

            
            chk = false;
        }


        return chk;
    }

    @Override
    public List<BookHopeHistoryDTO> bookHopeHistory(String memberId, PageRequestDTO pageRequestDTO) {

        Pageable pageable =pageRequestDTO.getPageable();


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBookHopeHistory qBookHopeHistory = QBookHopeHistory.bookHopeHistory;

        List<BookHopeHistory> list = null;

        if(memberId.equals("admin")){
            list = queryFactory.selectFrom(qBookHopeHistory)
                    .orderBy(qBookHopeHistory.hopeDate.desc())
                    .offset(pageable.getOffset())   //N 번부터 시작
                    .limit(pageable.getPageSize()) //조회 갯수
                    .fetch();
            
        }else{
            list = queryFactory.selectFrom(qBookHopeHistory)
                    .where(qBookHopeHistory.member.memberId.eq(memberId))
                    .orderBy(qBookHopeHistory.hopeDate.desc())
                    .offset(pageable.getOffset())   //N 번부터 시작
                    .limit(pageable.getPageSize()) //조회 갯수
                    .fetch();
        }




        List<BookHopeHistoryDTO> bookHopeHistoryDTOS = this.bookHopeEntityToDto(list);


        return bookHopeHistoryDTOS;
    }

    @Override
    public Long bookHopeCount(String memberId) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBookHopeHistory qBookHopeHistory = QBookHopeHistory.bookHopeHistory;

        Long count = 0L;

        if(memberId.equals("admin")) {
            count = queryFactory.selectFrom(qBookHopeHistory)
                    .fetch().stream().count();
        }else{
            count = queryFactory.selectFrom(qBookHopeHistory)
                    .where(qBookHopeHistory.member.memberId.eq(memberId))
                    .fetch().stream().count();
        }

        return count;
    }

    @Override
    public Long bookHopeDel(String memberId, String bookName) {


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBookHopeHistory qBookHopeHistory = QBookHopeHistory.bookHopeHistory;


        queryFactory.delete(qBookHopeHistory)
                .where(qBookHopeHistory.bookInfo.bookName.eq(bookName)
                        .and(qBookHopeHistory.member.memberId.eq(memberId))).execute();



        Long count = queryFactory.selectFrom(qBookHopeHistory)
                .where(qBookHopeHistory.member.memberId.eq(memberId)).stream().count();


        return count;
    }


}
