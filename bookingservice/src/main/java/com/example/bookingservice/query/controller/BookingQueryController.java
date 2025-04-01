package com.example.bookingservice.query.controller;

import com.example.bookingservice.query.model.BookingResponseModel;
import com.example.bookingservice.query.queries.GetAllBookingsQuery;
import com.example.bookingservice.query.queries.GetBookingByIdQuery;
import com.example.bookingservice.query.queries.GetBookingByTicketQuery;
import com.example.bookingservice.query.queries.GetBookingByUserQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingQueryController {

	@Autowired
	private QueryGateway queryGateway;

	@GetMapping
	public CompletableFuture<List<BookingResponseModel>> getAllBookings() {
		return queryGateway.query(new GetAllBookingsQuery(),
				ResponseTypes.multipleInstancesOf(BookingResponseModel.class));
	}

	@GetMapping("/{bookingId}")
	public CompletableFuture<BookingResponseModel> getBookingById(@PathVariable String bookingId) {
		return queryGateway.query(new GetBookingByIdQuery(bookingId),
				ResponseTypes.instanceOf(BookingResponseModel.class));
	}

	@GetMapping("/user/{userId}")
	public CompletableFuture<List<BookingResponseModel>> getBookingsByUser(@PathVariable String userId) {
		return queryGateway.query(new GetBookingByUserQuery(userId),
				ResponseTypes.multipleInstancesOf(BookingResponseModel.class));
	}


	@GetMapping("/ticket/{ticketId}")
	public CompletableFuture<List<BookingResponseModel>> getBookingsByTicket(@PathVariable String ticketId) {
		return queryGateway.query(new GetBookingByTicketQuery(ticketId),
				ResponseTypes.multipleInstancesOf(BookingResponseModel.class));
	}
}
