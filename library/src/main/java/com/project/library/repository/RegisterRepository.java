package com.project.library.repository;

import com.project.library.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface RegisterRepository extends JpaRepository<Member,String>, QuerydslPredicateExecutor<Member> {
}
