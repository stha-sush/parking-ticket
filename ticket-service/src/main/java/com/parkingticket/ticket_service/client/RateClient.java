package com.parkingticket.ticket_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.parkingticket.ticket_service.dto.RateDTO;

@FeignClient(name = "rate-service")
public interface RateClient {
	@GetMapping("/rates/current")
	RateDTO current();

}
