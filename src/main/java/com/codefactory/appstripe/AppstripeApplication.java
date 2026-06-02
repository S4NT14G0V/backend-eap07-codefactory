package com.codefactory.appstripe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AppstripeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppstripeApplication.class, args);
	}

}
