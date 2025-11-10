package com.parkingticket.ticket_service.client;

import lombok.Data;

@Data
public class RateDTO {
	private String pricingModel;
	private Double perHour;
	private Integer graceMinutes;
	private Boolean roundUpToHour;
	private Double flatAmount;
}
