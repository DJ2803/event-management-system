package com.personal.ems.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsWebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // You can specify particular paths here
				.allowedOrigins("http://localhost:3000") // Specify the allowed origins
				.allowedMethods("*") // Allowed HTTP methods
				.allowedHeaders("*") // Allowed headers
				.maxAge(-1) // Disable caching
				.allowCredentials(true); // If you need credentials
	}
}

