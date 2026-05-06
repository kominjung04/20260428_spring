package com.example.ex4.repository;

import com.example.ex4.entity.Guestbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface guestbookRepository extends JpaRepository<Guestbook,Long>,
    QuerydslPredicateExecutor<Guestbook> { //동적 질의 가능하게 하는

}
