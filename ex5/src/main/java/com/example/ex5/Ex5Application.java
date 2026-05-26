package com.example.ex5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing  //BasicEntity를 상속 가능하게 함.
public class Ex5Application {

	public static void main(String[] args) {
		SpringApplication.run(Ex5Application.class, args);
		System.out.println("http://localhost:8081/ex5");
	}

}
