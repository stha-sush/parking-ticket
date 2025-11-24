package com.parkingticket.ticket_service.dto;

import lombok.Data;

@Data
public class RateDTO {
	private String pricingModel;
	private Double perHour;
	private Double bikePerHour; // for BIKE
	private Double carPerHour; // for CAR
	private Double truckPerHour; // for TRUCK
	private Integer graceMinutes;
	private Boolean roundUpToHour;
	private Double flatAmount;
}
