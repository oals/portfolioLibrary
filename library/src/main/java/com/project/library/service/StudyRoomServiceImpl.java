package com.project.library.service;

import com.project.library.dto.PageRequestDTO;
import com.project.library.dto.StudyRoomHistoryDTO;
import com.project.library.dto.StudyRoomStateDTO;
import com.project.library.entity.*;
import com.project.library.repository.MemberRepository;
import com.project.library.repository.StudyRoomHistoryRepository;
import com.project.library.repository.StudyRoomStateRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class StudyRoomServiceImpl implements StudyRoomService{

    @PersistenceContext
    EntityManager em;

    private final StudyRoomStateRepository studyRoomStateRepository;
    private final StudyRoomHistoryRepository StudyRoomHistoryRepository;
    
    private final MemberRepository memberRepository;



    public class MyRunnable implements Runnable {
        private final String seatNum;
        private boolean threadChk;

        public MyRunnable(String seatNum) {
            this.seatNum = seatNum;
            this.threadChk = true;
        }

        @Override
        public void run() {
            while(threadChk) {

                try {
                    Thread.sleep(60000);

                    JPAQueryFactory queryFactory = new JPAQueryFactory(em);
                    QStudyRoomState qStudyRoomState = QStudyRoomState.studyRoomState;

                    StudyRoomState studyRoomState =  queryFactory.selectFrom(qStudyRoomState)
                            .where(qStudyRoomState.seatNum.eq(seatNum))
                            .fetchOne();

                    if(studyRoomState == null){
                        Thread.interrupted();
                        log.info("스레드 종료");
                        break;
                    }
                    log.info(studyRoomState.getSeatNum() + " 스레드 동작중");

                    LocalTime time = LocalTime.parse(studyRoomState.getSeatCountTime());
                    time = time.minusMinutes(1);

                    if(time.toString().equals("00:00")){
                        studyRoomStateRepository.deleteById(studyRoomState.getId());
                        threadChk = false;

                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
                        String endTime = now.format(formatter);

                        StudyRoomHistory studyRoomHistory = StudyRoomHistory.builder()
                                .seatNum(studyRoomState.getSeatNum())
                                .member(studyRoomState.getMember())
                                .historySeatStartDate(studyRoomState.getSeatStartDate())
                                .historySeatEndDate(endTime)
                                .build();

                        StudyRoomHistoryRepository.save(studyRoomHistory);

                    }else {

                        studyRoomState.setSeatCountTime(time.toString());
                        studyRoomStateRepository.save(studyRoomState);
                    }



                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public boolean RentalStudyRoom(String seatNum, String memberId) {


        boolean returnChk = false;

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QStudyRoomState qStudyRoomState = QStudyRoomState.studyRoomState;

        //이미 사용중인 좌석이 있는지 확인
        Boolean seatChk =  queryFactory.selectFrom(qStudyRoomState)
                .where(qStudyRoomState.member.memberId.eq(memberId))
                .fetchOne() == null;

        if(seatChk == true) {

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
            String startTime = now.format(formatter);
            log.info( "현재 시간 : " + startTime);

            StudyRoomStateDTO studyRoomStateDTO = StudyRoomStateDTO.builder()
                    .seatNum(seatNum)
                    .memberId(memberId)
                    .seatCountTime("03:00")
                    .seatStartDate(startTime)
                    .build();

            StudyRoomState studyRoomState = this.StudyRoomStateDtoToEntity(studyRoomStateDTO);

            studyRoomStateRepository.save(studyRoomState);

            
            //학습실 이용 횟수 기록
            Member member = memberRepository.findById(memberId).orElseThrow();
            member.setStudyRentalCount(member.getStudyRentalCount() + 1);
            memberRepository.save(member);
            
            
            returnChk = true;

            String message = seatNum;
            Thread thread = new Thread(new MyRunnable(message));
            thread.start(); // 스레드를 실행합니다.


        }else{
            returnChk = false;
        }

        return returnChk;

    }

    @Override
    public List<StudyRoomStateDTO> chkSeat() {

        List<StudyRoomStateDTO> studyRoomStateDTOS = null;
        List<StudyRoomState> list = studyRoomStateRepository.findAll();

        if(list.size() != 0) {
            studyRoomStateDTOS = list.stream().map(x -> this.StudyRoomStateEntityToDto(x)).collect(Collectors.toList());
        }

        return studyRoomStateDTOS;
    }

    @Override
    public void movingOutStudyRoom(String memberId) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QStudyRoomState qStudyRoomState = QStudyRoomState.studyRoomState;

        StudyRoomState studyRoomState = queryFactory.selectFrom(qStudyRoomState)
                .where(qStudyRoomState.member.memberId.eq(memberId))
                .fetchOne();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
        String endTime = now.format(formatter);

        StudyRoomHistory studyRoomHistory = StudyRoomHistory.builder()
                .seatNum(studyRoomState.getSeatNum())
                .member(studyRoomState.getMember())
                .historySeatStartDate(studyRoomState.getSeatStartDate())
                .historySeatEndDate(endTime)
                .build();

        studyRoomStateRepository.movingOut(memberId);

        StudyRoomHistoryRepository.save(studyRoomHistory);


    }

    @Override
    public void plusRentalTimeStudyRoom(String seatNum) {
        studyRoomStateRepository.plusTime(seatNum);

    }

    @Override
    public List<StudyRoomHistoryDTO> studyRoomHistory(String memberId, PageRequestDTO pageRequestDTO) {


        Pageable pageable = pageRequestDTO.getPageable();


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QStudyRoomHistory qStudyRoomHistory = QStudyRoomHistory.studyRoomHistory;

        List<StudyRoomHistory> list = queryFactory.selectFrom(qStudyRoomHistory)
                .where(qStudyRoomHistory.member.memberId.eq(memberId)).limit(5)
                .orderBy(qStudyRoomHistory.historySeatEndDate.desc())
                .offset(pageable.getOffset())   //N 번부터 시작
                .limit(pageable.getPageSize()) //조회 갯수
                .fetch();


        List<StudyRoomHistoryDTO> studyRoomHistoryEntityToDto =  this.studyRoomHistoryEntityToDto(list);

        return studyRoomHistoryEntityToDto;
    }

    @Override
    public Long studyRoomHistoryCount(String memberId) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QStudyRoomHistory qStudyRoomHistory = QStudyRoomHistory.studyRoomHistory;

        Long studyRoomCount = queryFactory.selectFrom(qStudyRoomHistory)
                .where(qStudyRoomHistory.member.memberId.eq(memberId))
                .stream().count();

        return studyRoomCount;
    }

    @Override
    public StudyRoomStateDTO studyRoomUseCheck(String memberId) {

        StudyRoomStateDTO studyRoomStateDTO = null;

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QStudyRoomState qStudyRoomState = QStudyRoomState.studyRoomState;

        Boolean seatChk =  queryFactory.selectFrom(qStudyRoomState)
                .where(qStudyRoomState.member.memberId.eq(memberId))
                .fetchOne() == null;

        if(seatChk != true){
            StudyRoomState studyRoomState =  queryFactory.selectFrom(qStudyRoomState)
                    .where(qStudyRoomState.member.memberId.eq(memberId))
                    .fetchOne();

            studyRoomStateDTO =  this.StudyRoomStateEntityToDto(studyRoomState);
        }


        return studyRoomStateDTO;
    }
}
