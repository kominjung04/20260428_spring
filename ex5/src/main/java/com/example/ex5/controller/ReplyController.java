package com.example.ex5.controller;

import com.example.ex5.dto.ReplyDTO;
import com.example.ex5.entity.Reply;
import com.example.ex5.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @Controller: 동기적 요청에서 view 포함한 다양한 데이터 포맷을 처리
// @RestController: 비동기적 요청에서 view 제외하고 JSON/XML 형식의 데이터를 받기 위한 요청
@RestController
@Log4j2
@RequestMapping("/replies")
@RequiredArgsConstructor
public class ReplyController {
  private final ReplyService replyService;

  // 목록
  @GetMapping(value = "/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ReplyDTO>> getRepliesByBoardOrderByRno(@PathVariable Long bno) {
    return new ResponseEntity<>(replyService.getList(bno), HttpStatus.OK);
  }

  // 등록
  @PostMapping(value = {"", "/"})
  public ResponseEntity<String> register(@RequestBody ReplyDTO replyDTO) {
    Long result = replyService.register(replyDTO);
    if (result == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    return new ResponseEntity<>(result + "번 댓글 등록 ", HttpStatus.OK);
  }

  // 수정
  @PutMapping(value = {"", "/"})
  public ResponseEntity<String> update(@RequestBody ReplyDTO replyDTO) {
    Long result = replyService.modify(replyDTO);
    if (result == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    return new ResponseEntity<>(result.toString() + "번 댓글 수정", HttpStatus.OK);
  }

  // 삭제
  @DeleteMapping(value = {"", "/"})
  public ResponseEntity<String> delete(@RequestBody ReplyDTO replyDTO) {
    Long result = replyService.remove(replyDTO.getRno());
    if (result == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } else {
      return new ResponseEntity<>(result+"번 댓글 삭제", HttpStatus.OK);
    }
  }
}