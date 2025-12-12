package com.parkingticket.ticket_service.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parkingticket.ticket_service.client.RateClient;
import com.parkingticket.ticket_service.client.VehicleClient;
import com.parkingticket.ticket_service.dto.EnterDTO;
import com.parkingticket.ticket_service.dto.ExitDTO;
import com.parkingticket.ticket_service.dto.RateDTO;
import com.parkingticket.ticket_service.dto.VehicleDTO;
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

	private final AtomicLong counter = new AtomicLong(1);

	public List<Ticket> findAll() {
		return repo.findAll();
	}

	public List<Ticket> openTickets() {
		return repo.findByStatus("OPEN");
	}

	public Ticket findById(String id) {
		return repo.findById(id).orElseThrow(() -> new RuntimeException("Ticket not found"));
	}

	// vehicle enters parking
	public Ticket enter(EnterDTO dto) {
		String plate = dto.getPlateNumber();
		if (plate == null || plate.isBlank()) {
			throw new RuntimeException("plateNumber is required");
		}

		// // check if there is already an OPEN ticket for this plate
		// repo.findFirstByPlateNumberAndStatus(plate, "OPEN").ifPresent(t -> {
		// throw new RuntimeException("Vehicle already has an OPEN ticket");
		// });

		// Improvement: use existsBy + index for faster check
		if (repo.existsByPlateNumberAndStatus(plate, "OPEN")) {
			throw new RuntimeException("Vehicle already has an OPEN ticket");
		}

		Ticket t = new Ticket();
		t.setPlateNumber(plate);
		t.setStatus("OPEN");
		t.setEntryTime(LocalDateTime.now());

		long next = counter.incrementAndGet();
		t.setTicketNumber(String.format("T-%03d", next));

		return repo.save(t);
	}

	// vehicle exits parking
	public Ticket exit(ExitDTO dto) {
		Ticket t;

		// find ticket either by ticketNumber or by plateNumber
		if (dto.getTicketNumber() != null && !dto.getTicketNumber().isBlank()) {
			t = repo.findFirstByTicketNumber(dto.getTicketNumber())
					.orElseThrow(() -> new RuntimeException("Ticket not found for ticketNumber"));
		} else if (dto.getPlateNumber() != null && !dto.getPlateNumber().isBlank()) {
			t = repo.findFirstByPlateNumberAndStatus(dto.getPlateNumber(), "OPEN")
					.orElseThrow(() -> new RuntimeException("OPEN ticket not found for plate"));
		} else {
			throw new RuntimeException("ticketNumber or plateNumber is required");
		}

		if ("CLOSED".equalsIgnoreCase(t.getStatus())) {
			throw new RuntimeException("Ticket is already CLOSED");
		}

		// 1) get vehicle to know its type
		VehicleDTO vehicle = vehicleClient.findByPlate(t.getPlateNumber());
		String vehicleType = vehicle.getVehicleType(); // BIKE / CAR / TRUCK

		// 2) get current rate configuration
		RateDTO rate = rateClient.current();

		// 3) calculate total minutes parked
		LocalDateTime entry = t.getEntryTime();
		LocalDateTime now = LocalDateTime.now();

		long totalMinutes = Duration.between(entry, now).toMinutes();

		int grace = rate.getGraceMinutes() != null ? rate.getGraceMinutes() : 0;
		long billable = Math.max(0, totalMinutes - grace);

		// 4) choose hourly rate based on vehicle type
		double perHour = 0.0;
		if (vehicleType != null) {
			String type = vehicleType.toUpperCase();
			switch (type) {
			case "BIKE":
				if (rate.getBikePerHour() != null) {
					perHour = rate.getBikePerHour();
				}
				break;
			case "CAR":
				if (rate.getCarPerHour() != null) {
					perHour = rate.getCarPerHour();
				}
				break;
			case "TRUCK":
				if (rate.getTruckPerHour() != null) {
					perHour = rate.getTruckPerHour();
				}
				break;
			default:
				break;
			}
		}

		// fallback to generic perHour if specific one is missing
		if (perHour == 0.0 && rate.getPerHour() != null) {
			perHour = rate.getPerHour();
		}

		double fee;

		if ("FLAT".equalsIgnoreCase(rate.getPricingModel())) {
			fee = rate.getFlatAmount() != null ? rate.getFlatAmount() : 0.0;
		} else {
			// HOURLY
			double hours;
			if (billable <= 0) {
				hours = 0;
			} else if (Boolean.TRUE.equals(rate.getRoundUpToHour())) {
				hours = Math.ceil(billable / 60.0);
			} else {
				hours = Math.max(1, Math.floor(billable / 60.0));
			}
			fee = perHour * hours;
		}

		t.setExitTime(now);
		t.setCalculatedFee(fee);
		t.setStatus("CLOSED");
		return repo.save(t);
	}
}