package com.example.ex2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Data: 자동 getter,setter와 toString 만들어짐
//@Builder: new를 생성할 필요 X
//@AllArgsConstructor: 모든 필드를 매개변수로 받는 생성자 생성
//@@NoArgsConstructor: 매개변수 없는 기본 생성자 생성
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
  private String id;
  private String pass;

}
