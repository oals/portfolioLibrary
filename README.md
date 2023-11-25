
# PortfolioLibrary
공공 도서관 웹 사이트 개인 프로젝트 포트폴리오

# 소개
 도서 대여 및 희망 도서 신청을 할 수 있고 학습실을 이용 할 수 있는 웹 사이트

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


# 핵심 기능 및 페이지 소개


<h3>회원가입&로그인</h3>
<br>

<img src='https://github.com/oals/portfolioLibrary/assets/136543676/b744b275-b230-42f8-bed3-12146afeae3c'>


<ul>
 <li>중복 아이디 검사 기능</li>
 <li>모든 항목의 입력 체크</li>
 <li>정규 표현식을 통한 잘못된 입력 방지</li>
 <li>주소 API를 통해 우편번호와 도로명 주소 가져오기</li>
</ul>

<br>
<br>


<h3>도서 대여</h3>
<br>

<img src='https://github.com/oals/portfolioLibrary/assets/136543676/8d5fe7df-2af2-4951-baa6-4789398d8e5f'>

<br>
<br>
<img src='https://github.com/oals/portfolioLibrary/assets/136543676/da4eb3f9-9875-40e5-b0e6-89dae5f50731' width = '150px'>

<ul>
 <li>(위 소스코드) if문으로 호출할 url을 조정하여 도서명,저자,출판사 분류별로 검색 할 수 있도록 구현 했습니다.</li>
 <li>최대 8권의 도서 대여 .</li>
 <li>이미 대여중인 도서 일 경우 오류 메세지 출력 </li>
 <li>도서 반납 -> 반납 신청 목록 -> 관리자 검토 -> 반납 완료</li>
</ul>

<br>
<br>


<h3>관심 대여</h3>
<br>

<img src='https://github.com/oals/portfolioLibrary/assets/136543676/371370a7-25c4-40bf-8add-3a0db924fd96'>


<ul>
 <li>이미 관심 도서목록에 있는 도서 일 경우 오류 메세지 출력</li>
 <li>마이페이지의 관심 도서 목록에서 도서 대여 및 관심 도서 목록에서 지우기 기능 지원</li>

</ul>

<br>
<br>






<h3>학습실</h3>
<br>

<img src='https://github.com/oals/portfolioLibrary/assets/136543676/1dfc2af5-9e24-4c2d-9b61-1b6efc842a31'>

<ul>
 <li>1인 1좌석 체크</li>
  <li>스레드를 통해 1분 간격으로 이용시간 업데이트</li>
 <li>이용시간 연장 기능</li>
 <li>학습실 이용 내역 기록</li>
</ul>

<br>
<br>



<h3>희망 도서 신청</h3>
<br>

<img src='https://github.com/oals/portfolioLibrary/assets/136543676/f557ca5d-efaf-48b3-bc2f-5b9e7e10c584'>


<ul>
 <li>희망 도서 신청 -> 관리자 검토</li>

</ul>

<br>
<br>




<h3>관리자 페이지</h3>
<br>

<img src='https://github.com/oals/portfolioLibrary/assets/136543676/5052951d-1189-40dc-b947-cf0ff23d8ce8'>





<ul>
 <li>반납 신청된 도서들의 반납 완료 </li>
 <li>희망 도서 검토 </li>

</ul>

<br>
<br>



