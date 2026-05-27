package com.example.ex7.service;

import com.example.ex7.dto.ClubMemberDTO;


public interface ClubMemberService {
  String modify(ClubMemberDTO clubMemberDTO);

  void userAccess();
  void managerAccess();
  void adminAccess();
  void selfAdminAccess(String username);

}
