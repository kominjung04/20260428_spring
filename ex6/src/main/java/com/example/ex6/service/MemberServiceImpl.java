package com.example.ex6.service;

import com.example.ex6.dto.MemberDTO;
import com.example.ex6.dto.PageRequestDTO;
import com.example.ex6.dto.PageResultDTO;
import com.example.ex6.entity.Member;
import com.example.ex6.repository.MemberRepository;
import com.example.ex6.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
  private final MemberRepository memberRepository;
  private final ReviewRepository reviewRepository;

  @Override
  public Long register(MemberDTO memberDTO) {
    return memberRepository.save(dtoToEntity(memberDTO)).getMid();
  }

  @Override
  public MemberDTO getMember(Long mid) {
    Member member = memberRepository.findById(mid).orElseThrow();
    return entityToDTO(member);
  }

  @Override
  @Transactional
  public Long update(MemberDTO memberDTO) {
    Optional<Member> result = memberRepository.findById(memberDTO.getMid());
    if (result.isPresent()) {
      Member member = result.get();
      member.changePw(memberDTO.getPw());
      member.changeNickname(memberDTO.getNickname());
      memberRepository.save(member);
    }
    return memberDTO.getMid();
  }

  @Override
  @Transactional
  public Long delete(Long mid) {
    Optional<Member> member = memberRepository.findById(mid);
    if (member.isPresent()) {
      reviewRepository.deleteByMember(member.get());
      memberRepository.delete(member.get());
    }
    return mid;
  }

  @Override
  public PageResultDTO<MemberDTO, Member> getMemberList(PageRequestDTO pageRequestDTO) {
    Pageable pageable = pageRequestDTO.getPageable(Sort.by("mid").descending());

    Page<Member> result = memberRepository.searchPage(pageRequestDTO.getType(),
        pageRequestDTO.getKeyword(), pageable);

    Function<Member, MemberDTO> fn = member -> entityToDTO(member);

    return new PageResultDTO<>(result, fn);
  }

  @Override
  public MemberDTO checkLogin(String email, String password) {
    Optional<Member> result = memberRepository.findMemberByEmail(email);
    if(result.isPresent()) {
      Member member = result.get();
      if(member.getPw().equals(password)) {
        return entityToDTO(member);
      }
    };
    return null;
  }
}
