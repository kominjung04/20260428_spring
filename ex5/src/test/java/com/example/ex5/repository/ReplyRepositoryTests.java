package com.example.ex5.repository;

import com.example.ex5.entity.Board;
import com.example.ex5.entity.Member;
import com.example.ex5.entity.Reply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReplyRepositoryTests {
  @Autowired
   ReplyRepository replyRepository;

  @Test
  void insertReply() {
    IntStream.rangeClosed(1, 300).forEach(i -> {
      long bno = (long)(Math.random()*100)+ 1;
      long mid = (long)(Math.random()*100)+ 1;
      Reply reply = Reply.builder()
          .text("Reply" + i)
          .board(Board.builder().bno(bno).build())
          .commenter(Member.builder().email("user"+mid+"@a.a").build())
          .build();
      replyRepository.save(reply);
    });
  }

  @Test
  void testListByBoard() {
    List<Reply> replies = replyRepository.findAll();

  }
}