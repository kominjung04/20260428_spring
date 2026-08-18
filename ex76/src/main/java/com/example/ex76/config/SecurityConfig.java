package com.example.ex76.config;

import com.example.ex76.security.handler.CustomAccessDeniedHandler;
import com.example.ex76.security.handler.CustomAuthenticationFailureHandler;
import com.example.ex76.security.handler.CustomLoginSuccessHandler;
import com.example.ex76.security.handler.CustomLogoutSuccessHandler;
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
@EnableMethodSecurity(prePostEnabled = true) //메서드 단위 보안 설정, AOP
public class SecurityConfig {
  private static final String[] AUTH_WHITELIST = { // ""는 안됨.
      "/css/**", "/js/**", "/images/**", "/assets/**", // static 폴더
      "/", "/auth/login", "/auth/authenticationFailure"
  };
  // /주소/** :: 하위복수주소 뿐만 아니라 쿼리까지 포함.
  private static final String[] AUTHENTICATED_LIST = {
      "/auth/accessDenied", "/auth/modify", "/auth/logout/**", "/movie/**"
      , "/member/**"
  };

  @Bean
  protected SecurityFilterChain config(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.authorizeHttpRequests(auth -> {
      auth.requestMatchers(AUTH_WHITELIST).permitAll();
      auth.requestMatchers(AUTHENTICATED_LIST).authenticated();
      //auth.anyRequest().denyAll();
    });

    httpSecurity.formLogin(httpSecurityFormLoginConfigurer -> {
      httpSecurityFormLoginConfigurer
          .loginPage("/auth/login")
          .loginProcessingUrl("/login")
          .successHandler(authenticationSuccessHandler());
    });
    httpSecurity.logout(httpLogoutConfigurer -> {
      httpLogoutConfigurer.logoutUrl("/logout")
          .deleteCookies("JSESSIONID")
          .invalidateHttpSession(true)
          .clearAuthentication(true)
          .logoutSuccessHandler(getLogoutSuccessHandler());
    });

    httpSecurity.exceptionHandling(httpExceptionHandlingConfigurer -> {
      // 권한이 없을 때 접근 불가 페이지 지정, AUTH_WHITELIST, AuthController 등록 필
      httpExceptionHandlingConfigurer
          //.accessDeniedPage("/auth/accessDenied")
          .accessDeniedHandler(getAccessDeniedHandler());
    });

    // Social 로그인
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
    return new CustomLogoutSuccessHandler();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean // 성공했을 때 처리하는 객체
  public AuthenticationSuccessHandler authenticationSuccessHandler() {
    return new CustomLoginSuccessHandler(passwordEncoder());
  }
}
