package com.example.ex4.controller;

import com.example.ex4.dto.GuestbookDTO;
import com.example.ex4.dto.PageRequestDTO;
import com.example.ex4.service.GuestbookService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.querydsl.QPageRequest;
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

  @GetMapping({"", "/", "list"})
  public String list(Model model, PageRequestDTO pageRequestDTO) {
    model.addAttribute("pageResultDTO", guestbookService.getList(pageRequestDTO));
    return "guestbook/list";
  }

  @GetMapping("register")
  public void register() {
  }

  @PostMapping("register")
  // RedirectAttributes :: 1회용 데이터
  public String register(RedirectAttributes ra, GuestbookDTO guestbookDTO) {
    Long gno = guestbookService.register(guestbookDTO);
    ra.addFlashAttribute("msg", gno + "번 글이 등록되었습니다.");
    return "redirect:/guestbook/list";
  }

  @GetMapping({"read", "modify"})
  public void read(Model model, PageRequestDTO pageRequestDTO, Long gno) {
    if (gno == null) {
      throw new IllegalArgumentException("gno가 전달되지 않았습니다.");
    }
    model.addAttribute("guestbookDTO", guestbookService.read(gno));
  }

  @PostMapping("modify")
  public String modify(RedirectAttributes ra, GuestbookDTO guestbookDTO, PageRequestDTO pageRequestDTO) {
    Long gno = guestbookService.modify(guestbookDTO);
    typeKeywordInit(pageRequestDTO); // null문자열이 넘어올 경우

    ra.addFlashAttribute("msg", gno + "번 글이 수정되었습니다."); //일회성
    ra.addAttribute("gno", gno);
    ra.addAttribute("page", pageRequestDTO.getPage());
    ra.addAttribute("type", pageRequestDTO.getType());
    ra.addAttribute("keyword", pageRequestDTO.getKeyword());
    return "redirect:/guestbook/read";
  }

  @PostMapping("remove")
  public String remove(RedirectAttributes ra, Long gno, PageRequestDTO pageRequestDTO) {
    Long tmp = guestbookService.remove(gno); //삭제 후 결과
    // 목록 하나를 지운 페이지에 나머지 갯수가 0일 경우
    if(tmp != null) {
      if(guestbookService.getList(pageRequestDTO).getDtoList().size() == 0
        && pageRequestDTO.getPage() != 1) {
        pageRequestDTO.setPage(pageRequestDTO.getPage() - 1);
      }
    }
    String msgResult = tmp!= null ? gno + "번 글이 삭제되었습니다":"삭제 실패";
    typeKeywordInit(pageRequestDTO); // null문자열이 넘어올 경우
    ra.addFlashAttribute("msg", msgResult);
    ra.addAttribute("page", pageRequestDTO.getPage());
    ra.addAttribute("type", pageRequestDTO.getType());
    ra.addAttribute("keyword", pageRequestDTO.getKeyword());
    return "redirect:/guestbook/list";
  }

  //type,keword에 null값이 문자열로 올 때 그것을 지울때
  private void typeKeywordInit(PageRequestDTO pageRequestDTO) {
    if(pageRequestDTO.getType().equals("null") || pageRequestDTO.getType()==null)
      pageRequestDTO.setType(null);
    if(pageRequestDTO.getKeyword().equals("null") || pageRequestDTO.getKeyword()==null)
      pageRequestDTO.setKeyword(null);
  }
}