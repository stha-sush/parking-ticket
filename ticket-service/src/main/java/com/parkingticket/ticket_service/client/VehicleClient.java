package com.parkingticket.ticket_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "vehicle-service")
public interface VehicleClient {
	@GetMapping("/vehicles/search")
	Object findByPlate(@RequestParam("plate") String plate);
}
