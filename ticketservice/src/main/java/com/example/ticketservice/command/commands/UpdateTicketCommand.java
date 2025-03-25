package com.example.ticketservice.command.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTicketCommand {
	@TargetAggregateIdentifier
	private String id;
	private String name;
	private double price;
	private int totalQuantity;
	private int remainingQuantity;
	private String status;
}
