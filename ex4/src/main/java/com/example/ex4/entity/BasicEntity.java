package com.example.ex4.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass // 다른 entity가 상속받아 사용하는 부모 클래스
@EntityListeners(value = {AuditingEntityListener.class}) // 저장되거나 수정도리때 자동으로 실행
@Getter
abstract class BasicEntity { // abstract :: 상속해서만 사용 가능
  @CreatedDate // 현재 시간 자동 저장
  @Column(name = "regdate",updatable = false) // updatable = false :: 수정 불가
  private LocalDateTime regDate;

  @LastModifiedDate
  @Column(name = "moddate")
  private LocalDateTime modDate;
}
