package com.example.ex6.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"movie", "member"})
public class Review extends BasicEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reviewNum;

  @ManyToOne(fetch = FetchType.LAZY) //성능저하를 막기 위한 게으른 연결
  private Movie movie;

  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  private int grade;
  private String text;

  public void changeGrade(int grade) {this.grade = grade;}
  public void changeText(String text) {this.text = text;}
}
