package com.example.ex5.controller;

import com.example.ex5.dto.BoardDTO;
import com.example.ex5.dto.PageRequestDTO;
import com.example.ex5.dto.PageResultDTO;
import com.example.ex5.repository.BoardRepository;
import com.example.ex5.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {
  private final BoardService boardService;

  @GetMapping({"","/","list"})
  public String list(Model model, PageRequestDTO  pageRequestDTO) {
    model.addAttribute("pageResultDTO", boardService.getList(pageRequestDTO));
    return "/board/list";
  }

  @GetMapping("register")
  public void register() {
  }

  @PostMapping("register")
  // RedirectAttributes :: 1회용 데이터
  public String register(RedirectAttributes ra, BoardDTO boardDTO) {
    Long bno = boardService.register(boardDTO);
    ra.addFlashAttribute("msg", bno + "번 글이 등록되었습니다.");
    return "redirect:/board/list";
  }

  @GetMapping({"read","modify"})
  public void read(Model model, PageRequestDTO  pageRequestDTO,Long bno) {
    if (bno == null) {
      throw new IllegalArgumentException("bno가 전달되지 않았습니다.");
    }
    model.addAttribute("boardDTO", boardService.get(bno));
  }
  
  @PostMapping("modify")
  public String modify(RedirectAttributes ra, BoardDTO boardDTO, PageRequestDTO pageRequestDTO) {
    Long bno = boardService.modify(boardDTO);

    ra.addFlashAttribute("msg", bno + "번 글이 수정되었습니다."); //일회성
    ra.addAttribute("bno", bno);
    ra.addAttribute("page", pageRequestDTO.getPage());
    ra.addAttribute("type", pageRequestDTO.getType());
    ra.addAttribute("keyword", pageRequestDTO.getKeyword());
    return "redirect:/board/read";
  }

  @PostMapping("remove")
  public String remove(RedirectAttributes ra, Long bno, PageRequestDTO pageRequestDTO) {
    Long tmp = boardService.removeWithReplies(bno); //삭제 후 결과
    // 목록 하나를 지운 페이지에 나머지 갯수가 0일 경우
    if(tmp != null) {
      if(boardService.getList(pageRequestDTO).getDtoList().size() == 0
          && pageRequestDTO.getPage() != 1) {
        pageRequestDTO.setPage(pageRequestDTO.getPage() - 1);
      }
    }
    String msgResult = tmp!= null ? bno + "번 글이 삭제되었습니다":"삭제 실패";
    ra.addFlashAttribute("msg", msgResult);
    ra.addAttribute("page", pageRequestDTO.getPage());
    ra.addAttribute("type", pageRequestDTO.getType());
    ra.addAttribute("keyword", pageRequestDTO.getKeyword());
    return "redirect:/board/list";
  }

}
