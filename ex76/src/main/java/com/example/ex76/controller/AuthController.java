package com.example.ex76.controller;

import com.example.ex76.dto.ClubMemberDTO;
import com.example.ex76.service.ClubMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final ClubMemberService clubMemberService;

  @GetMapping({"login", "logout", "accessDenied", "modify", "authenticationFailure"})
  public void auth(){}

  @PostMapping("modify")
  public String modify(RedirectAttributes ra, ClubMemberDTO clubMemberDTO){
    log.info(">>"+clubMemberDTO.toString());
    String email = clubMemberService.modify(clubMemberDTO);
    ra.addFlashAttribute("msg",email + "정보가 변경되었습니다.");
    return "redirect:/";
  }
}
