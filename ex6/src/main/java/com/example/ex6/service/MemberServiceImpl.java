package com.example.ex6.service;

import com.example.ex6.dto.MemberDTO;
import com.example.ex6.dto.PageRequestDTO;
import com.example.ex6.dto.PageResultDTO;
import com.example.ex6.entity.Member;
import com.example.ex6.entity.Review;
import com.example.ex6.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.example.ex6.entity.QMember.member;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
  private final MemberRepository memberRepository;

  @Override
  public Long register(MemberDTO memberDTO) {
    Map<String, Object> map = dtoToEntity(memberDTO);
    Member member = (Member) map.get("member");
    memberRepository.save(member);
    return member.getMid();
  }

  @Override
  public PageResultDTO<MemberDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
    Pageable pageable = pageRequestDTO.getPageable(Sort.by("mid").descending());

    Page<Member> result = memberRepository.findAll(pageable);
    Page<Object[]> mresult = result.map(m -> new Object[]{m});
    Function<Object[], MemberDTO> fn = (arr -> entitiesToDTO((Member) arr[0]));
    return new PageResultDTO<>(mresult, fn);
  }

  @Override
  public MemberDTO get(Long mid) {
    Optional<Member> result = memberRepository.findById(mid);

    if (result.isPresent()) {
      return entitiesToDTO(result.get());
    }
    return null;
  }

  @Override
  public Long modify(MemberDTO memberDTO) {
    Optional<Member> result = memberRepository.findById(memberDTO.getMid());
    if (result.isPresent()) {
      Member member = result.get();
      member.changeNickname(memberDTO.getNickname());
      member.changePw(memberDTO.getPw());
      memberRepository.save(member);
      return member.getMid(); // 수정이 잘되면 댓글번호 리턴
    }
    return 0L; // 변경하고자 하는 것이 없는 경우 0을 리턴
  }

  @Override
  public Long remove(Long mid) {
    try {
      memberRepository.deleteById(mid);
    } catch (Exception e) {
      return 0L;
    }
    return mid;
  }
}

