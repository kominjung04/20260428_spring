package com.example.ex4.service;

import com.example.ex4.dto.GuestbookDTO;
import com.example.ex4.dto.PageRequestDTO;
import com.example.ex4.dto.PageResultDTO;
import com.example.ex4.entity.Guestbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GuestbookServiceTests {

  @Autowired
  GuestbookService guestbookService;

  @Test
  void testRegister() {
    GuestbookDTO guestbookDTO = GuestbookDTO.builder()
        .title("Sample Title")
        .content("Sample Content")
        .writer("Writer0")
        .build();
    System.out.println(guestbookService.register(guestbookDTO));
  }

  @Test
  public void testGetList() {
    PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();
    PageResultDTO<GuestbookDTO, Guestbook> pageResultDTO = guestbookService.getList(pageRequestDTO);
    System.out.println("===============================");
    System.out.println("Page: " + pageResultDTO.getPage());
    System.out.println("Size: " + pageResultDTO.getSize());
    System.out.println("Start: " + pageResultDTO.getStart());
    System.out.println("End: " + pageResultDTO.getEnd());
    System.out.println("Prev: " + pageResultDTO.isPrev());
    System.out.println("Next: " + pageResultDTO.isNext());
    System.out.println("Total: " + pageResultDTO.getTotalPage());

    pageResultDTO.getDtoList().forEach(System.out::println);
  }
}
