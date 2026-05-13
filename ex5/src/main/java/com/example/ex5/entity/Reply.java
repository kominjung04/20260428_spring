package com.example.ex5.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"board","commenter"}) //ToString에서 board,commenter 제외
public class Reply extends BasicEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long rno;

  private String text;
  @ManyToOne(fetch = FetchType.LAZY)
  private Member commenter;

  @ManyToOne(fetch = FetchType.LAZY)
  private Board board;

  public void changeText(String text) {this.text = text;}
}
