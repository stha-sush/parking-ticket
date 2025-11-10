package com.parkingticket.vehicleservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.parkingticket.vehicleservice.entity.Vehicle;
import com.parkingticket.vehicleservice.service.VehicleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor

public class VehicleController {
	private final VehicleService service;

	@GetMapping({ "/", "" })
	public List<Vehicle> all() {
		return service.findAll();
	}

	@GetMapping("/{id}")
	public Vehicle one(@PathVariable Long id) {
		return service.findById(id);
	}

	@PostMapping
	public Vehicle create(@RequestBody Vehicle v) {
		return service.create(v);
	}

	@PutMapping("/{id}")
	public Vehicle update(@PathVariable Long id, @RequestBody Vehicle v) {
		return service.update(id, v);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}

	@GetMapping("/search")
	public Vehicle byPlate(@RequestParam String plate) {
		return service.findByPlate(plate);
	}
}
