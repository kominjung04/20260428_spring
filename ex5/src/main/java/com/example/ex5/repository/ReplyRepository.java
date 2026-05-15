package com.example.ex5.repository;

import com.example.ex5.entity.Board;
import com.example.ex5.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
  //삭제 처리
  @Modifying // 복수의 트랜잭션으로 삭제할 때는 붙여서 사용 :: foreign key일 시 참조된 영역을 우선 삭제하여 오류 발생 막기
  @Query(value = "delete from Reply r where r.board.bno = :bno ")
  void deleteByBno(Long bno);

  // 쿼리 메서드 :: Board로 Reply 댓글 목록 가져오기
  List<Reply> getRepliesByBoardOrderByRno(Board board);

}
