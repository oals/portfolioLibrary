package com.project.library.repository;

import com.project.library.entity.StudyRoomHistory;
import com.project.library.entity.StudyRoomState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface StudyRoomHistoryRepository  extends JpaRepository<StudyRoomHistory,Long>, QuerydslPredicateExecutor<StudyRoomHistory> {
}
