package com.project.library.service;

import com.project.library.dto.BookLikeHistoryDTO;
import com.project.library.dto.PageRequestDTO;
import com.project.library.dto.StudyRoomHistoryDTO;
import com.project.library.dto.StudyRoomStateDTO;
import com.project.library.entity.BookLikeHistory;
import com.project.library.entity.Member;
import com.project.library.entity.StudyRoomHistory;
import com.project.library.entity.StudyRoomState;

import java.util.ArrayList;
import java.util.List;

public interface StudyRoomService {

    default StudyRoomState StudyRoomStateDtoToEntity (StudyRoomStateDTO studyRoomStateDTO){


        Member member = Member.builder()
                .memberId(studyRoomStateDTO.getMemberId())
                .build();


        StudyRoomState studyRoomState = StudyRoomState.builder()
                .seatNum(studyRoomStateDTO.getSeatNum())
                .seatCountTime(studyRoomStateDTO.getSeatCountTime())
                .seatStartDate(studyRoomStateDTO.getSeatStartDate())
                .member(member)
                .build();


        return studyRoomState;
    }


    default StudyRoomStateDTO StudyRoomStateEntityToDto (StudyRoomState studyRoomState){



        StudyRoomStateDTO studyRoomStateDTO = StudyRoomStateDTO.builder()
                .seatNum(studyRoomState.getSeatNum())
                .seatCountTime(studyRoomState.getSeatCountTime())
                .memberId(studyRoomState.getMember().getMemberId())
                .seatStartDate(studyRoomState.getSeatStartDate())
                .build();


        return studyRoomStateDTO;
    }

    default List<StudyRoomHistoryDTO> studyRoomHistoryEntityToDto (List<StudyRoomHistory> studyRoomHistories){

        List<StudyRoomHistoryDTO> studyRoomHistoryDTOS = new ArrayList<>();

        for(int i = 0; i < studyRoomHistories.size(); i ++) {
            StudyRoomHistoryDTO studyRoomHistoryDTO = StudyRoomHistoryDTO.builder()
                    .seatNum(studyRoomHistories.get(i).getSeatNum())
                    .historySeatStartDate(studyRoomHistories.get(i).getHistorySeatStartDate())
                    .historySeatEndDate(studyRoomHistories.get(i).getHistorySeatEndDate())
                    .memberId(studyRoomHistories.get(i).getMember().getMemberId())
                    .build();

            studyRoomHistoryDTOS.add(studyRoomHistoryDTO);
        }

        return studyRoomHistoryDTOS;
    }



    public boolean RentalStudyRoom(String seatNum,String memberId);

    public List<StudyRoomStateDTO> chkSeat();

    public void movingOutStudyRoom(String memberId);

    public void plusRentalTimeStudyRoom(String seatNum);


    public List<StudyRoomHistoryDTO> studyRoomHistory(String memberId, PageRequestDTO pageRequestDTO);

    public Long studyRoomHistoryCount(String memberId);

    public StudyRoomStateDTO studyRoomUseCheck(String memberId);
}
