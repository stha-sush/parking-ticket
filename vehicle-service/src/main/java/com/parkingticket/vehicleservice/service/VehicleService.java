package com.parkingticket.vehicleservice.service;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exception.VehicleAlreadyExistsException;
import com.parkingticket.vehicleservice.entity.Vehicle;
import com.parkingticket.vehicleservice.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService {

	private final VehicleRepository repo;
	// private final MqttGateway mqttGateway;

	public List<Vehicle> findAll() {
		return repo.findAll();
	}

	public Vehicle findById(@NonNull String id) {
		return repo.findById(id).orElseThrow(() -> new RuntimeException("Vehicle not found"));
	}

	public Vehicle create(@NonNull Vehicle v) {
		if (v.getPlateNumber() == null || v.getPlateNumber().isBlank()) {
			throw new RuntimeException("plateNumber is required");
		}
		if (repo.findFirstByPlateNumber(v.getPlateNumber()).isPresent()) {
			throw new VehicleAlreadyExistsException(v.getPlateNumber());
		}

		Vehicle saved = repo.save(v);
		// mqttGateway.senToMqtt(saved.getId() + "", "vehicle-topic");
		return saved;
	}

	@Transactional
	public Vehicle update(@NonNull String id, @NonNull Vehicle data) {
		Vehicle v = findById(id);
		v.setVehicleType(data.getVehicleType());
		v.setOwnerName(data.getOwnerName());
		return repo.save(v);
	}

	@Transactional
	public void delete(@NonNull String id) {
		repo.deleteById(id);
	}

	public Vehicle findByPlate(@NonNull String plate) {
		return repo.findFirstByPlateNumber(plate)
				.orElseThrow(() -> new RuntimeException("Vehicle not found for plate"));
	}
}
