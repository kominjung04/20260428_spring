package com.example.ex8.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // 메서드 단위 보안 설정, AOP
public class SecurityConfig {
  private static final String[] AUTH_WHITELIST = { // ""는 안됨.
      "/", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html"
  };
  private static final String[] AUTH_CHECKLIST = {
      "/notes/**/*"
  };

  @Bean
  protected SecurityFilterChain config(HttpSecurity httpSecurity) throws Exception {

    httpSecurity.csrf(csrf -> csrf.disable());// csrf 사용안할 경우

    httpSecurity.authorizeHttpRequests(auth -> {
      auth.requestMatchers(AUTH_WHITELIST).permitAll();
      auth.anyRequest().permitAll();
    });


    return httpSecurity.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}