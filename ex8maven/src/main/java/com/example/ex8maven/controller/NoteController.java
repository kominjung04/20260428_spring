package com.example.ex8maven.controller;

import com.example.ex8maven.dto.NoteDTO;
import com.example.ex8maven.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "알림 API", description = "컨트롤러에 대한 설명입니다.")
@RestController
@RequestMapping("/notes/")
@Log4j2
@RequiredArgsConstructor
public class NoteController {
  private final NoteService noteService;

  // ResponseEntity<T> :: HTTP 응답전체를 직접 제어(응답본문:body, HTTP 상태코드:status, HTTP 헤더:headers)
  // status :: 성공:200, 생성:201, 삭제:204,잘못된요청:400, 인증실패:401, 못찾을경우: 404, 서버오류:500

  @Operation(summary = "Note Register", description = "새로운 Note를 등록합니다.")
  @PostMapping(value = "")
  public ResponseEntity<Long> register(@RequestBody NoteDTO noteDTO) {
    return new ResponseEntity<>(noteService.register(noteDTO), HttpStatus.OK);
  }

  @Operation(summary = "노트 목록 불러오기", description = "이메일로 관련 노트 정보를 수집합니다.")
  @Parameter(name = "writerEmail", description = "Note를 작성한 사람")
  @GetMapping(value = "" , produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<NoteDTO>> findByWriter(String writerEmail) {
    List<NoteDTO> list = noteService.getAllWithWriter(writerEmail);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }
  @Operation(summary = "노트 조회", description = "노트 번호(num)로 특정 노트를 조회합니다.")
  @Parameter(name = "num", description = "Note의 ID")
  @GetMapping(value = "/{num}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<NoteDTO> read(@PathVariable Long num) {
    return new ResponseEntity<>(noteService.get(num), HttpStatus.OK);
  }

  @Operation(summary = "노트 수정", description = "노트 정보를 수정합니다.")
  @PutMapping(value = "",  produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> modify(@RequestBody NoteDTO noteDTO) {
    noteService.modify(noteDTO);
    return new ResponseEntity<>(noteDTO.getNum() + " 수정", HttpStatus.OK);
  }

  @Operation(summary = "노트 삭제", description = "노트 번호(num)로 특정 노트를 삭제합니다.")
  @DeleteMapping(value = "/{num}",  produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> delete(@PathVariable Long num) {
    noteService.remove(NoteDTO.builder().num(num).build());
    return new ResponseEntity<>(num + " 삭제", HttpStatus.OK);
  }


}
