package com.parkingticket.ticket_service.entity;

import java.time.LocalDateTime;

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
public class Ticket {
	@Id
	private String id;

	@Indexed
	private String ticketNumber; // e.g., "T-001"

	private String plateNumber;

	private String status; // OPEN, CLOSED

	private LocalDateTime entryTime;

	private LocalDateTime exitTime; // set on close

	private Double calculatedFee; // set on close
}