package com.example.ex6.repository;

import com.example.ex6.entity.Member;
import com.example.ex6.entity.MovieImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
  @Modifying
  @Query("delete from m_member m where m.mid=:mid ")
  void deleteMemberByUid(@Param("mid") Long mid);

  @Modifying
  @Query("select m from m_member m where m.member.mid=:mid ")
  List<Member> findByMid(@Param("mid") Long mid);

 
}
