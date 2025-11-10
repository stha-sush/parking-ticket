package com.parkingticket.ticket_service.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parkingticket.ticket_service.client.EnterDTO;
import com.parkingticket.ticket_service.client.ExitDTO;
import com.parkingticket.ticket_service.client.RateClient;
import com.parkingticket.ticket_service.client.RateDTO;
import com.parkingticket.ticket_service.client.VehicleClient;
import com.parkingticket.ticket_service.entity.Ticket;
import com.parkingticket.ticket_service.repository.TicketRepository;

@Service
public class TicketService {
	@Autowired
	private TicketRepository repo;
	@Autowired
	private VehicleClient vehicleClient;
	@Autowired
	private RateClient rateClient;

	private final AtomicLong seq = new AtomicLong(1);

	public List<Ticket> openTickets() {
		return repo.findAll().stream().filter(t -> "OPEN".equals(t.getStatus())).toList();
	}

	public Ticket findById(Long id) {
		return repo.findById(id).orElseThrow(() -> new RuntimeException("Ticket not found"));
	}

	public Ticket enter(EnterDTO dto) {
		if (dto.getPlateNumber() == null || dto.getPlateNumber().isBlank())
			throw new RuntimeException("plateNumber is required");

		// ensure no open ticket for this plate
		repo.findFirstByPlateNumberAndStatus(dto.getPlateNumber(), "OPEN").ifPresent(t -> {
			throw new RuntimeException("Open ticket already exists for this plate");
		});

		// validate vehicle exists
		try {
			vehicleClient.findByPlate(dto.getPlateNumber());
		} catch (Exception e) {
			throw new RuntimeException("Vehicle not found for plate: " + dto.getPlateNumber());
		}

		Ticket t = new Ticket();
		t.setTicketNumber(String.format("T-%04d", seq.getAndIncrement()));
		t.setPlateNumber(dto.getPlateNumber());
		t.setStatus("OPEN");
		t.setEntryTime(LocalDateTime.now());
		return repo.save(t);
	}

	public Ticket exit(ExitDTO dto) {
		Ticket t;
		if (dto.getTicketNumber() != null && !dto.getTicketNumber().isBlank()) {
			t = repo.findFirstByTicketNumber(dto.getTicketNumber())
					.orElseThrow(() -> new RuntimeException("Ticket not found"));
		} else if (dto.getPlateNumber() != null && !dto.getPlateNumber().isBlank()) {
			t = repo.findFirstByPlateNumberAndStatus(dto.getPlateNumber(), "OPEN")
					.orElseThrow(() -> new RuntimeException("Open ticket not found for plate"));
		} else {
			throw new RuntimeException("Provide ticketNumber or plateNumber");
		}

		if (!"OPEN".equals(t.getStatus())) {
			throw new RuntimeException("Ticket is already closed");
		}

		// fetch current rate
		RateDTO rate = rateClient.current();

		// compute fee
		LocalDateTime now = LocalDateTime.now();
		long minutes = Duration.between(t.getEntryTime(), now).toMinutes();
		long grace = rate.getGraceMinutes() != null ? rate.getGraceMinutes() : 0;
		long billable = Math.max(0, minutes - (rate.getGraceMinutes() != null ? rate.getGraceMinutes() : 0));

		double fee;
		if ("FLAT".equalsIgnoreCase(rate.getPricingModel())) {
			fee = rate.getFlatAmount() != null ? rate.getFlatAmount() : 0.0;
		} else {
			// HOURLY
			double hours = Boolean.TRUE.equals(rate.getRoundUpToHour()) ? Math.ceil(billable / 60.0)
					: Math.max(1, Math.floor(billable / 60.0));
			fee = (rate.getPerHour() != null ? rate.getPerHour() : 0.0) * hours;
		}

		t.setExitTime(LocalDateTime.now());
		t.setCalculatedFee(fee);
		t.setStatus("CLOSED");
		return repo.save(t);
	}
}
