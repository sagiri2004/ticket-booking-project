package com.example.ticketservice.command.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketQuantityUpdatedEvent {
	private String id;
	private int quantity;
}
