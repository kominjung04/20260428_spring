package com.example.ex5.service;

import com.example.ex5.dto.BoardDTO;
import com.example.ex5.dto.PageRequestDTO;
import com.example.ex5.dto.PageResultDTO;
import com.example.ex5.entity.Board;
import com.example.ex5.entity.Member;
import com.example.ex5.repository.BoardRepository;
import com.example.ex5.repository.ReplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
  private final BoardRepository boardRepository;
  private final ReplyRepository replyRepository;

  @Override
  public Long register(BoardDTO boardDTO) {
    return boardRepository.save(dtoToEntity(boardDTO)).getBno();
  }

  @Override
  public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
    // 동적 검색이 없을 경우
    Page<Object[]> result = boardRepository.getBoardWithReplyCount(
        pageRequestDTO.getPageable(Sort.by("bno").descending()));

    // 동적 검색이 있는 경우
    Page<Object[]> page = boardRepository.searchPage(
        pageRequestDTO.getType(),pageRequestDTO.getKeyword(),
        pageRequestDTO.getPageable(Sort.by("bno").descending())
    );

    Function<Object[], BoardDTO> fn = objects -> entityToDto(
        (Board) objects[0],
        (Member) objects[1],
        (Long) objects[2]);

    //return new PageResultDTO<>(result, fn); //동적 검색 무
    return new PageResultDTO<>(page, fn); //동적 검색  유
  }

  @Override
  public BoardDTO get(Long bno) {
    Object[] arr = (Object[]) boardRepository.getBoardByBno(bno);
    return entityToDto((Board) arr[0], (Member) arr[1], (Long) arr[2]);
  }

  @Transactional //트랙잰셕이 여러번 반복 행위가 존재할 때
  @Override
  public Long removeWithReplies(Long bno) {
    //삭제 할시 :: 참조한 글 삭제 후 본 글 삭제
    try {
      replyRepository.deleteByBno(bno);
      boardRepository.deleteById(bno);
    } catch (Exception e) {
      log.info("removeWithReplies 삭제 실패"); //Log4j2존재
      return null;
    }
    return bno;
  }

  @Transactional
  @Override
  public Long modify(BoardDTO boardDTO) {
    Long bno = null;
    Optional<Board> result = boardRepository.findById(boardDTO.getBno());
    if (result.isPresent()) {
      Board board = result.get();
      board.changeTitle(boardDTO.getTitle());
      board.changContent(boardDTO.getContent());
      boardRepository.save(board);
      return board.getBno();
    }
    return bno;
  }


}
