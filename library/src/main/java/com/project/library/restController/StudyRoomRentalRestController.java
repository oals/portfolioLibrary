package com.project.library.restController;

import com.project.library.dto.PageRequestDTO;
import com.project.library.dto.StudyRoomHistoryDTO;
import com.project.library.service.StudyRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
public class StudyRoomRentalRestController {

    private final StudyRoomService studyRoomService;

    @PostMapping("/rentalStudyRoom")
    public boolean rentalStudyRoom(String seatNum, Principal principal){



        boolean returnChk = studyRoomService.RentalStudyRoom(seatNum,principal.getName());


        return returnChk;
    }

    @Transactional
    @PutMapping("/plusRentalTime")
    public void plusRentalTime(String seatNum){


        studyRoomService.plusRentalTimeStudyRoom(seatNum);


    }

    @Transactional
    @DeleteMapping("/movingOut")
    public void movingOut(Principal principal){

        studyRoomService.movingOutStudyRoom(principal.getName());


    }

    @GetMapping("/studyRoomHistory")
    public List<StudyRoomHistoryDTO> studyRoomHistory(Principal principal, PageRequestDTO pageRequestDTO){


       List<StudyRoomHistoryDTO> studyRoomHistoryDTOS = studyRoomService.studyRoomHistory(principal.getName(),pageRequestDTO);


        return studyRoomHistoryDTOS;
    }



}
