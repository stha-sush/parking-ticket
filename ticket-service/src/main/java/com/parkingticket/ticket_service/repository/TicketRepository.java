package com.parkingticket.ticket_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.parkingticket.ticket_service.entity.Ticket;

public interface TicketRepository extends MongoRepository<Ticket, String> {
	Optional<Ticket> findFirstByPlateNumberAndStatus(String plateNumber, String status);

	Optional<Ticket> findFirstByTicketNumber(String ticketNumber);

	List<Ticket> findByStatus(String status);

	// only returns true/false, does not fetch full Ticket document
	// test 
	boolean existsByPlateNumberAndStatus(String plateNumber, String status);
}
