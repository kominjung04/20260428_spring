package com.example.ex6.controller;

import com.example.ex6.dto.MemberDTO;
import com.example.ex6.dto.PageRequestDTO;
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
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping({"", "/", "list"})
    public String memberList(Model model, PageRequestDTO pageRequestDTO) {
        model.addAttribute("pageResultDTO", memberService.getMemberList(pageRequestDTO));
        return "/member/list";
    }

    @GetMapping("register")
    public void register() {  }

    @PostMapping("/register")
    public String register(RedirectAttributes ra, MemberDTO memberDTO) {
        Long mid = memberService.register(memberDTO);
        ra.addFlashAttribute("msg", mid + "번 회원 등록 완료");
        return "redirect:/member/list";
    }

    @GetMapping({"read", "modify"})
    public void read(Model model, Long mid, PageRequestDTO pageRequestDTO) {
        model.addAttribute("memberDTO", memberService.getMember(mid));
    }

    @PostMapping("modify")
    public String modify(RedirectAttributes ra, MemberDTO memberDTO, PageRequestDTO pageRequestDTO) {
        memberService.update(memberDTO);
        ra.addFlashAttribute("msg", memberDTO.getMid() + "번 회원 수정 완료");
        ra.addAttribute("mid", memberDTO.getMid());
        ra.addAttribute("page", pageRequestDTO.getPage());
        ra.addAttribute("type", pageRequestDTO.getType());
        ra.addAttribute("keyword", pageRequestDTO.getKeyword());
        return "redirect:/member/read";
    }


    @PostMapping("remove")
    public String remove(RedirectAttributes ra, Long mid,PageRequestDTO pageRequestDTO) {
        memberService.delete(mid);
        if (memberService.getMemberList(pageRequestDTO).getDtoList().isEmpty()
                && pageRequestDTO.getPage() != 1) {
            pageRequestDTO.setPage(pageRequestDTO.getPage()-1);
        }
        ra.addFlashAttribute("msg", mid + "번 회원 삭제 완료");
        ra.addAttribute("page", pageRequestDTO.getPage());
        ra.addAttribute("type", pageRequestDTO.getType());
        ra.addAttribute("keyword", pageRequestDTO.getKeyword());
        return "redirect:/member/list";
    }

}
