package com.project.library.dto;


import com.project.library.entity.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyRoomStateDTO {

    private String seatNum;  //좌석 번호

    private String seatStartDate; // 이용 시작 시간
    private String seatCountTime; //남은 이용 시간

    private String memberId;


}
