package com.project.library.repository;

import com.project.library.entity.BookRentalHistory;
import com.project.library.entity.BookRentalReturnHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface BookRentalReturnRepository extends JpaRepository<BookRentalReturnHistory,Long>, QuerydslPredicateExecutor<BookRentalReturnHistory> {



    @Modifying
    @Query("update BookRentalReturnHistory b SET b.returnState = '1' where b.bookInfo.bookName = :bookName and b.member.memberId = :memberId")
    void returnComplete(@Param("bookName") String bookName,@Param("memberId") String memberId);






}
