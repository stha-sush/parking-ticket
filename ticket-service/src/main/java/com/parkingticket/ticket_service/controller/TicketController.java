package com.parkingticket.ticket_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parkingticket.ticket_service.client.EnterDTO;
import com.parkingticket.ticket_service.client.ExitDTO;
import com.parkingticket.ticket_service.entity.Ticket;
import com.parkingticket.ticket_service.service.TicketService;

@RestController
@RequestMapping("/tickets")
public class TicketController {
	@Autowired
	private TicketService service;

	@GetMapping("/open")
	public List<Ticket> open() {
		return service.openTickets();
	}

	@GetMapping("/{id:\\d+}")
	public Ticket one(@PathVariable Long id) {
		return service.findById(id);
	}

	@PostMapping("/enter")
	public Ticket enter(@RequestBody EnterDTO dto) {
		return service.enter(dto);
	}

	@PostMapping("/exit")
	public Ticket exit(@RequestBody ExitDTO dto) {
		return service.exit(dto);
	}

}
