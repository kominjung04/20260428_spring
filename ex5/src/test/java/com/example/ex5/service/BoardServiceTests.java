package com.example.ex5.service;

import com.example.ex5.dto.BoardDTO;
import com.example.ex5.dto.PageRequestDTO;
import com.example.ex5.dto.PageResultDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardServiceTests {
  @Autowired
  BoardService boardService;

  @Test
  void testRegister() {
    Long bno = boardService.register(BoardDTO.builder()
        .title("title")
        .content("content")
        .writerEmail("user100@a.a")
        .build());
    System.out.println(bno.toString());
  }

  @Test
  void testGetList() {
    PageRequestDTO pageRequestDTO = new PageRequestDTO();
    PageResultDTO<BoardDTO,Object[]> pageResultDTO = boardService.getList(pageRequestDTO);

    pageResultDTO.getDtoList().forEach(System.out::println);
  }

  @Test
  void testGetBoardByBno() {
    Long bno = 100L;
    BoardDTO boardDTO = boardService.get(bno);
    System.out.println(boardDTO);
  }

  @Test
  public void testDeleteByBno() {
    boardService.removeWithReplies(1l);
  }

  @Test
  void testModify() {
    BoardDTO boardDTO = BoardDTO.builder()
        .bno(3L)
        .title("Updated")
        .content("Updated")
        .build();
    boardService.modify(boardDTO);
  }
}