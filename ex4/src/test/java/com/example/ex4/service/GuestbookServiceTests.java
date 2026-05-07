package com.example.ex4.service;

import com.example.ex4.dto.GuestbookDTO;
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

}