package com.example.bookingservice.command.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreatedEvent {
	private String id;
	private String userId;
	private String ticketId;
	private int quantity;
}
