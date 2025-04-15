package com.example.bookingservice.query.projection;

import com.example.bookingservice.command.data.Booking;
import com.example.bookingservice.command.data.BookingRepository;
import com.example.bookingservice.query.model.BookingResponseModel;
import com.example.bookingservice.query.queries.GetAllBookingsQuery;
import com.example.bookingservice.query.queries.GetBookingByIdQuery;
import com.example.bookingservice.query.queries.GetBookingByTicketQuery;
import com.example.bookingservice.query.queries.GetBookingByUserQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingProjection {
	@Autowired
	private BookingRepository bookingRepository;

	@QueryHandler
	@Cacheable(value = "bookings", key = "'all_bookings'")
	public List<BookingResponseModel> handle(GetAllBookingsQuery query) {
		List<Booking> bookings = bookingRepository.findAll();
		return bookings.stream().map(booking -> {
			BookingResponseModel model = new BookingResponseModel();
			BeanUtils.copyProperties(booking, model);
			return model;
		}).collect(Collectors.toList());
	}

	@QueryHandler
	@Cacheable(value = "bookings", key = "#query.id")
	public BookingResponseModel handle(GetBookingByIdQuery query) throws Exception {
		Booking booking = bookingRepository.findById(query.getId())
				.orElseThrow(() -> new Exception("Booking not found: " + query.getId()));
		BookingResponseModel model = new BookingResponseModel();
		BeanUtils.copyProperties(booking, model);
		return model;
	}

	@QueryHandler
	public List<BookingResponseModel> handle(GetBookingByUserQuery query) {
		List<Booking> bookings = bookingRepository.findByUserId(query.getUserId());
		return bookings.stream().map(booking -> {
			BookingResponseModel model = new BookingResponseModel();
			BeanUtils.copyProperties(booking, model);
			return model;
		}).collect(Collectors.toList());
	}

	@QueryHandler
	public List<BookingResponseModel> handle(GetBookingByTicketQuery query) {
		List<Booking> bookings = bookingRepository.findByTicketId(query.getTicketId());
		return bookings.stream().map(booking -> {
			BookingResponseModel model = new BookingResponseModel();
			BeanUtils.copyProperties(booking, model);
			return model;
		}).toList();
	}
}
