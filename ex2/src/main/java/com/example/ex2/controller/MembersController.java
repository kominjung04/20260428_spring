package com.example.ex2.controller;

import com.example.ex2.dto.LoginDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/members") //get,post방식 둘다 받음
public class MembersController {

  //void일 경우 요청된 url이 resource 위치와 동일
  @GetMapping("join")
  public void join() {} //templates 밑에 파일이 있어야만 열림

  //String일 경우 요청된 url이 대신하여 임의로 지정가능
  @GetMapping("login")
  public String login() {
    return "/members/login";
  }

  @PostMapping("login")
  // 매개변수의 속성의 이름과 커맨드 객체의 속성의 이름이 view에서 넘어오는 name과 일치해야됨
  public String loginProcess(String id, String pass, LoginDTO dto, Model model, RedirectAttributes ra){
    System.out.println(id + "/" + pass);
    System.out.println(dto);
    //forward일 경우 model활용
    //model.addAttribute("id",id);
    //model.addAttribute("pass",pass);
    //return "forward:/";

    //redirect를 사용하는 경우 RedirectAttribute의 addFlashAttribute 활용
    //addAttribute 경우는 get방식으로 받을 수 있다.
    ra.addFlashAttribute("id",id); //post를 전송, 1회성
    ra.addFlashAttribute("pass",pass);
    ra.addAttribute("id2",id); //get으로 전송
    return "redirect:/";
  }

}
