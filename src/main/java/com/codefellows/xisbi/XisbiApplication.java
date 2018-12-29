package com.codefellows.xisbi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class XisbiApplication {

	public static void main(String[] args) {
		SpringApplication.run(XisbiApplication.class, args);
		System.out.println("http://localhost:8080/");
	}

}

