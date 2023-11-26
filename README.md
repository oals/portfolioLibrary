
# PortfolioLibrary
공공 도서관 웹 사이트 개인 프로젝트 포트폴리오

# 소개
 알라딘 도서 API를 통해 도서 검색 및 대여, 희망 도서 신청을 할 수 있고 자유 학습실 좌석을 선택해 이용 할 수 있는 웹 사이트

# 제작기간 & 참여 인원
<UL>
  <LI>2023.11.03 ~ 2023.11.23</LI>
  <LI>개인 프로젝트</LI>
</UL>


# 사용기술
![js](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white)
![js](https://img.shields.io/badge/Java-FF0000?style=for-the-badge&logo=Java&logoColor=white)
![js](https://img.shields.io/badge/IntelliJ-004088?style=for-the-badge&logo=IntelliJ&logoColor=white)
![js](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=MariaDB&logoColor=white)
![js](https://img.shields.io/badge/security-6DB33F?style=for-the-badge&logo=security&logoColor=white)

![js](https://img.shields.io/badge/jquery-0769AD?style=for-the-badge&logo=jquery&logoColor=white)
![js](https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)
![js](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=white)

# E-R 다이어그램

<img src='https://github.com/oals/portfolioLibrary/assets/136543676/ffaa8bf6-e976-4a16-891b-d0d43e4bd59b'>


# 핵심 기능 및 페이지 소개


<h3>회원가입&로그인</h3>
<br>

<img src='https://github.com/oals/portfolioLibrary/assets/136543676/17e39a43-ccb3-40c0-9381-239f1bac15f4'>


<br>
<br>
<details>
 <summary> 회원가입 플로우 차트
 
 </summary> 
 
<img src='https://github.com/oals/portfolioLibrary/assets/136543676/f5428db8-442f-4200-8ac6-12026a05570b'>
</details>



<br>
<br>




<h3>학습실</h3>
<br>

<img src='https://github.com/oals/portfolioLibrary/assets/136543676/1dfc2af5-9e24-4c2d-9b61-1b6efc842a31'>


<br>
<br>

<details>
 <summary> 학습실 플로우 차트
 
 </summary> 
 
<img src='https://github.com/oals/portfolioLibrary/assets/136543676/079dbb43-c836-4552-a0d0-265f49c5357c'>
</details>



<details>
 <summary> 학습실 Service 코드
 
 </summary> 



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

            StudyRoomStateDTO studyRoomStateDTO = StudyRoomStateDTO.builder()
                    .seatNum(seatNum)
                    .memberId(memberId)
                    .seatCountTime("03:00") //이용 시간
                    .seatStartDate(startTime)
                    .build();

            StudyRoomState studyRoomState = this.StudyRoomStateDtoToEntity(studyRoomStateDTO);

            //학습실 현재 사용 정보 저장
            studyRoomStateRepository.save(studyRoomState);

            //학습실 이용 횟수 업데이트
            Member member = memberRepository.findById(memberId).orElseThrow();
            member.setStudyRentalCount(member.getStudyRentalCount() + 1);
            memberRepository.save(member);

            returnChk = true;

            //현재 좌석 정보를 스레드로 전달 및 스레드 실행
            String message = seatNum;
            Thread thread = new Thread(new MyRunnable(message));
            thread.start(); // 스레드를 실행합니다.


        }else{
            returnChk = false;
        }

        return returnChk;

    }

    



</details>



<details>

 <summary> 학습실 스레드 코드
 
 </summary> 


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
                    //1분 주기로 현재 남은 이용시간 업데이트
                    Thread.sleep(60000);

                    JPAQueryFactory queryFactory = new JPAQueryFactory(em);
                    QStudyRoomState qStudyRoomState = QStudyRoomState.studyRoomState;
                    StudyRoomState studyRoomState =  queryFactory.selectFrom(qStudyRoomState)
                            .where(qStudyRoomState.seatNum.eq(seatNum))
                            .fetchOne();
                    //스레드 종료
                    if(studyRoomState == null){
                        Thread.interrupted();
                        break;
                    }
                    //남은 이용 시간 계산
                    LocalTime time = LocalTime.parse(studyRoomState.getSeatCountTime());
                    time = time.minusMinutes(1);
                    
                    //현재 남은 이용 시간이 없을 때 
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
                        //학습실 이용 정보 저장
                        StudyRoomHistoryRepository.save(studyRoomHistory);
                    }else {
                        //현재 남은 이용 시간 업데이트
                        studyRoomState.setSeatCountTime(time.toString());
                        studyRoomStateRepository.save(studyRoomState);
                    }



                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
 

</details>

<br>
<br>


<h3>도서 대여</h3>
<br>

<img src='https://github.com/oals/portfolioLibrary/assets/136543676/8d5fe7df-2af2-4951-baa6-4789398d8e5f'>

<br>
<br>
<details>
 <summary> 도서 대여 플로우 차트
 
 </summary> 
 
<img src='https://github.com/oals/portfolioLibrary/assets/136543676/60657fde-7948-4fa6-b7d2-43157bea6c37'>
</details>


<details>
 <summary> 도서 대여 Service 코드
 
 </summary> 
 


     public String rentalBook(BookInfoDTO bookInfoDTO,String memberId) {

        String returnChk = "";

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBookRentalHistory qBookRentalHistory = QBookRentalHistory.bookRentalHistory;

        //이미 대여한 도서인지 검사
        Boolean rentalChk =  queryFactory.selectFrom(qBookRentalHistory)
                .where(qBookRentalHistory.member.memberId.eq(memberId).and(qBookRentalHistory.bookInfo.bookName.eq(bookInfoDTO.getBookName())))
                .fetchOne() == null;

        if(rentalChk == true) {

            //해당 도서의 정보가 DB에 없을 경우에만 도서 정보 저장
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

            //대여 내역 저장
            bookRentalRepository.save(bookRentalHistory);


            //도서 대여 횟수 업데이트
            Member member = memberRepository.findById(memberId).orElseThrow();
            member.setBookRentalCount(member.getBookRentalCount() + 1);
            memberRepository.save(member);


            returnChk = "대여 가능";
        }else{
            returnChk = "대여 불가";
        }


        return returnChk;




    }



</details>




<br>
<br>


<h3>관심 도서</h3>
<br>

<img src='https://github.com/oals/portfolioLibrary/assets/136543676/371370a7-25c4-40bf-8add-3a0db924fd96'>

<br>
<br>

<details>
 <summary> 관심 도서 플로우 차트
 
 </summary> 
 
<img src='https://github.com/oals/portfolioLibrary/assets/136543676/689b487c-9ab0-498e-a44e-2dd1bbed9a99'>
</details>


<details>
 <summary> 관심 도서 Service 코드
 
 </summary> 
 


     public boolean likeBook(BookInfoDTO bookInfoDTO, String memberId) {

        boolean returnChk = false;

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBookLikeHistory qBookLikeHistory = QBookLikeHistory.bookLikeHistory;
        
        //도서 명과 사용자명으로 이미 관심 도서에 넣었는지 체크
         Boolean likeChk =  queryFactory.selectFrom(qBookLikeHistory)
                .where(qBookLikeHistory.member.memberId.eq(memberId).and(qBookLikeHistory.bookInfo.bookName.eq(bookInfoDTO.getBookName())))
                 .fetchOne() == null;

        if(likeChk == true) {
            
            //해당 도서의 정보가 DB에 없을 경우에만 도서 정보 저장
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

            //관심 도서 내역 테이블 저장
            bookLikeRepository.save(bookLikeHistory);
            returnChk = true;
            
        }else{
            //안내 메세지 출력
            returnChk = false;
        }


        return returnChk;
    }


    
</details>



<br>
<br>



<h3>희망 도서 신청</h3>
<br>

<img src='https://github.com/oals/portfolioLibrary/assets/136543676/f557ca5d-efaf-48b3-bc2f-5b9e7e10c584'>


<br>
<br>


<details>
 <summary> 희망 도서 신청 플로우 차트
 
 </summary> 
 
<img src='https://github.com/oals/portfolioLibrary/assets/136543676/b662b2e5-44cd-493f-a4cc-c69eaabc941b'>
</details>


<details>
 <summary> 희망 도서 Service 코드
 
 </summary> 
 

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
            //해당 도서의 정보가 DB에 없을 경우에만 도서 정보 저장
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
            //희망 도서 신청 내역 저장
            bookHopeRepository.save(bookHopeHistory);

            chk = true;
        }else{
            chk = false;
        }


        return chk;
    }






