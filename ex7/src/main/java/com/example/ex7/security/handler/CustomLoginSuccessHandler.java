package com.example.ex7.security.handler;

import com.example.ex7.security.dto.ClubAuthMemberDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
  private PasswordEncoder passwordEncoder;
  private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  public CustomLoginSuccessHandler(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    ClubAuthMemberDTO clubAuthMemberDTO = (ClubAuthMemberDTO) authentication.getPrincipal();
    if (clubAuthMemberDTO.isFromSocial() && passwordEncoder.matches("1", clubAuthMemberDTO.getPassword())) {
      redirectStrategy.sendRedirect(request, response, "/auth/modify");
      return;
    }
    Collection<GrantedAuthority> authorities = clubAuthMemberDTO.getAuthorities();
    List<String> roles = authorities.stream().map(
        grantedAuthority -> grantedAuthority.getAuthority()
    ).sorted().collect(Collectors.toList());
    for (int i = 0; i < roles.size(); i++) {
      String forward = null;
      if (roles.get(i).contains("ROLE_ADMIN")) forward = "/sample/admin";
      else if (roles.get(i).contains("ROLE_MANAGER")) forward = "/sample/manager";
      else forward = "/sample/all";
      redirectStrategy.sendRedirect(request,response,forward);
      break;
    }
  }
}
