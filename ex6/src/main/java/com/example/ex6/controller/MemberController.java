package com.example.ex6.controller;

import com.example.ex6.dto.MemberDTO;
import com.example.ex6.dto.MovieDTO;
import com.example.ex6.dto.PageRequestDTO;
import com.example.ex6.repository.MemberRepository;
import com.example.ex6.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
  private final MemberService memberService;

  @GetMapping({"", "/", "list"})
  public String list(PageRequestDTO pageRequestDTO, Model model) {
    model.addAttribute("pageResultDTO", memberService.getList(pageRequestDTO));
    return "/member/list";
  }

  @GetMapping("register")
  public void register(){}

  @PostMapping("register")
  public String register(MemberDTO memberDTO, RedirectAttributes ra) {
    Long mid = memberService.register(memberDTO);
    ra.addFlashAttribute("msg", mid + "번 회원이 등록되었습니다.");
    return "redirect:/member/list";
  }

  @GetMapping({"read", "modify"})
  public void get(Long mid, PageRequestDTO pageRequestDTO, Model model) {
    MemberDTO dto = memberService.get(mid);
    model.addAttribute("memberDTO", dto);
  }

  @PostMapping("/modify")
  public String modify(MemberDTO memberDTO, RedirectAttributes ra, PageRequestDTO pageRequestDTO) {
    log.info("modify.... memberDTO:" + memberDTO); //movieDTO에는 mno, title, imageDTOList 가 넘어옴
    memberService.modify(memberDTO); // service 이동
    ra.addFlashAttribute("msg", memberDTO.getMid() + " 수정");
    ra.addAttribute("mid", memberDTO.getMid());
    ra.addAttribute("page", pageRequestDTO.getPage());
    ra.addAttribute("type", pageRequestDTO.getType());
    ra.addAttribute("keyword", pageRequestDTO.getKeyword());
    return "redirect:/member/read";
  }

  @PostMapping("/remove")
  public String remove(Long mid, RedirectAttributes ra, PageRequestDTO pageRequestDTO){
    log.info("remove post... mid: " + mid);
    memberService.remove(mid);

    //memberService.removeWithReviewsAndMovieImages(mno);

    if(memberService.getList(pageRequestDTO).getDtoList().size() == 0 && pageRequestDTO.getPage() != 1) {
      pageRequestDTO.setPage(pageRequestDTO.getPage()-1);
    }
    ra.addFlashAttribute("msg", mid + " 삭제");
    ra.addAttribute("page", pageRequestDTO.getPage());
    ra.addAttribute("type", pageRequestDTO.getType());
    ra.addAttribute("keyword", pageRequestDTO.getKeyword());
    return "redirect:/member/list";
  }
}
