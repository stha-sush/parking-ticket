package com.parkingticket.vehicleservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.parkingticket.vehicleservice.entity.Vehicle;

public interface VehicleRepository extends MongoRepository<Vehicle, String> {
	Optional<Vehicle> findFirstByPlateNumber(String plateNumber);

	boolean existsByPlateNumber(String plateNumber);
}
