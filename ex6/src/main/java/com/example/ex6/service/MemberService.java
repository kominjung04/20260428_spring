package com.example.ex6.service;

import com.example.ex6.dto.MemberDTO;
import com.example.ex6.dto.PageRequestDTO;
import com.example.ex6.dto.PageResultDTO;
import com.example.ex6.entity.Member;

public interface MemberService {
  MemberDTO getMember(Long mid);
  Long update(MemberDTO memberDTO);
  PageResultDTO<MemberDTO, Member> getMemberList(PageRequestDTO pageRequestDTO);
  Long register(MemberDTO memberDTO);
  Long delete(Long mid);
  MemberDTO checkLogin(String email, String password);

  default MemberDTO entityToDTO(Member member) {
    return MemberDTO.builder()
        .mid(member.getMid())
        .email(member.getEmail())
        .pw(member.getPw())
        .nickname(member.getNickname())
        .regDate(member.getRegDate())
        .modDate(member.getModDate())
        .build();
  }

  default Member dtoToEntity(MemberDTO memberDTO) {
    return Member.builder()
        .mid(memberDTO.getMid())
        .email(memberDTO.getEmail())
        .pw(memberDTO.getPw())
        .nickname(memberDTO.getNickname())
        .build();
  }
}
