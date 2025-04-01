package com.example.bookingservice.command.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingRejectedEvent {
	private String id;
}