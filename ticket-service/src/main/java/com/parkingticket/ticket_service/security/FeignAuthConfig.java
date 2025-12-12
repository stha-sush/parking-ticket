package com.parkingticket.ticket_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

@Configuration
public class FeignAuthConfig {

	@Bean
	RequestInterceptor requestInterceptor() {
		return requestTemplate -> {
			String token = JwtTokenContext.getToken();
			if (token != null) {
				requestTemplate.header("Authorization", "Bearer " + token);
			}
		};
	}
}
