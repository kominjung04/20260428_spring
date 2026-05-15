package com.example.ex5.repository;

import com.example.ex5.entity.Board;
import com.example.ex5.repository.search.SearchBoardRepository;
import com.example.ex5.repository.search.SearchBoardRepositoryImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {
  //   JPQL  :: JPA(Java Persistence API)+SQL 에서 사용하는 객체지향 쿼리 언어
  //   select b, r from Board b left join Reply r on r.board = b where b.bno=100;

  //   SQL :: RDBMS에서 사용하는 구조화된 쿼리 언어
  //   select b.*, r.* from db7.board b, db7.reply r where b.bno = r.board_bno and b.bno =100;

  //@ManyToOne 연관관계가 있을 경우(join)
  @Query("select b,w from Board b left join b.writer w where b.bno=:bno")
  Object getBoardWithWriter(Long bno);

  //@ManyToOne 연관관계가 없다고 가정한 경우 on을 사용
  @Query("select b,m from Board b left join Member m on b.writer = m where b.bno=:bno")
  Object getBoardWithWriter2(Long bno);

  //@ManyToOne 연관관계가 없는 경우 on을 사용
  @Query("select b, r from Board b left join Reply r on r.board=b where b.bno= :bno")
  List<Object[]> getBoardWithReply(Long bno);

  //@ManyToOne 연관관계가 있을 경우
  @Query("select b, r from Reply r left join r.board b where b.bno= :bno")
  List<Object[]> getBoardWithReply2(Long bno);

  //페이지 목록 :: board, member, 댓글 개수
  @Query(
      value = "select b,w, count(r)" +
          " from Board b left join b.writer w left join Reply r on r.board = b " +
          "group by b ",
      countQuery = "select count(b) from Board b ")
  Page<Object[]> getBoardWithReplyCount(Pageable pageable);

  //조회 화면
  @Query(
      value = "select b,w, count(r)" +
          " from Board b left join b.writer w left join Reply r on r.board = b " +
          "where b.bno = :bno ")
  Object getBoardByBno(Long bno);//Object 배열로 값을 받음(3개의 값)


}

