package com.example.ex5.service;

import com.example.ex5.dto.BoardDTO;
import com.example.ex5.dto.PageRequestDTO;
import com.example.ex5.dto.PageResultDTO;
import com.example.ex5.entity.Board;
import com.example.ex5.entity.Member;
import com.example.ex5.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
  private final BoardRepository boardRepository;

  @Override
  public Long register(BoardDTO boardDTO) {
    return boardRepository.save(dtoToEntity(boardDTO)).getBno();
  }

  @Override
  public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
    Page<Object[]> result = boardRepository.getBoardWithReplyCount(
        pageRequestDTO.getPageable(Sort.by("bno").descending()));
    Function<Object[],BoardDTO> fn = objects -> entityToDto(
        (Board) objects[0],
        (Member) objects[1],
        (Long) objects[2]);
    return new PageResultDTO<>(result,fn);
  }
}
