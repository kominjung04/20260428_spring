package com.example.ex7.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Log4j2
@Getter
@Setter
@ToString
public class ClubAuthMemberDTO extends User {
  // User를 상속받았기에 세션으로 저장가능, session 정보로 저장하는 객체
  private String email;
  private String password;
  private String name;
  private boolean fromSocial;

  // Spring Security 로그인 방식에는 인증 정보가 브라우저의 Cookie와 서버의 HttpSession 조합으로 관리
  public ClubAuthMemberDTO(String username, @Nullable String password, boolean fromSocial,Collection<? extends GrantedAuthority> authorities, String email,String name) {
    super(username, password, authorities); //이 정보는 반드시 User로 전송
    this.email = email;
    this.password = password;
    this.fromSocial = fromSocial;
    this.name = name;

  }
}
