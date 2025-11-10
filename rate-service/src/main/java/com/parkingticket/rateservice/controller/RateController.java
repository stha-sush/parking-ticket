package com.parkingticket.rateservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parkingticket.rateservice.entity.RateConfig;
import com.parkingticket.rateservice.service.RateService;

@RestController
@RequestMapping("/rates")
public class RateController {
	@Autowired
	private RateService service;

	@GetMapping("/current")
	public RateConfig current() {
		return service.current();
	}

	// PUT /rates/current
	@PutMapping("/current")
	public RateConfig update(@RequestBody RateConfig data) {
		return service.update(data);
	}
}
