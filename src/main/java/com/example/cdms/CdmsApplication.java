package com.example.cdms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CdmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CdmsApplication.class, args);
	}

}
