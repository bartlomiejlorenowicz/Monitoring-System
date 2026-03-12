package com.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SecurityEventPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityEventPlatformApplication.class, args);
	}

}
