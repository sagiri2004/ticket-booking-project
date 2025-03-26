package com.example.bookingservice.command.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
public class CreateBookingCommand {

	@TargetAggregateIdentifier
	private String id;
	private String userId;
	private String ticketId;
	private int quantity;
}
