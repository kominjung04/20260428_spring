package com.example.ex8maven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Ex8mavenApplication {

	public static void main(String[] args) {
		SpringApplication.run(Ex8mavenApplication.class, args);
		System.out.println("http://localhost:8080/ex8maven/swagger-ui/index.html");
	}

}
