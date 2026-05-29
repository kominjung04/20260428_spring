package com.example.ex7.config;

import com.example.ex7.security.handler.CustomAccessDeniedHandler;
import com.example.ex7.security.handler.CustomAuthenticationFailureHandler;
import com.example.ex7.security.handler.CustomLoginSuccessHandler;
import com.example.ex7.security.handler.getLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // 메서드 단위 보안 설정, AOP
public class SecurityConfig {
  private static final String[] AUTH_WHITELIST = { // ""는 안됨.
      "/css/**", "/js/**", "/images/**", // static 폴더
      "/", "/auth/login"
  };
  // /주소/** :: 하위복수주소 뿐만 아니라 쿼리까지 포함.
  private static final String[] AUTH_LOGIN_WHITELIST = {
      "/auth/accessDenied","/auth/modify","/auth/logout/**"
  };

  @Bean
  protected SecurityFilterChain config(HttpSecurity httpSecurity) throws Exception {

    //httpSecurity.csrf(csrf -> csrf.disable());// csrf 사용안할 경우

    httpSecurity.authorizeHttpRequests(auth -> {
      //auth.anyRequest().permitAll(); //모든 사용자 접근 허용
      //auth.anyRequest().denyAll(); //모든 사용자 접근 차단
      //auth.anyRequest().authenticated(); //로그인한 사용자만 접근 가능

      auth.requestMatchers(AUTH_WHITELIST).permitAll();
      //auth.requestMatchers("/sample/**").permitAll();
      //auth.requestMatchers("/sample/manager").permitAll();
      //auth.requestMatchers("/sample/admin").permitAll();

      auth.requestMatchers(AUTH_LOGIN_WHITELIST).authenticated();
      //auth.requestMatchers("/auth/logout/**").authenticated();

      auth.requestMatchers("/sample/all").permitAll();
      /*auth.requestMatchers("/sample/all").access(
          new WebExpressionAuthorizationManager(
              "hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')" //hasRole:특정역할 필요
          )
      );*/
      auth.requestMatchers("/sample/manager").access(
          new WebExpressionAuthorizationManager("hasRole('MANAGER') or hasRole('ADMIN')"));
      auth.requestMatchers("/sample/admin").hasRole("ADMIN");
    });

    //httpSecurity.formLogin(Customizer.withDefaults());
    //httpSecurity.logout(Customizer.withDefaults());

    // Spring Security 로그인 방식에는 인증 정보가 브라우저의 Cookie와 서버의 HttpSession 조합으로 관리
    httpSecurity.formLogin(httpSecurityFormLoginConfigurer -> {
      httpSecurityFormLoginConfigurer.loginPage("/auth/login")
          .loginProcessingUrl("/login").successHandler(authenticationSuccessHandler());
    });
    httpSecurity.logout(httpLogoutConfigurer -> {
      httpLogoutConfigurer.logoutUrl("/logout")
          .deleteCookies("JSESSIONID")
          .invalidateHttpSession(true)
          .clearAuthentication(true)
          .logoutSuccessHandler(getLogoutSuccessHandler());
    });

    httpSecurity.logout(httpLogoutConfigurer -> {
      httpLogoutConfigurer
          .logoutUrl("/logout")
          .logoutSuccessHandler(getLogoutSuccessHandler());
    });

    /*
    exceptionHanding 전체 로그인 공통 적용
    oauth2Login() 소셜 로그인 전용
    formLogin() 일반 로그인 적용
     */

    httpSecurity.exceptionHandling(httpExceptionHandlingConfigurer -> {
      // 권한이 없을 때 접근 불가 페이지 지정, AUTH_WHITELIST, AuthController 등록 필
      httpExceptionHandlingConfigurer
          //.accessDeniedPage("/auth/accessDenied")
          .accessDeniedHandler(getAccessDeniedHandler());
    });

    // 소셜로그인
    httpSecurity.oauth2Login(httpOAuth2LoginConfigurer -> {
      httpOAuth2LoginConfigurer.successHandler(authenticationSuccessHandler());
    });

    httpSecurity.rememberMe(httpRememberMeConfigurer -> {
      httpRememberMeConfigurer.tokenValiditySeconds(60 * 60 * 24 * 7); //일주일

    });

    return httpSecurity.build();
  }

  @Bean
  public AccessDeniedHandler getAccessDeniedHandler() {
    return new CustomAccessDeniedHandler();
  }

  @Bean
  public AuthenticationFailureHandler getAuthenticationFailureHandler() {
    return new CustomAuthenticationFailureHandler();
  }

  @Bean
  public LogoutSuccessHandler getLogoutSuccessHandler() {
    return new getLogoutSuccessHandler();
  }

  @Bean
  PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

  @Bean // 성공했을 때 처리하는 객체
  public AuthenticationSuccessHandler authenticationSuccessHandler() {
    return new CustomLoginSuccessHandler(passwordEncoder());
  }
}