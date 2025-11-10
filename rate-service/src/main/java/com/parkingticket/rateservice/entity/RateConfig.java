package com.parkingticket.rateservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class RateConfig {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String pricingModel = "HOURLY";

	// used when pricingModel = HOURLY
	private Double perHour = 100.0;

	// optional grace period
	private Integer graceMinutes = 0;

	// if true, 61 minutes = 2 hours
	private Boolean roundUpToHour = true;

	// used when pricingModel = FLAT
	private Double flatAmount = 0.0;
}
