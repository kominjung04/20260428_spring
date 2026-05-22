package com.example.ex7.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecurityConfigTests {
@Autowired
  private PasswordEncoder passwordEncoder;

@Test
  public void testEncoder() {
  String password = "1111";
  String enPassword = passwordEncoder.encode(password);
  System.out.println("password,enPassword: " + password + "/" + enPassword);
  boolean matches = passwordEncoder.matches(password, enPassword);
  System.out.println("matches :" + matches);
}


}