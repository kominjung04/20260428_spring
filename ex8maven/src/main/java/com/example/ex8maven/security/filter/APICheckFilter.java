package com.example.ex8maven.security.filter;

import com.example.ex8maven.security.util.JWTUtil;
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
public class APICheckFilter extends OncePerRequestFilter {
  private String[] pattern;
  private AntPathMatcher matcher;
  private JWTUtil jwtUtil;

  public APICheckFilter(String[] pattern, JWTUtil jwtUtil) {
    this.pattern = pattern;
    this.matcher = new AntPathMatcher();
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    log.info("APICheckFilter........................");

    boolean check = false;
    for (int i = 0; i < pattern.length; i++) {
      // 사용자가 요청한 주소와 checkFilter에 등록된 목록중에 같은 것이 있느냐?
      log.info(">>>"+request.getContextPath() + pattern[i]);
      log.info(">>>"+request.getRequestURI());
      log.info("matcher: " + matcher.match(request.getContextPath() + pattern[i], request.getRequestURI()));
      if (matcher.match(request.getContextPath() + pattern[i], request.getRequestURI())) {
        check = true; // 있으면 true
        break;
      }
    }
    if (check) { // 요청 주소와 패턴의 주소가 일치하는 경우
      if (checkAuthHeader(request)) {  // 토큰이 있는 경우
        log.info("token Pass!");
        filterChain.doFilter(request, response); // 해당필터 확인후 다음 과정으로 넘김.
        return;
      } else {                        // 토큰이 없는 경우
        log.info("token Fail!");
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
    } else {     // 요청 주소와 패턴의 주소가 불일치 하는 경우, JWT 체크 안함.
      log.info("Request does not match protected patterns. Skipping JWT check.");
    }

    filterChain.doFilter(request, response); // 필터링만 하고 나머지는 원래 루틴으로 흘려보낸다.
  }

  // 주소를 검증하면서 header에 포함된 Authorization의 토큰에서 email을 추출할 경우 true
  private boolean checkAuthHeader(HttpServletRequest request) {
    boolean checkResult = false;
    String authHeader = request.getHeader("Authorization");

    if (authHeader != null && StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
      log.info("authHeader:" + authHeader);
      try {
        String email = jwtUtil.validateAndExtract(authHeader.substring(7));
        log.info("checkAuthHeader email:" + email);
        checkResult = email.length() > 0;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return checkResult;
  }
}
