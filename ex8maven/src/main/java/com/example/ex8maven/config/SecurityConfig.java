package com.example.ex8maven.config;

import com.example.ex8maven.security.filter.APICheckFilter;
import com.example.ex8maven.security.filter.APILoginFilter;
import com.example.ex8maven.security.filter.CORSFilter;
import com.example.ex8maven.security.handler.APILoginFailHandler;
import com.example.ex8maven.security.util.JWTUtil;
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
@EnableMethodSecurity(prePostEnabled = true) //메서드 단위 보안 설정, AOP
public class SecurityConfig {
  private static final String[] AUTH_WHITELIST = {
      // "" 불가. /notes/**는 하위 주소 복수개와 쿼리까지 모두 사용(AntPathMathcher에도 가능)
      "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html",
      "/notes/**", "/member/**"  // 개방해놓고 API 체크할 예정,

  };
  private static final String[] API_CHECKLIST = {
      "/notes/**", "/member/**"  // API 체크하고 토큰 여부 확인할 주소
  };

  @Bean
  protected SecurityFilterChain config(HttpSecurity httpSecurity) throws Exception {

    httpSecurity.csrf(csrf -> csrf.disable());// csrf 사용안할 경우

    //httpSecurity.cors(Customizer.withDefaults()); //기본값 사용
    /*httpSecurity.cors(cors -> cors.disable()); // cors 필터를 사용함으로 기본값 disable
    httpSecurity.addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class);*/

    httpSecurity.authorizeHttpRequests(auth -> {
      auth.requestMatchers(AUTH_WHITELIST).permitAll();
      auth.anyRequest().denyAll();
    });

    // filter 적용(apiCheckFilter, apiLoginFilter)
    httpSecurity.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);
    httpSecurity.addFilterBefore(
        apiLoginFilter(httpSecurity.getSharedObject(AuthenticationConfiguration.class))
        , UsernamePasswordAuthenticationFilter.class
    );

    return httpSecurity.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public APICheckFilter apiCheckFilter() {
    return new APICheckFilter(API_CHECKLIST, jwtUtil());
  }

  @Bean
  public APILoginFilter apiLoginFilter(AuthenticationConfiguration ac) throws Exception {
    APILoginFilter apiLoginFilter = new APILoginFilter("/api/login", jwtUtil());
    apiLoginFilter.setAuthenticationManager(ac.getAuthenticationManager());
    apiLoginFilter.setAuthenticationFailureHandler(apiLoginFailHandler());
    return apiLoginFilter;
  }

  @Bean
  public APILoginFailHandler apiLoginFailHandler() {
    return new APILoginFailHandler();
  }

  @Bean
  public JWTUtil jwtUtil(){
    return new JWTUtil();
  }

  @Bean
  public CORSFilter corsFilter() {
    return new CORSFilter();
  }
}
