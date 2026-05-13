package com.example.ex5.repository;

import com.example.ex5.dto.BoardDTO;
import com.example.ex5.entity.Board;
import com.example.ex5.entity.Member;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardRepositoryTests {

  @Autowired
  BoardRepository boardRepository;

  @Test
  void insertBoard() {
    IntStream.rangeClosed(1, 100).forEach(i -> {
      Board board = Board.builder()
          .title("title" + i)
          .content("content" + i)
          .writer(Member.builder().email("user"+i+"@a.a").build())
          .build();
      boardRepository.save(board);
    });
  }

  @Test
  @Transactional //Board의 writer의 관계가 lazy라서 두개의 트랜젝션을 실행
  public void testRead1() {
    Board board = boardRepository.findById(100L).orElse(null);
    System.out.println(board);
    System.out.println(board.getWriter());
    assertNotNull(board);
  }

  @Test //연관관게가 있다고 가정
  public void testGetBoardWithWriter(){
    Object result = boardRepository.getBoardWithWriter(100L);
    Object[] arr = (Object[]) result;
    System.out.println(Arrays.toString(arr));
  }

  @Test //연관관게가 없다고 가정
  public void testGetBoardWithWriter2(){
    Object result = boardRepository.getBoardWithWriter2(100L);
    Object[] arr = (Object[]) result;
    System.out.println(Arrays.toString(arr));
  }

  @Test //연관관게가 없다고 가정
  public void testGetBoardWithReply(){
    List<Object[]> result = boardRepository.getBoardWithReply(100L);
    result.stream().forEach(new Consumer<Object[]>(){
    @Override
      public void accept(Object[] obj) {
      Object[] arr = (Object[]) obj;
      System.out.println(Arrays.toString(arr));
    }
    });
  }

  @Test //연관관계가 없다고 가정
  public void testGetBoardWithReply2(){
    List<Object[]> result = boardRepository.getBoardWithReply2(100L);
    result.stream().forEach(new Consumer<Object[]>(){
      @Override
      public void accept(Object[] obj) {
        Object[] arr = (Object[]) obj;
        System.out.println(Arrays.toString(arr));
      }
    });
  }

  @Test
  public void testGetBoardWithReplyCount(){
    Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
    Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);

    result.stream().forEach((obj) -> {
      Object[] arr = (Object[]) obj;
      System.out.println(Arrays.toString(arr));
    });
  }

  @Test
  public void testGetBoardByBno(){
    Object result = boardRepository.getBoardByBno(100L);
      Object[] arr = (Object[]) result;
      System.out.println(Arrays.toString(arr));
  }


  }
