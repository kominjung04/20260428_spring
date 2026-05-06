package com.example.ex4.entity;

import jakarta.persistence.*;

@Entity
public class Guestbook extends BasicEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long gno;

  @Column(nullable = false,length = 100)
  private String title;

  @Column(nullable = false,length = 1500)
  private String content;

  @Column(nullable = false,length = 50)
  private String writer;

  public void changeTitle(String title) {
    this.title = title;
  }

  public void changeContent(String content) {
    this.content = content;
  }

}
