package com.emirhaneren.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
	
	@Bean
	public OpenAPI swaggerConfiguration() {
		return new OpenAPI()
				.info(new Info()
						.title("Migros Courier Tracking Case")
						.description("Kurye konumunu işleme ve total kurye mesafesini hesaplama projesi. Detaylı bilgi için -> README")
						.version("1.0.0"));
	}
}
