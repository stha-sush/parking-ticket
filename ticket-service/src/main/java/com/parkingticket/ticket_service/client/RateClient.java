package com.parkingticket.ticket_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "rate-service")
public interface RateClient {
	@GetMapping("/rates/current")
	RateDTO current();

}
