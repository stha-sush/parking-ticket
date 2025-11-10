package com.parkingticket.ticket_service.entity;

import java.time.LocalDateTime;

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
public class Ticket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String ticketNumber; // e.g., "T-001"

	@Column(nullable = false)
	private String plateNumber;

	@Column(nullable = false)
	private String status; // OPEN, CLOSED

	@Column(nullable = false)
	private LocalDateTime entryTime;

	private LocalDateTime exitTime; // set on close

	private Double calculatedFee; // set on close
}