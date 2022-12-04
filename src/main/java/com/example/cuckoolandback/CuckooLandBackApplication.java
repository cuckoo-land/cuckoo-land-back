package com.example.cuckoolandback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CuckooLandBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(CuckooLandBackApplication.class, args);
	}

}
