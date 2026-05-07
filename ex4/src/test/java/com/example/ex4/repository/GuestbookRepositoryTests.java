package com.example.ex4.repository;

import com.example.ex4.entity.Guestbook;
import com.example.ex4.entity.QGuestbook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SpringBootTest
class GuestbookRepositoryTests {
  @Autowired
  private GuestbookRepository guestbookRepository;

  @Test
  public void insertDummies() {
    IntStream.rangeClosed(1, 300).forEach(i -> {
      Guestbook guestbook = Guestbook.builder()
          .title("Title..." + i)
          .content("Content..." + i)
          .writer("user" + (i % 10))
          .build();
      guestbookRepository.save(guestbook); //jpaRepository로 crud 자동 생성
    });
  }

  @Test
  //update를 위해 먼저 바꿀 값을 찾고 난 후 save
  public void updateTest() {
    //Optioanl - null값일 수도 아닐 수도 있는 값을 안전하게 표현
  Optional<Guestbook> result = guestbookRepository.findById(300l);
    if (result.isPresent()) {
      Guestbook guestbook = result.get();
      guestbook.changeTitle("Changed Title...");
      guestbook.changeContent("Changed Content...");
      guestbookRepository.save(guestbook);
    }
  }

  @Test
  public void testQuery() {
    //Pagealbe - 몇 페이지를 몇개씩 가져올지 정하는 객체(요청조건)
    //Page - 페이징된 결과(조회 결과)

    // 검색 선언을 위한 객체 선언
    Pageable pageable = PageRequest.of(0,10, Sort.by("gno").descending());
    // 동적검색을 위한 큐도메인(검색대상) 선언
    QGuestbook qGuestbook = QGuestbook.guestbook;

    String keyword = "1";

    // 검색을 위한 객체 선언
    BooleanBuilder bulider = new BooleanBuilder();

    // 다중 항목 검색 표현식
    BooleanExpression expression1 = qGuestbook.title.containsIgnoreCase(keyword);
    BooleanExpression expression2 = qGuestbook.content.containsIgnoreCase(keyword);
    BooleanExpression expression = expression1.or(expression2);

    bulider.and(expression); // 검색 적용 위한 최종 처리

    // 페이징 처리 후 결과를 담는 객체
    Page<Guestbook> result = guestbookRepository.findAll(bulider,pageable);

    result.stream().forEach(System.out::println); //출력
  }

}
