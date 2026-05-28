package com.example.ex8.config;


import com.example.ex8.security.filter.APICheckFilter;
import com.example.ex8.security.filter.APILoginFilter;
import com.example.ex8.security.handler.APILoginFailHandler;
import com.example.ex8.security.utill.JWTUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // 메서드 단위 보안 설정, AOP
public class SecurityConfig {
  private static final String[] AUTH_WHITELIST = { // ""는 안됨.
      "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html"
  };
  private static final String[] API_CHECKLIST = {
      "/notes/**/*"
  };

  @Bean
  protected SecurityFilterChain config(HttpSecurity httpSecurity) throws Exception {

    httpSecurity.csrf(csrf -> csrf.disable());// csrf 사용안할 경우

    httpSecurity.authorizeHttpRequests(auth -> {
      auth.requestMatchers(AUTH_WHITELIST).permitAll();
      auth.anyRequest().denyAll();
    });

    // filter 적용(apiCheckFilter,apiLoginFilter)
    httpSecurity.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);
    httpSecurity.addFilterBefore(
        apiLoginFilter(httpSecurity.getSharedObject(AuthenticationConfiguration.class))
        ,UsernamePasswordAuthenticationFilter.class);


    return httpSecurity.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public APICheckFilter apiCheckFilter() {
    return new APICheckFilter(API_CHECKLIST);
  }

  @Bean
  public APILoginFilter apiLoginFilter(AuthenticationConfiguration ac) throws Exception {
    APILoginFilter apiLoginFilter = new APILoginFilter("/api/login",jwtUtil());
    apiLoginFilter.setAuthenticationManager(ac.getAuthenticationManager());
    apiLoginFilter.setAuthenticationFailureHandler(apiLoginFailHandler());
    return apiLoginFilter;
  }

  @Bean
  public APILoginFailHandler  apiLoginFailHandler() {
    return new APILoginFailHandler();
  }

  @Bean
  public JWTUtil jwtUtil() {
    return new JWTUtil();
  }

}