package com.example.bookingservice.query.queries;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetBookingByTicketQuery {
	private String ticketId;
}
