package com.example.ex3.controller;

import com.example.ex3.entity.Memo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/memo")
public class MemoController {
  @GetMapping({"", "/"})
  public String index(Model model) {
    model.addAttribute("title","Hello world");
    List<Memo> memoList = IntStream.rangeClosed(1, 20).asLongStream().mapToObj(
        i -> {
          Memo memo = Memo.builder().mno(i).memoText("sample..."+i).build();
          return memo;
        }
    ).collect(Collectors.toList());
    model.addAttribute("memoList",memoList);
    return  "/memo/index.html";
  }

  @GetMapping("/regMemo")
  public void regMemo() { }

  @PostMapping("/regMemo")
  public String postMemo() {
    return "redirect:/memo";
  }


}
