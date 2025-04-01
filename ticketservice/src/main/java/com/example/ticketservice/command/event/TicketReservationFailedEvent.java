package com.example.ticketservice.command.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketReservationFailedEvent {
	private String id;
	private int quantity;
	private String bookingId;
}
