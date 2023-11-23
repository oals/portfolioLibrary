package com.project.library.entity;


import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StudyRoomHistory {

    @javax.persistence.Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long Id;

    private String seatNum;  //좌석 번호

    private String historySeatStartDate; // 이용 시작 시간
    private String historySeatEndDate; //이용 끝 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //사용자 정보




}
