package com.example.ex3.repository;

import com.example.ex3.entity.Memo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
  //JpaRepository를 상속 받는 것만으로 CRUD를 사용 가능.

  //쿼리메서드 :: 약속된 단어를 조합하여 메서드명으로도 질의가 가능.
  //https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html#page-title
  List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);
  Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);
  void deleteMemoByMnoLessThan(Long num);



  //@Query :: 쿼리메서드가 단어를 조합하는 반면 JPQL을 활용하여 질의가 가능
  @Query("select m from Memo m order by m.mno desc ")
  List<Memo> getListDesc();

  @Transactional
  @Modifying
  @Query("update Memo m set m.memoText = :memoText where m.mno = :mno ")
  int updateMemoText(@Param("mno") Long mno, String memoText);// @Param 사용안해도 가능


  @Transactional
  @Modifying
  @Query("update Memo m set m.memoText = :#{#param.memoText} where m.mno = :#{#param.mno} ")
  int updateMemoText2(@Param("mno") Long mno, String memoText);

  //Page<Memo>로 받을 경우 value와 countQuery를 같이 정의해야 함.
  @Query(value = "select m from Memo m where m.mno > :mno ",
      countQuery = "select count(m) from Memo m where m.mno > :mno ")
  Page<Memo> getListWithQuery(Long mno, Pageable pageable);

  @Query(value = "select m.mno, m.memoText, CURRENT_DATE from Memo m where m.mno > :mno ",
      countQuery = "select count(m) from Memo m where m.mno > :mno ")
  Page<Object[]> getListWithQueryObject(Long mno, Pageable pageable);

  // JPQL대신에 SQL을 활용할 경우 nativeQuery = true 적용
  @Query(value = "select * from tbl_memo where mno <= 20 ", nativeQuery = true)
  List<Memo> getNativeResult();
}
