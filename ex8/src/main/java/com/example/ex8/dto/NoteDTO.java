package com.example.ex8.dto;

import com.example.ex8.entity.ClubMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Bag;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoteDTO {
  private Long num;
  private String title;
  private String content;
  private String writerEmail;
  private LocalDateTime regDate, modDate;

}
