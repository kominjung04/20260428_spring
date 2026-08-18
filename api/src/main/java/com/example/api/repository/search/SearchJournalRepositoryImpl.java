package com.example.api.repository.search;

import com.example.api.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Log4j2
@RequiredArgsConstructor // JPAQueryFactory 주입을 위한 생성자 자동 생성
public class SearchJournalRepositoryImpl implements SearchJournalRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        // 1) 도메인 선언
        QJournal qJournal = QJournal.journal;
        QPhotos qPhotos = QPhotos.photos;
        QPhotos qPhotosSub = QPhotos.photos;
        QMembers qMembers = QMembers.members;
        QComments qComments = QComments.comments;

        // 2) 검색 조건 객체 생성 및 기본 조건 지정
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qJournal.jno.gt(0L)); // 기본 검색 조건

        // 3) 검색 조건 추가
        if (type != null && !type.trim().isEmpty()) {
            String[] typeArr = type.split("");
            BooleanBuilder condition = new BooleanBuilder();
            for (String t : typeArr) {
                switch (t) {
                    case "t" -> condition.or(qJournal.title.containsIgnoreCase(keyword));
                    case "w" -> condition.or(qMembers.email.containsIgnoreCase(keyword));
                    case "c" -> condition.or(qJournal.content.containsIgnoreCase(keyword));
                }
            }
            builder.and(condition);
        }

        // 4) 본 쿼리 객체 생성 (JPAQueryFactory 활용)
        JPAQuery<Tuple> query = queryFactory
                .select(
                        qJournal,
                        qPhotos,
                        qMembers,
                        qComments.likes.sum().coalesce(0L),
                        qComments.countDistinct()
                )
                .from(qJournal)
                .leftJoin(qPhotos).on(
                        qPhotos.journal.eq(qJournal)
                                .and(qPhotos.pno.eq(
                                        JPAExpressions
                                                .select(qPhotosSub.pno.max())
                                                .from(qPhotosSub)
                                                .where(qPhotosSub.journal.eq(qJournal))
                                ))
                )
                .leftJoin(qMembers).on(qJournal.members.eq(qMembers))
                .leftJoin(qComments).on(qComments.journal.eq(qJournal))
                .where(builder)
                .groupBy(
                        qJournal.jno,
                        qPhotos.pno,
                        qMembers.mid
                );
        // 5) 정렬 조건 추가
        Sort sort = pageable.getSort();
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            PathBuilder orderByExpression = new PathBuilder(Journal.class, "journal");
            query.orderBy(
                    new OrderSpecifier<>(
                            direction,
                            orderByExpression.get(order.getProperty())
                    )
            );
        });

        // 6) 데이터 카운트 조회 (페이징용 총 개수)
        // 기존의 query 객체 구조를 재사용하여 count 쿼리를 빌드합니다.
        Long count = queryFactory
                .select(qJournal.countDistinct())
                .from(qJournal)
                .leftJoin(qPhotos).on(
                        qPhotos.journal.eq(qJournal)
                                .and(qPhotos.pno.eq(
                                        JPAExpressions
                                                .select(qPhotosSub.pno.max())
                                                .from(qPhotosSub)
                                                .where(qPhotosSub.journal.eq(qJournal))
                                ))
                )
                .leftJoin(qComments).on(qComments.journal.eq(qJournal))
                .leftJoin(qMembers).on(qComments.members.eq(qMembers))
                .where(builder)
                .fetchOne();
        if (count == null) count = 0L;
        log.info("총 개수 출력: " + count);

        // 7) 페이징 오프셋 및 제한 설정 후 데이터 fetch
        List<Tuple> result = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 8) Page 객체 변환 및 반환
        List<Object[]> content = result.stream()
                .map(Tuple::toArray)
                .toList();
        return new PageImpl<>(content, pageable, count);
    }
}
