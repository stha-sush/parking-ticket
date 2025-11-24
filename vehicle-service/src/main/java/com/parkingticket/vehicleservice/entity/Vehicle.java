package com.parkingticket.vehicleservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document

public class Vehicle {
	@Id
	private String id;

	@Indexed(unique = true)
	private String plateNumber;

	@Indexed
	private String vehicleType;
	// Type of vehicles will be car, bike, truck

	private String ownerName;
}
