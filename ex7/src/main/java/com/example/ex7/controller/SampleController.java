package com.example.ex7.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sample")
@Log4j2
public class SampleController {
  @GetMapping("all")
  public void exAll(){}

  @GetMapping("manager")
  public void exManager(){}

  @GetMapping("admin")
  @PreAuthorize("hasRole('ADMIN')")
  public void exAdmin(){}

}
