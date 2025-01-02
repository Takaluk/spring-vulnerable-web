package com.example.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SpringVulnerableWebApplication extends SpringBootServletInitializer{
	
    // 이 부분 추가
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SpringVulnerableWebApplication.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SpringVulnerableWebApplication.class, args);
	}
}