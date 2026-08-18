package com.example.ex76.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubMemberDTO {
  private String username;
  private String password;
  private String email;
  private String name;
  private boolean fromSocial;
  private LocalDateTime regDate, modDate;
}
