package com.example.ex7.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebSecurity // security 설정 하는 클래스로 선언
public class SecurityConfig {
  // Spring Security 로그인 방식에는 인증 정보가 브라우저의 Cookie와 서버의 HttpSession 조합으로 관리
  // Security에 대한 설정시 메서드의 리턴타입이 중요 ⭐
  // Security는 인증(Authentication)과 권한(Authority)으로 나누고 조합을 할 수 있음.

  // 개방하는 주소 목록
  private static final String[] AUTH_WHITELIST = {
      "/","/auth/login"
  };

  @Bean //SecurityFilterChain 설정시 모든 시큐리티 설정은 직접 지정해줘야 한다
  protected SecurityFilterChain config(HttpSecurity httpSecurity) throws Exception {

    // authorizeHttpRequests :: http의 요청에 대한 처리
    httpSecurity.authorizeHttpRequests(auth -> {
      auth.requestMatchers(AUTH_WHITELIST).permitAll();// 개방주소 등록
      auth.anyRequest().authenticated(); // 나머지 주소는 인증
    });

    // 로그인 :: security 제공하는 Default 사용 :: 별도의 컨트롤러와 인증권한 불필요
    //httpSecurity.formLogin(Customizer.withDefaults());

    // 로그인 :: 화면을 사용자가 별도로 만들 때
    httpSecurity.formLogin(httpSecurityFormLoginConfigurer -> {
      httpSecurityFormLoginConfigurer.loginPage("/login"); // controller를 등록할 필요
    });


    return httpSecurity.build();
  }
}
