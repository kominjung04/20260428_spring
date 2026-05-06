package com.example.ex3.repository;

import com.example.ex3.entity.Memo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MemoRepositoryTest {
  @Autowired
  MemoRepository memoRepository;

  @Test
  public void testClass() {
    System.out.println(memoRepository.getClass().getName());
  }

  @Test
  public void testInsetDummies(){
    IntStream.rangeClosed(1,100).forEach(value -> {
      Memo memo = Memo.builder().memoText("Sample..."+value).build();
      memoRepository.save(memo);
    });
  }

  @Test
  public void testSelect() {
    Long mno = 100L;
    // findById()의 리턴값은 Optional타입으로 반환
    Optional<Memo> result = memoRepository.findById(mno);
    System.out.println("===========================");
    if(result.isPresent()){ //그래서 if문으로 체크하는 코드작성
      System.out.println(result.get());
    }
  }

//  @Transactional
//  @Test
//  public void testSelect2() {
//    Long mno = 100L;
//    // getOne:: deprecated
//    Optional<Memo> result = memoRepository.getOne(mno);
//    System.out.println("===========================");
//    if(result.isPresent()){ //그래서 if문으로 체크하는 코드작성
//      System.out.println(result.get());
//    }
//  }

  @Test
  public void testUpdate(){
    Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();
    memoRepository.save(memo);
  }

  @Test
  public void testDelete(){
    memoRepository.deleteById(100L);
  }

  @Test
  public void testPageDefault(){
    Pageable pageable = PageRequest.of(0,10); //page 범위 지정 객체
    Page<Memo> result = memoRepository.findAll(pageable); // page 목록 결과 객체
    System.out.println("===================================================");
    System.out.println(result);
    System.out.println("Total Pages:" + result.getTotalPages()); //총 페이지 수
    System.out.println("Total Count:" + result.getTotalElements()); // 총 개별 건수
    System.out.println("Page Number:" + result.getNumber()); // 현재 페이지 번호::0부터시작
    System.out.println("Page Size:" + result.getSize()); // 페이지당 데이터 갯수
    System.out.println("Next Page:" + result.hasNext()); // 다음 페이지 유무
    System.out.println("First Page:" + result.isFirst()); // 첫페이지 인지 확인
    System.out.println("===================================================");
    for(Memo memo:result.getContent()){
      System.out.println(memo);
    };
  }

  @Test
  public void testSort() {
    Sort sort1 = Sort.by("mno").descending(); //페이지 정렬 옵션
    Pageable pageable = PageRequest.of(4,10,sort1); //page 범위 지정 객체
    Page<Memo> result = memoRepository.findAll(pageable); // 해당page 목록 결과 객체
    result.stream().forEach(System.out::println);
  }

  @Test
  public void testSortMulti() {
    Sort sort1 = Sort.by("mno").descending(); //페이지 정렬 옵션
    Sort sort2 = Sort.by("memoText").ascending(); //페이지 정렬 옵션
    Sort sortAll = sort1.and(sort2);

    Pageable pageable = PageRequest.of(4,10,sort1); //page 범위 지정 객체
    Page<Memo> result = memoRepository.findAll(pageable); // 해당page 목록 결과 객체
    result.stream().forEach(System.out::println);
  }

  @Test
  public void testFindByMnoBetweenOrderByMnoDesc() {
    List<Memo> result = memoRepository.findByMnoBetweenOrderByMnoDesc(1L,10L);
    for(Memo m:result){
      System.out.println(m);
    }
  }

  @Test
  public void testFindByMnoBetween() {
    Pageable pageable = PageRequest.of(2,10,Sort.by("mno").descending());
    Page<Memo> result = memoRepository.findByMnoBetween(10L,30L,pageable);
    result.get().forEach(memo-> System.out.println(memo));
    System.out.println(result.getTotalPages());
  }

  @Transactional // 삭제할때 필요함
  @Commit // 삭제할때 필요함
  @Test
  public void testDeleteMemoByMnoLessThan(){
    memoRepository.deleteMemoByMnoLessThan(10L);
  }
  @Test
  public void testGetListDesc() {
    List<Memo> result = memoRepository.getListDesc();
    for(Memo m:result){System.out.println(m);}
  }




}