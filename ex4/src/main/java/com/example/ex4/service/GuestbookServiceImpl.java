package com.example.ex4.service;

import com.example.ex4.dto.GuestbookDTO;
import com.example.ex4.dto.PageRequestDTO;
import com.example.ex4.dto.PageResultDTO;
import com.example.ex4.entity.Guestbook;
import com.example.ex4.entity.QGuestbook;
import com.example.ex4.repository.GuestbookRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    // 페이지별 검색을 위한 객체 선언
    Pageable pageable = pageRequestDTO.getPageable(Sort.by("gno").descending());

    // 검색 조건 객체
    BooleanBuilder builder = getSearch(pageRequestDTO);

    // PageResultDTO의 첫번째 매개변수
    //Page<Guestbook> result = guestbookRepository.findAll(pageable); //동적검색 무
    Page<Guestbook> result = guestbookRepository.findAll(builder, pageable); //동적검색 유

    // PageResultDTO의 두번째 매개변수
    Function<Guestbook, GuestbookDTO> fn = new Function<Guestbook, GuestbookDTO>() {
      @Override
      public GuestbookDTO apply(Guestbook guestbook) {
        return entityToDto(guestbook);
      }
    };
    return new PageResultDTO<>(result, fn);
  }

  private BooleanBuilder getSearch(PageRequestDTO pageRequestDTO) {
    String type = pageRequestDTO.getType();
    String keyword = pageRequestDTO.getKeyword();
    QGuestbook qGuestbook = QGuestbook.guestbook; // 검색 원천 큐도메인

    BooleanBuilder builder = new BooleanBuilder();
    BooleanExpression booleanExpression = qGuestbook.gno.gt(0L); // 0보다 큰값만 검색
    builder.and(booleanExpression);

    // 검색 조건이 없는 경우(검색조건이 전체 보기, 첫페이지 경우)
    if(type == null || type.trim().length() == 0) {return builder;}
    if(keyword == null || keyword.trim().length() == 0) {return builder;}

    // 검색 조건이 있는 경우
    BooleanBuilder conditionBuilder = new BooleanBuilder(); //검색조건을 위한 새 builder
    if(type.contains("t")) conditionBuilder.or(qGuestbook.title.containsIgnoreCase(keyword));
    if(type.contains("c")) conditionBuilder.or(qGuestbook.content.containsIgnoreCase(keyword));
    if(type.contains("w")) conditionBuilder.or(qGuestbook.writer.containsIgnoreCase(keyword));

    // 최종 검색을 위한 조건 더하기.
    builder.and(conditionBuilder);
    return builder;
  }

  @Override
  public GuestbookDTO read(Long gno) {
    Optional<Guestbook> result = guestbookRepository.findById(gno);
    if (result.isPresent()) {
      return entityToDto(result.get());
    }
    return null;
  }

  @Override
  //@Transactional // 수정후 수정일자가 변경되지 않을 경우 추가
  public Long modify(GuestbookDTO guestbookDTO) {
    Long result = null;
    Optional<Guestbook> find = guestbookRepository.findById(guestbookDTO.getGno());
    if (find.isPresent()) {
      Guestbook guestbook = find.get();  //찾은것의 일부만 변경
      guestbook.changeTitle(guestbookDTO.getTitle());
      guestbook.changeContent(guestbookDTO.getContent());
      guestbookRepository.save(guestbook);
      result = guestbook.getGno();
    }
    return result;
  }

  @Override
  public Long remove(Long gno) {
    try {
      guestbookRepository.deleteById(gno);
    } catch (Exception e) {
      return null;
    }
    return gno;
  }


}