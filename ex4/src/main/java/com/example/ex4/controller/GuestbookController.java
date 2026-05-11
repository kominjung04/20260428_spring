package com.example.ex4.controller;

import com.example.ex4.dto.GuestbookDTO;
import com.example.ex4.dto.PageRequestDTO;
import com.example.ex4.service.GuestbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor
public class GuestbookController {
  private final GuestbookService guestbookService;

  @GetMapping({"","/","list"})
  public String list(Model model, PageRequestDTO pageRequestDTO) {
    model.addAttribute("pageResultDTO", guestbookService.getList(pageRequestDTO));
    return "/guestbook/list";
  }

  @GetMapping("register")
  public void register() {}

  @PostMapping("register")
  // RedirectAttributes :: 1회용 데이터
  public String register(RedirectAttributes ra, GuestbookDTO guestbookDTO) {
    Long gno = guestbookService.register(guestbookDTO);
    ra.addFlashAttribute("msg", gno+"번 글이 등록되었습니다.");
    return "redirect:/guestbook/list";
  }

  @GetMapping("read")
  public void read(Model model, PageRequestDTO pageRequestDTO, Long gno) {
  model.addAttribute("guestbookDTO",guestbookService.read(gno));
  }
}