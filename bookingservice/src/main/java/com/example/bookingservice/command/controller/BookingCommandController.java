package com.example.bookingservice.command.controller;

import com.example.bookingservice.command.commands.CancelBookingCommand;
import com.example.bookingservice.command.commands.CreateBookingCommand;
import com.example.bookingservice.command.model.CancelBookingModel;
import com.example.bookingservice.command.model.CreateBookingModel;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/v1/bookings")
public class BookingCommandController {

	@Autowired
	private CommandGateway commandGateway;

	@PostMapping
	public CompletableFuture<String> createBooking(@Valid @RequestBody CreateBookingModel model) {
		String bookingId = UUID.randomUUID().toString();
		return commandGateway.send(new CreateBookingCommand(
				bookingId,
				model.getUserId(),
				model.getTicketId(),
				model.getQuantity()
		));
	}

	@PutMapping("/cancel")
	public CompletableFuture<String> cancelBooking(@Valid @RequestBody CancelBookingModel model) {
		return commandGateway.send(new CancelBookingCommand(model.getId()));
	}
}