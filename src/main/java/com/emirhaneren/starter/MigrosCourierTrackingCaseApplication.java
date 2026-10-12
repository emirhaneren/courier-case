package com.emirhaneren.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"com.emirhaneren"})
@EntityScan(basePackages = {"com.emirhaneren"})
@EnableJpaRepositories(basePackages = {"com.emirhaneren"})
@EnableScheduling
public class MigrosCourierTrackingCaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(MigrosCourierTrackingCaseApplication.class, args);
	}

}
