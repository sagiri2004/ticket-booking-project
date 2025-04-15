package com.example.bookingservice.command.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
public class CancelBookingCommand {
	@TargetAggregateIdentifier
	private String id;
}