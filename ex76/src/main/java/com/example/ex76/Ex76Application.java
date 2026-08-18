package com.example.ex76;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Ex76Application {

	public static void main(String[] args) {
		SpringApplication.run(Ex76Application.class, args);
		System.out.println("http://localhost:8080/ex76");
	}

}