</details>



<br>
<br>




<h3>관리자 페이지</h3>
<br>

<img src='https://github.com/oals/portfolioLibrary/assets/136543676/5052951d-1189-40dc-b947-cf0ff23d8ce8'>


<br>
<br>

<details>
 <summary> 관리자 페이지 플로우 차트
 
 </summary> 
 
<img src='https://github.com/oals/portfolioLibrary/assets/136543676/d284cf07-56aa-4a47-a027-5df2278230c3'>
</details>




# 프로젝트를 통해 느낀 점과 소감

공부 하면서 API를 사용해 본 적은 있지만 웹 프로젝트에서 API를 사용 하는 것은 처음이였다. <br>
도서와 관련된 모든 처리를 API에 의지 하려다가 비동기 통신에서 흔히 발생하는 데이터 누락 문제가 생겼다 <br> 
상황은 도서 관련된 처리를 할때 DB에는 도서명만 넣어놓고 나중에 도서명을 통해 다른 데이터를 받아와 처리 하려고 했는데 <br>
for문 안의 AJAX 비동기 통신에서 데이터 누락이 발생했다. <br>
후에 데이터 누락 문제는 해결 했지만 속도가 너무 느려졌다 <br>
그래서 책 정보 테이블을 만들어 저장되지 않은 책 정보들은 저장되도록 구현했다<br>










