package com.example.ex6.service;

import com.example.ex6.dto.*;
import com.example.ex6.entity.Member;
import com.example.ex6.entity.Movie;
import com.example.ex6.entity.MovieImage;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.ex6.entity.QMovie.movie;

public interface MemberService {
  Long register(MemberDTO memberDTO);
  PageResultDTO<MemberDTO, Object[]> getList(PageRequestDTO pageRequestDTO);
  MemberDTO get(Long mid);
  Long modify(MemberDTO memberDTO);
  public Long remove(Long mid);

  default Map<String, Object> dtoToEntity(MemberDTO memberDTO) {
    Map<String, Object> map = new HashMap<>();
    Member member = Member.builder()
        .mid(memberDTO.getMid())
        .email(memberDTO.getEmail())
        .pw(memberDTO.getPw())
        .nickname(memberDTO.getNickname())
        .build();
    map.put("member", member);
    return map;

  }

  default MemberDTO entitiesToDTO(Member member) {
    MemberDTO memberDTO = MemberDTO.builder()
        .mid(member.getMid())
        .email(member.getEmail())
        .pw(member.getPw())
        .nickname(member.getNickname())
        .regDate(member.getRegDate())
        .modDate(member.getModDate())
        .build();
    return memberDTO;
  }
}
