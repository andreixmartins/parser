package com.ef;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Parser {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Parser.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}
}
