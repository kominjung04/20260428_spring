package com.example.ex2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
  @RequestMapping({"","/","/home"})
  public String home(){
    return "/index.html";
  }
}
