package com.project.library.repository;

import com.project.library.entity.BookHopeHistory;
import com.project.library.entity.BookLikeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BookHopeRepository extends JpaRepository<BookHopeHistory,Long>, QuerydslPredicateExecutor<BookHopeHistory> {
}
