package com.project.library.repository;

import com.project.library.entity.StudyRoomState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface StudyRoomStateRepository extends JpaRepository<StudyRoomState,Long>, QuerydslPredicateExecutor<StudyRoomState> {


    @Modifying
    @Query("delete from StudyRoomState s where s.member.memberId = :memberId")
    void movingOut(@Param("memberId") String memberId);




    @Modifying
    @Query("update StudyRoomState s SET s.seatCountTime = '04:00' where s.seatNum = :seatNum")
    void plusTime(@Param("seatNum") String seatNum);


}
