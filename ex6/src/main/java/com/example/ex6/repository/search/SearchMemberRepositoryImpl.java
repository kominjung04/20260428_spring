package com.example.ex6.repository.search;

import com.example.ex6.entity.Member;
import com.example.ex6.entity.QMember;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Log4j2
public class SearchMemberRepositoryImpl extends QuerydslRepositorySupport implements SearchMemberRepository {

  public SearchMemberRepositoryImpl() {
    super(Member.class);
  }

  @Override
  public Page<Member> searchPage(String type, String keyword, Pageable pageable) {
    QMember member = QMember.member;
    JPQLQuery<Member> query = from(member);

    BooleanBuilder builder = new BooleanBuilder();
    builder.and(member.mid.gt(0L));

    if (type != null && keyword != null && !keyword.isBlank()) {
      BooleanBuilder condition = new BooleanBuilder();

      for (char t : type.toCharArray()) {
        switch (t) {
          case 'e' -> condition.or(member.email.containsIgnoreCase(keyword));
          case 'n' -> condition.or(member.nickname.containsIgnoreCase(keyword));
        }
      }

      builder.and(condition);
    }

    query.where(builder);

    Sort sort = pageable.getSort();
    if (sort.isSorted()) {
      for (Sort.Order order : sort) {
        com.querydsl.core.types.Order direction =
            order.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC;

        switch (order.getProperty()) {
          case "mid" -> query.orderBy(new OrderSpecifier<>(direction, member.mid));
          case "email" -> query.orderBy(new OrderSpecifier<>(direction, member.email));
          case "nickname" -> query.orderBy(new OrderSpecifier<>(direction, member.nickname));
        }
      }
    }

    long count = query.fetchCount();

    List<Member> result = query
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    return new PageImpl<>(result, pageable, count);
  }
}
