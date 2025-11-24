package com.parkingticket.vehicleservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parkingticket.vehicleservice.config.MqttGateway;
import com.parkingticket.vehicleservice.entity.Vehicle;
import com.parkingticket.vehicleservice.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService {

	@Autowired
	private final VehicleRepository repo;
	private final MqttGateway mqttGateway;

	public List<Vehicle> findAll() {
		return repo.findAll();
	}

	public Vehicle findById(String id) {
		return repo.findById(id).orElseThrow(() -> new RuntimeException("Vehicle not found"));
	}

	public Vehicle create(Vehicle v) {
		if (v.getPlateNumber() == null || v.getPlateNumber().isBlank()) {
			throw new RuntimeException("plateNumber is required");
		}
		if (repo.existsByPlateNumber(v.getPlateNumber())) {
			throw new RuntimeException("Vehicle already exists for plate");
		}
		Vehicle saved = repo.save(v);
		// Publish vehicle ID to MQTT
		mqttGateway.senToMqtt(saved.getId() + "", "vehicle-topic");

		return saved;
	}

	@Transactional
	public Vehicle update(String id, Vehicle data) {
		Vehicle v = findById(id);
		v.setVehicleType(data.getVehicleType());
		v.setOwnerName(data.getOwnerName());
		return repo.save(v);
	}

	@Transactional
	public void delete(String id) {
		repo.delete(findById(id));
	}

	public Vehicle findByPlate(String plate) {
		return repo.findFirstByPlateNumber(plate)
				.orElseThrow(() -> new RuntimeException("Vehicle not found for plate"));
	}

}
