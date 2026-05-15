package com.example.ex5.repository.search;

import com.example.ex5.entity.Board;
import com.example.ex5.entity.QBoard;
import com.example.ex5.entity.QMember;
import com.example.ex5.entity.QReply;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

  public SearchBoardRepositoryImpl() {
    super(Board.class);
  }

  @Override
  public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
   // 1) 도메인을 확보
    QBoard qBoard = QBoard.board;
    QMember qMember = QMember.member;
    QReply qReply = QReply.reply;

    // 2) 도메인을 조인
    JPQLQuery<Board> jpqlquery = from(qBoard);
    jpqlquery.leftJoin(qMember).on(qBoard.writer.eq(qMember));
    jpqlquery.leftJoin(qReply).on(qReply.board.eq(qBoard));

    // 3) Tuple 생성: 조인한 객체와 select를 이용해서 필요한 데이터를 tuple로 생성
    JPQLQuery<Tuple> tuple = jpqlquery.select(qBoard,qMember,qReply.count());

    // 4) 조건절 검색을 위한 객체를 생성
    BooleanBuilder builder = new BooleanBuilder();
    BooleanExpression expression = qBoard.bno.gt(0L); //기본 검색 조건
    builder.and(expression);

    // 5) 검색 조건 추가
    //필수 검색 조건 지정
    if (type != null) {
      String[] arr = type.split(""); //한글자씩 떼어서 배열 생성
      BooleanBuilder condition = new BooleanBuilder();
      for (String t : arr) {
        switch (t) {
          case "t" -> condition.and(qBoard.title.containsIgnoreCase(keyword));
          case "w" -> condition.and(qMember.email.containsIgnoreCase(keyword));
          default -> condition.and(qBoard.content.containsIgnoreCase(keyword));
        }
      }
      builder.and(condition);
    }
    // 6) 조인된 tuple에 추가된 조건절 적용
    tuple.where(builder);
    // 7) 조인된 데이터의 select를 위한 group by 설정
    tuple.groupBy(qBoard);

    // 8) 정렬 조건 추가
    Sort sort = pageable.getSort(); //pageable에서 정렬 정보를 가져온다.
    sort.stream().forEach(order ->{ //하나씩 꺼내본다
      Order direction = order.isAscending()? Order.ASC : Order.DESC; //정렬객체 지정
      // Querydsl에서 동적으로 컬럼 경로를 만들기 위한 객체
      PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
      // 정렬조건을 추가하는 부분
      tuple.orderBy(new OrderSpecifier<Comparable>(direction, orderByExpression));
    });
    // 9) tuple의 데이터를 가져오기 위한 시작 위치 지정(offset 지정)
    tuple.offset(pageable.getOffset());

    // 10) tuple의 데이터를 가져올 때 개수 지정
    tuple.limit(pageable.getPageSize());

    // 11) 최정결과를 tuple의 fetch()를 통해서 컬렉션으로 변환
    List<Tuple> result = tuple.fetch();

    // 12) tuple의 검색 결과 개수
    Long count = tuple.fetchCount();
    log.info("총 개수: " + count);

    // 13) Page 객체를 PageImpl 객체로 변환
    return new PageImpl<Object[]> (result.stream()
        .map(t -> t.toArray()).collect(Collectors.toList()), pageable, count);
  }
}
