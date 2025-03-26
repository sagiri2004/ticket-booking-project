package com.example.bookingservice.command.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FailBookingCommand {
	@TargetAggregateIdentifier
	private String id;
}