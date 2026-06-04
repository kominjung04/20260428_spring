package com.example.ex8maven.security.filter;

import com.example.ex8maven.security.dto.ClubAuthMemberDTO;
import com.example.ex8maven.security.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

@Log4j2
public class APILoginFilter extends AbstractAuthenticationProcessingFilter {
  private JWTUtil jwtUtil;

  public APILoginFilter(String defaultFilterProcessesUrl, JWTUtil jwtUtil) {
    super(defaultFilterProcessesUrl);
    this.jwtUtil = jwtUtil;
  }

  @Override
  public @Nullable Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    log.info("APILoginFilter attemptAuthentication");
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    if(email == null || email.isEmpty() || password == null || password.isEmpty()) {
      throw new BadCredentialsException("Email cannot be null");
    }
    // ClubUserDetailsService의 loadUserByUsername()를 호출하고 인증
    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, password);
    return getAuthenticationManager().authenticate(auth); // principal에 인증을 남김.
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    Object principal = authResult.getPrincipal();
    log.info("successfulAuthentication principal: " + principal);
    String email = ((ClubAuthMemberDTO) principal).getEmail();
    String password = ((ClubAuthMemberDTO) principal).getPassword();
    String token = null;
    try {
      token = jwtUtil.generateToken(email);
      response.setContentType("text/plain");
      response.getOutputStream().write(token.getBytes());
      log.info("successfulAuthentication generated token: " + token);
    } catch (Exception e) {e.printStackTrace();}
  }
}
