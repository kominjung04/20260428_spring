package com.example.ex4.service;

import com.example.ex4.dto.GuestbookDTO;
import com.example.ex4.dto.PageRequestDTO;
import com.example.ex4.dto.PageResultDTO;
import com.example.ex4.entity.Guestbook;
import com.example.ex4.repository.GuestbookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class GuestbookServiceImpl implements GuestbookService {


  private final GuestbookRepository guestbookRepository;

  @Override
  public Long register(GuestbookDTO guestbookDTO) {
    // save 하면 저장된 엔티티를 리턴
    Guestbook guestbook = guestbookRepository.save(dtoToEntity(guestbookDTO));
    return guestbook.getGno();
  }

  @Override
  public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO pageRequestDTO) {
    // PageResultDTO의 첫번째 매개변수
    Pageable pageable = pageRequestDTO.getPageable(Sort.by("gno").descending());
    Page<Guestbook> result = guestbookRepository.findAll(pageable);

    // PageResultDTO의 두번째 매개변수
    Function<Guestbook, GuestbookDTO> fn = new  Function<Guestbook, GuestbookDTO>() {
      @Override
      public GuestbookDTO apply(Guestbook guestbook) {
        return entityToDto(guestbook);
      }
    };
    return new PageResultDTO<>(result, fn);
  }

  @Override
  public GuestbookDTO read(Long gno) {
    Optional<Guestbook> result = guestbookRepository.findById(gno);
    if(result.isPresent()) {
      return entityToDto(result.get());
    }
    return null;
  }
}
