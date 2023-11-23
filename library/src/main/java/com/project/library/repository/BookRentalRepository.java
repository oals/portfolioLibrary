package com.project.library.repository;

import com.project.library.entity.BookLikeHistory;
import com.project.library.entity.BookRentalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface BookRentalRepository extends JpaRepository<BookRentalHistory,Long>, QuerydslPredicateExecutor<BookRentalHistory> {


    @Modifying
    @Query("delete from BookRentalHistory b where b.member.memberId = :memberId and b.bookInfo.bookName = :bookName")
    void returnRentalState(@Param("memberId") String memberId,@Param("bookName") String bookName);


}
