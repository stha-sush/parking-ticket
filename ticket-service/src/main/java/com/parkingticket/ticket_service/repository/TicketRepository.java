package com.parkingticket.ticket_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parkingticket.ticket_service.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
	Optional<Ticket> findFirstByPlateNumberAndStatus(String plateNumber, String status);
	Optional<Ticket> findFirstByTicketNumber(String ticketNumber);
}
