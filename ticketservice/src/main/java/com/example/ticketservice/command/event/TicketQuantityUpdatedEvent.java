package com.example.ticketservice.command.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketQuantityUpdatedEvent {
	private String id;
	private int quantity;
	private String bookingId;
}
