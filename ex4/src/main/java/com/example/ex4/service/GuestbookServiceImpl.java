package com.example.ex4.service;

import com.example.ex4.dto.GuestbookDTO;
import com.example.ex4.entity.Guestbook;
import com.example.ex4.repository.GuestbookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class GuestbookServiceImpl implements GuestbookService{
  //@Autowired - 순환참조로 인한 오류 발생
  private final GuestbookRepository guestbookRepository;

  @Override
  public Long register(GuestbookDTO guestbookDTO) {
    //dto -> entity로 바꿔서 register구성
    /* Guestbook guestbook = guestbookRepository.save(dtoToEntity(guestbookDTO));
    return guestbook.getGno();*/
    return guestbookRepository.save(dtoToEntity(guestbookDTO)).getGno();
  }

}
