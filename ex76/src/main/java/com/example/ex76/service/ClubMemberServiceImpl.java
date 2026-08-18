package com.example.ex76.service;

import com.example.ex76.dto.ClubMemberDTO;
import com.example.ex76.entity.ClubMember;
import com.example.ex76.repository.ClubMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class ClubMemberServiceImpl implements ClubMemberService {
  private final ClubMemberRepository clubMemberRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public String modify(ClubMemberDTO clubMemberDTO) {
    Optional<ClubMember> result = clubMemberRepository.findByEmail(
        clubMemberDTO.getEmail(), clubMemberDTO.isFromSocial());
    if (result.isPresent()) {
      log.info("result.toString() >> "+result.toString());
      ClubMember clubMember = result.get();
      clubMember.changePassword(passwordEncoder.encode(clubMemberDTO.getPassword()));
      clubMember.changeName(clubMemberDTO.getName());
      clubMemberRepository.save(clubMember);
      log.info("변경후 >> "+result.toString());
    }
    return clubMemberDTO.getEmail();
  }

  @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
  @Override
  public void userAccess() {

  }

  @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
  @Override
  public void managerAccess() {

  }

  @PreAuthorize("hasRole('ADMIN')")
  @Override
  public void adminAccess() {

  }

  @PreAuthorize("#username == authentication.name")
  @Override
  public void selfAccess(String username) {

  }
}
