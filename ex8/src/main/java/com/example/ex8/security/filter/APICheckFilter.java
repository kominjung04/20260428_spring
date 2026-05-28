package com.example.ex8.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
//APICheckFilter :: 요청한 주소가 서버에 토큰 발행이 필요하는지 확인
public class APICheckFilter extends OncePerRequestFilter {
  private String[] pattern;
  private AntPathMatcher matcher;

  public APICheckFilter(String[] pattern) {
    this.pattern = pattern;
    this.matcher = new AntPathMatcher();
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    log.info("APICheckFilter...............");
    boolean check = false;
    for (int i = 0; i < pattern.length; i++) {
      if (matcher.match(request.getAuthType() + pattern[i], request.getRequestURI())) {
        log.info("matcher:" + matcher.match(request.getAuthType() + pattern[i], request.getRequestURI()));
        check = true;
        break;
      }
      ;
    }
    if (check) { // 요청 주소와 패턴의 주소가 일치하는 경우
      if (checkAuthHeader(request)) { // 토큰이 있는 경우
        // request :: 클라이언트 요청,response :: 서버가 응답
        filterChain.doFilter(request, response); // 해당 필터가 확인 후 다음 과정으로 넘김
        return;
      } else { // 토큰이 없는 경우
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        JSONObject jsonObj = new JSONObject();
        String msg = "FAIL CHECK API TOKEN";
        jsonObj.put("code", "403");
        jsonObj.put("msg", msg);
        PrintWriter out = response.getWriter();
        out.println(jsonObj);
        return;
      }
    } else { // 요청 주소와 패턴의 주소가 불일치 하는 경우
      log.info("Request doesn't match");
    }

    filterChain.doFilter(request, response); // 필터링만 하고 나머지는 원래 루틴으로 흘려보낸다.
  }

  private boolean checkAuthHeader(HttpServletRequest request) {
    boolean checkResult = false;
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null && StringUtils.hasText(authHeader)) {
      log.info("authHeader:" + authHeader);
      if (authHeader.startsWith("Bearer ")) {
        checkResult = true;
      }
    }
    return checkResult;
  }
}
