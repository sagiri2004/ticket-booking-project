package com.example.ticketservice.command.controller;

import com.example.ticketservice.command.commands.CreateTicketCommand;
import com.example.ticketservice.command.commands.DeleteTicketCommand;
import com.example.ticketservice.command.commands.UpdateTicketCommand;
import com.example.ticketservice.command.model.CreateTicketModel;
import com.example.ticketservice.command.model.UpdateTicketModel;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/tickets")
public class TicketCommandController {
	@Autowired
	private CommandGateway commandGateway;

	@PostMapping
	public String createTicket(@Valid @RequestBody CreateTicketModel model) {
		CreateTicketCommand command = new CreateTicketCommand(
				UUID.randomUUID().toString(),
				model.getName(),
				model.getPrice(),
				model.getTotalQuantity(),
				model.getRemainingQuantity(),
				model.getStatus()
		);

		log.info("Create ticket command: {}", command);
		return commandGateway.sendAndWait(command);
	}

	@PutMapping("/{ticketId}")
	public String updateTicket(@Valid @RequestBody UpdateTicketModel model, @PathVariable String ticketId) {
		UpdateTicketCommand command = new UpdateTicketCommand(
				ticketId,
				model.getName(),
				model.getPrice(),
				model.getTotalQuantity(),
				model.getRemainingQuantity(),
				model.getStatus()
		);

		log.info("Update ticket command: {}", command);
		return commandGateway.sendAndWait(command);
	}

	@DeleteMapping("/{ticketId}")
	public String deleteTicket(@PathVariable String ticketId) {
		DeleteTicketCommand command = new DeleteTicketCommand(ticketId);

		log.info("Delete ticket command: {}", command);
		return commandGateway.sendAndWait(command);
	}
}
