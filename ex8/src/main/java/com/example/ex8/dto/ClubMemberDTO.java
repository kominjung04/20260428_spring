package com.example.ex8.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;

@Data
@Log4j2
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubMemberDTO {
  private String username;
  private String password;
  private String email;
  private String name;
  private boolean FromSocial;
  private LocalDateTime regDate,modDate;
}
