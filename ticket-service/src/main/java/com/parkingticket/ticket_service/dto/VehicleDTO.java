package com.parkingticket.ticket_service.dto;

import lombok.Data;

@Data
public class VehicleDTO {
	private Long id;
	private String plateNumber;
	private String vehicleType;
	private String ownerName;
}
