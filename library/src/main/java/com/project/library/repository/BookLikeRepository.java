package com.project.library.repository;

import com.project.library.entity.BookLikeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;


public interface BookLikeRepository extends JpaRepository<BookLikeHistory,Long>, QuerydslPredicateExecutor<BookLikeHistory> {
}
