package com.example.ex7.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
@EnableWebSecurity // security 설정 하는 클래스로 선언
public class SecurityConfig {
  // Spring Security 로그인 방식에는 인증 정보가 브라우저의 Cookie와 서버의 HttpSession 조합으로 관리
  // Security에 대한 설정시 메서드의 리턴타입이 중요 ⭐
  // Security는 인증(Authentication)과 권한(Authority)으로 나누고 조합을 할 수 있음.

  // 개방하는 주소 목록
  private static final String[] AUTH_WHITELIST = { //""는 안됨.
      "/css/**", "/js/**", "/images/**", // static 폴더
      "/", "/auth/login","/auth/accessDenied" // 기본적으로 들어와서 사용해야 할 주소
  };

  @Bean //SecurityFilterChain 설정시 모든 시큐리티 설정은 직접 지정해줘야 한다
  protected SecurityFilterChain config(HttpSecurity httpSecurity) throws Exception {
    //csrt(Cross Site Request Forgery) :: 교차사이트요청 위조
    //httpSecurity.csrf(csrf -> csrf.disable());// csrf 사용안할 경우

    // authorizeHttpRequests :: http의 요청에 대한 인증과 권한 처리
    httpSecurity.authorizeHttpRequests(auth -> {
      // 요청하는 각각의 주소에 대하여 접근에 대한 설정을 조합
      // 주소관련 :: requestMatchers(), anyRequest()
      // 인증관련 :: permitAll(), denyAll(), authenticated()
      auth.requestMatchers(AUTH_WHITELIST).permitAll();// 모두 수용
      //auth.anyRequest().denyAll(); // 나머지 주소는 모두 거부해라(인증하더라도 권한없으면 안됨)
      //auth.anyRequest().authenticated(); // 나머지 주소는 인증해라
      //auth.requestMatchers("/sample/**").permitAll(); //특정 주소 관련 하위 주소까지 모두 허용
      //auth.requestMatchers("/sample/manager").permitAll();
      //auth.requestMatchers("/sample/admin").permitAll();

      auth.requestMatchers("/auth/logout/**").authenticated(); //로그된 사용자만 로그아웃 가능

      auth.requestMatchers("/sample/all").permitAll(); //특정 주소만 허용
      // 계정별 로그인해서 인증과 권한을 취득할 경우에 UserDetailsService가 자동으로 동작
      /*auth.requestMatchers("/sample/all").access(
          new WebExpressionAuthorizationManager(
              "hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')"
          )
      );*/


      auth.requestMatchers("/sample/manager").access( // 권한 복수일때
          new WebExpressionAuthorizationManager("hasRole('MANAGER') or hasRole('ADMIN')"));
      auth.requestMatchers("/sample/admin").hasRole("ADMIN");
    });

    // Security 에서 기본 제공하는 "/login","/logout" :: 별도의 컨트롤러와 인증권한 불필요
    //httpSecurity.formLogin(Customizer.withDefaults());
    //httpSecurity.logout(Customizer.withDefaults());

    // 사용자가 별도로 만든 "/login","/logout" :: 컨트롤러와 인증권한, html 파일 반드시 필요
    httpSecurity.formLogin(httpSecurityFormLoginConfigurer -> {
      // controller를 등록할 필요
      httpSecurityFormLoginConfigurer.loginPage("/auth/login")
          //로그인처리를 할때 ClubUserDetailsService를 가지고 인증처리함
          //login은 주소를 임의로 지정함. from태그에서 action에 똑같이 지정해야함
          .loginProcessingUrl("/login");//login 처리 주소 , login.html와 맞춰야함
    });
    httpSecurity.logout(httpLogoutConfigurer -> {
      //logout 페이지는 controller에 사용자가 직접 등록
      httpLogoutConfigurer.logoutUrl("/logout") // logout 처리 주소
          .deleteCookies("JSESSIONID") //쿠기 제거
          .invalidateHttpSession(true) //세션제거
          .clearAuthentication(true); //인증 정보 제거
    });

    httpSecurity.exceptionHandling(httpExceptionHandlingConfigurer -> {
      // 권한이 없을 때 접근 불가 페이지 지정, AUTH_WHITELIST, AuthController 등록
      httpExceptionHandlingConfigurer.accessDeniedPage("/auth/accessDenied");
    });

    return httpSecurity.build();
  }

  //암호화를 하기 위한 빈등록
  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  //암호화 빈을 인증관리자가 채택할 수 있도록 함
//  @Bean
//  public AuthenticationSuccessHandler authenticationSuccessHandler() {
//
//  }
}
