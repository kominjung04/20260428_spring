package com.example.ex5.service;

import com.example.ex5.dto.BoardDTO;
import com.example.ex5.dto.PageRequestDTO;
import com.example.ex5.dto.PageResultDTO;
import com.example.ex5.entity.Board;
import com.example.ex5.entity.Member;

public interface BoardService {
  Long register(BoardDTO boardDTO);
  PageResultDTO<BoardDTO,Object[]> getList(PageRequestDTO pageRequestDTO);

  default Board dtoToEntity(BoardDTO boardDTO) {
    Board board = Board.builder()
        .bno(boardDTO.getBno())
        .title(boardDTO.getTitle())
        .content(boardDTO.getContent())
        .writer(Member.builder().email(boardDTO.getWriterEmail()).build())
        .build();
    return board;
  }
  default BoardDTO entityToDto(Board board,Member member,Long replyCount) {
    BoardDTO boardDTO = BoardDTO.builder()
        .bno(board.getBno())
        .title(board.getTitle())
        .content(board.getContent())
        .regDate(board.getRegDate())
        .modDate(board.getModDate())
        .writerEmail(member.getEmail())
        .writerName(member.getName())
        .replyCount(replyCount.intValue()) //Long => int 형변환
        .build();
    return boardDTO;
  }
}
