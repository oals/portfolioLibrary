package com.project.library.dto;


import com.project.library.entity.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyRoomHistoryDTO {

    private Long Id;

    private String seatNum;  //좌석 번호

    private String historySeatStartDate; // 이용 시작 시간
    private String historySeatEndDate; //이용 끝 시간

    private String memberId;




}
