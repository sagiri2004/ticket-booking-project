package com.example.ticketservice.command.kafka.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTicketQuantityCommand {
	@TargetAggregateIdentifier
	private String id;
	private int quantity;
}
