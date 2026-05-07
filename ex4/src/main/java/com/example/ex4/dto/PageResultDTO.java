package com.example.ex4.dto;

import lombok.Data;

import java.util.List;

// 페이지 요청에 의한 결과를 담긴 위한 객체
@Data
public class PageResultDTO<DTO,EN> {
  private List<DTO> dtoList; // 페이지 내용에 대한 목록
  private int totalPage; // 총 페이지 수
  private int page; // 요청한 페이지 번호
  private int size; // 페이지당 개수
  private int start, end; // 페이지 번호의 시작번호와 끝번호
  private boolean prev, next; // 다음, 이번 버튼에 대한 정보
  private List<Integer> pageList; // 페이지 번호에 대한 목록
}
