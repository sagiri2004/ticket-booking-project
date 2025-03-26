package com.example.bookingservice.command.kafka.events;

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
