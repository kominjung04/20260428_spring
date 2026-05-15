package com.example.ex5.service;

import com.example.ex5.dto.ReplyDTO;
import com.example.ex5.entity.Board;
import com.example.ex5.entity.Reply;
import com.example.ex5.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
  private final ReplyRepository replyRepository;

  @Override
  public Long register(ReplyDTO replyDTO) {
    return replyRepository.save(dtoToEntity(replyDTO)).getRno();
  }

  @Override
  public List<ReplyDTO> getList(Long bno) {
    List<Reply> result = replyRepository.getRepliesByBoardOrderByRno(
        Board.builder().bno(bno).build()
    );
    return result.stream().map(reply -> entityToDto(reply)).collect(Collectors.toList());
  }

  @Override
  public Long modify(ReplyDTO replyDTO) {
    Optional<Reply> result = replyRepository.findById(replyDTO.getRno());
    if(result.isPresent()) {
      Reply reply = result.get();
      reply.changeText(replyDTO.getText());
      replyRepository.save(reply);
    }
    return replyDTO.getRno();
  }

  @Override
  public Long remove(Long rno) {
    try {
      replyRepository.deleteById(rno);
    } catch (Exception e) {
      return null;
    }
    return rno;
  }
}