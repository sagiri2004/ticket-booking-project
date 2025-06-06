package com.example.ticketservice.command.kafka.events;

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
