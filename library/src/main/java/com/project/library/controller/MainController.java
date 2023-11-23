package com.project.library.controller;


import com.project.library.dto.*;
import com.project.library.entity.QStudyRoomHistory;
import com.project.library.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class MainController {


    private final StudyRoomService studyRoomService;
    private final MemberService memberService;

    private final BookHopeService bookHopeService;

    private final BookRentalService bookRentalService;

    private final BookLikeService bookLikeService;

    private final BookRentalReturnService bookRentalReturnService;

    private final PagingService pagingService;

    @GetMapping("/")
    public String mainPage(Principal principal,Model model){


        if (principal == null || principal.getName() == null) {
            model.addAttribute("loginChk", "false");
        } else {
            // principal 객체가 비어있지 않다면, 원하는 작업을 수행합니다.
            if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().contains("ADMIN")) {
                model.addAttribute("adminChk", "true");
            }
            model.addAttribute("loginChk", "true");
        }

        return "index";

    }

    @GetMapping("/adminPage")
    public String adminPage(Model model){

        PageRequestDTO pageRequestDTO = new PageRequestDTO(1, 5);

        List<BookRentalReturnHistoryDTO> completeReturn = bookRentalReturnService.rentalReturnHistory("admin", pageRequestDTO, true);
        Long returnCompleteBookCount = bookRentalReturnService.rentalReturnCount("admin", true);
        PageResponseDTO pageResponseDTO = pagingService.paging(pageRequestDTO, Math.toIntExact(returnCompleteBookCount));


        List<BookRentalReturnHistoryDTO> waitReturn = bookRentalReturnService.rentalReturnHistory("admin", pageRequestDTO, false);
        Long returnWaitBookCount2 = bookRentalReturnService.rentalReturnCount("admin", false);
        PageResponseDTO pageResponseDTO2 = pagingService.paging(pageRequestDTO, Math.toIntExact(returnWaitBookCount2));


        PageRequestDTO pageRequestDTO2 = new PageRequestDTO(1, 15);

        List<BookHopeHistoryDTO> bookHopeHistoryDTOS = bookHopeService.bookHopeHistory("admin", pageRequestDTO2);
        Long HopeBookCount = bookHopeService.bookHopeCount("admin");

        PageResponseDTO pageResponseDTO3 = pagingService.paging(pageRequestDTO2, Math.toIntExact(HopeBookCount));

        model.addAttribute("returnBookDto", completeReturn);
        model.addAttribute("responseDTO", pageResponseDTO);

        model.addAttribute("returnBookDto2", waitReturn);
        model.addAttribute("responseDTO2", pageResponseDTO2);


        model.addAttribute("hopeBookDto", bookHopeHistoryDTOS);
        model.addAttribute("responseDTO3", pageResponseDTO3);


        return "myPage/adminPage";
    }

    @GetMapping("/rentalBook")
    public String rentalBook(Principal principal, Model model){


        if (principal == null || principal.getName() == null) {
            model.addAttribute("loginChk", "false");
        } else {
            // principal 객체가 비어있지 않다면, 원하는 작업을 수행합니다.
            model.addAttribute("loginChk", "true");
        }


        return "book/rentalBook";

    }

    @GetMapping("/rentalStudyRoom")
    public String rentalStudyRoom(Principal principal, Model model){

        log.info("학습실 이용 페이지 접근");

        //현재 사용중인 좌석 정보 가져가야됨
        List<StudyRoomStateDTO> list = studyRoomService.chkSeat();


        if (list != null) {
            model.addAttribute("seatList", list);
            log.info("글 크기 : " + list.get(0).getSeatNum().length());

        } else {
            model.addAttribute("seatList", "0");
        }


        if (principal == null || principal.getName() == null) {
            model.addAttribute("loginChk", "false");
        } else {

            model.addAttribute("loginChk", "true");
            model.addAttribute("memberId", principal.getName());
        }


        return "study/rentalStudyRoom";

    }


    @GetMapping("/myPage")
    public String MyPage(Principal principal,Model model){

        PageRequestDTO pageRequestDTO = new PageRequestDTO(1, 5);
        //내 정보
        MemberDTO memberDTO = memberService.myPageInfo(principal.getName());

        //도서 대여 정보
        List<BookRentalHistoryDTO> bookRentalHistoryDTOList = bookRentalService.rentalBookHistory(principal.getName());


        //도서 반납 내역 정보 - 반납 확인 중
        List<BookRentalReturnHistoryDTO> bookRentalReturnHistoryDTOS = bookRentalReturnService.rentalReturnHistory(principal.getName(), pageRequestDTO, false);
        Long returnHistoryCount = bookRentalReturnService.rentalReturnCount(principal.getName(), false);
        PageResponseDTO returnBookPage = pagingService.paging(pageRequestDTO, Math.toIntExact(returnHistoryCount));


        //도서 반납 내역 정보 - 반납완료
        List<BookRentalReturnHistoryDTO> bookRentalReturnCompleteHistoryDTOS = bookRentalReturnService.rentalReturnHistory(principal.getName(), pageRequestDTO, true);
        Long returnCompleteHistoryCount = bookRentalReturnService.rentalReturnCount(principal.getName(), true);
        PageResponseDTO returnCompleteBookPage = pagingService.paging(pageRequestDTO, Math.toIntExact(returnCompleteHistoryCount));


        PageRequestDTO pageRequestDTO2 = new PageRequestDTO(1, 8);
        //희망 도서 신청 정보
        List<BookHopeHistoryDTO> bookHopeHistoryDTOS = bookHopeService.bookHopeHistory(principal.getName(), pageRequestDTO2);
        Long bookHopeCount = bookHopeService.bookHopeCount(principal.getName());
        PageResponseDTO hopeBookPage = pagingService.paging(pageRequestDTO2, Math.toIntExact(bookHopeCount));


        // 관심 도서 정보
        List<BookLikeHistoryDTO> bookLikeHistoryDTOS = bookLikeService.BookLikeHistory(principal.getName(), pageRequestDTO);
        Long bookLikeCount = bookLikeService.BookLikeCount(principal.getName());
        PageResponseDTO likeBookPage = pagingService.paging(pageRequestDTO, Math.toIntExact(bookLikeCount));


        //학습실 이용 내역
        List<StudyRoomHistoryDTO> studyRoomHistoryDTOS = studyRoomService.studyRoomHistory(principal.getName(), pageRequestDTO);
        Long studyRoomHistoryCount = studyRoomService.studyRoomHistoryCount(principal.getName());
        PageResponseDTO studyRoomPage = pagingService.paging(pageRequestDTO, Math.toIntExact(studyRoomHistoryCount));


        StudyRoomStateDTO studyRoomUseChk = studyRoomService.studyRoomUseCheck(principal.getName());


        model.addAttribute("memberDto", memberDTO);
        model.addAttribute("rentalDto", bookRentalHistoryDTOList);

        model.addAttribute("likeDto", bookLikeHistoryDTOS);
        model.addAttribute("likeBookPage", likeBookPage);

        model.addAttribute("hopeDto", bookHopeHistoryDTOS);
        model.addAttribute("hopeBookPage", hopeBookPage);

        model.addAttribute("studyDto", studyRoomHistoryDTOS);
        model.addAttribute("studyRoomPage", studyRoomPage);


        model.addAttribute("returnDto", bookRentalReturnHistoryDTOS);
        model.addAttribute("returnBookPage", returnBookPage);

        model.addAttribute("returnCompleteDto", bookRentalReturnCompleteHistoryDTOS);
        model.addAttribute("returnCompleteBookPage", returnCompleteBookPage);


        if (studyRoomUseChk != null) {
            model.addAttribute("studyRoomUseChk", studyRoomUseChk);
        } else {
            model.addAttribute("studyRoomUseChk", null);
        }


        return "myPage/myPage";
    }

    @GetMapping("/hopeBook")
    public String hopeBook(Principal principal,Model model){

        log.info("도서 신청 페이지 접근");

        if (principal == null || principal.getName() == null) {
            model.addAttribute("loginChk", "false");
        } else {
            // principal 객체가 비어있지 않다면, 원하는 작업을 수행합니다.
            model.addAttribute("loginChk", "true");
        }


        return "book/hopeBook";

    }


}



