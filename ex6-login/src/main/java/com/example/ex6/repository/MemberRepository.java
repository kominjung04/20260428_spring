package com.example.ex6.repository;

import com.example.ex6.dto.MemberDTO;
import com.example.ex6.entity.Member;
import com.example.ex6.repository.search.SearchMemberRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>,
    SearchMemberRepository {
  @Query("select m from Member m where m.email =:email")
  Optional<Member> findMemberByEmail(@Param("email") String email);
}
