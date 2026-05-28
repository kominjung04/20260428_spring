package com.example.ex8.security.utill;

import com.nimbusds.jwt.proc.ExpiredJWTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JWTUtilTests {
  private JWTUtil jwtUtil;

  @BeforeEach
  public void testBefore(){
    jwtUtil = new JWTUtil();
  }

  @Test
  public void testEncode() throws Exception {
    String email = "user10@exmple.com";
    String str = jwtUtil.generateToken(email);
    System.out.println(">>>"+str);
  }

  @Test
  public void testValidate() throws Exception {
    String str = jwtUtil.generateToken("user10@example.com");
    Thread.sleep(5000);
    String resultEmail = jwtUtil.validateAndExtract(str);
    System.out.println(">>>" + resultEmail);
//    Exception exception = assertThrows(ExpiredJWTException.class,()->{
//      jwtUtil.validateAndExtract(str);
//    });
//    System.out.println(exception.getMessage());
  }

}