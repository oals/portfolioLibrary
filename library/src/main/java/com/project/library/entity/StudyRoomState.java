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
public class StudyRoomState {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="room_id")
    private Long Id;

    private String seatNum;  //좌석 번호

    private String seatStartDate; // 이용 시작 시간

    private String seatCountTime; //남은 이용 시간


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;  //사용자 정보


}
