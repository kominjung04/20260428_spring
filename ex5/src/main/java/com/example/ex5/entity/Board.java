package com.example.ex5.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "writer")
public class Board extends BasicEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long bno;

  private String title;
  private String content;

  //fetch = FetchType.EAGER :: (기본) 조회할 때 join처럼 같이 가져옴
  //fetch = FetchType.LAZY :: 처음에 안가져오고 필요시 추가 조회
  @ManyToOne(fetch = FetchType.LAZY)
  private Member writer;//board가 member를 참조

  public void changeTitle(String title) { this.title = title;}
  public void changContent(String content){ this.content = content;};
}
