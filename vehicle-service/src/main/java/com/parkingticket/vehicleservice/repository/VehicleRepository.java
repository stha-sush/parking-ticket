package com.parkingticket.vehicleservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parkingticket.vehicleservice.entity.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	Optional<Vehicle> findFirstByPlateNumber(String plateNumber);

	boolean existsByPlateNumber(String plateNumber);
}
