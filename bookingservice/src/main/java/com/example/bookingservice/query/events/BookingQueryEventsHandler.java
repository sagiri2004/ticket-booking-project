package com.example.bookingservice.query.events;

import com.example.bookingservice.command.events.BookingCancelledEvent;
import com.example.bookingservice.command.events.BookingConfirmedEvent;
import com.example.bookingservice.command.events.BookingCreatedEvent;
import com.example.bookingservice.command.events.BookingRejectedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class BookingQueryEventsHandler {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@EventHandler
	public void on(BookingCreatedEvent event) {
		redisTemplate.delete("bookings::all_bookings");
		redisTemplate.delete("bookings::" + event.getId());
		System.out.println("🗑️ Deleted cache for all bookings & booking ID: " + event.getId() + " (Booking Created)");
	}

	@EventHandler
	public void on(BookingConfirmedEvent event) {
		String bookingCacheKey = "bookings::" + event.getId();
		redisTemplate.delete(bookingCacheKey);
		redisTemplate.delete("bookings::all_bookings");
		System.out.println("🗑️ Deleted cache for booking ID: " + event.getId() + " (Booking Confirmed)");
	}

	@EventHandler
	public void on(BookingCancelledEvent event) {
		String bookingCacheKey = "bookings::" + event.getId();
		redisTemplate.delete(bookingCacheKey);
		redisTemplate.delete("bookings::all_bookings");
		System.out.println("🗑️ Deleted cache for booking ID: " + event.getId() + " (Booking Cancelled)");
	}

	@EventHandler
	public void on(BookingRejectedEvent event) {
		String bookingCacheKey = "bookings::" + event.getId();
		redisTemplate.delete(bookingCacheKey);
		redisTemplate.delete("bookings::all_bookings");
		System.out.println("🗑️ Deleted cache for booking ID: " + event.getId() + " (Booking Rejected)");
	}
}
