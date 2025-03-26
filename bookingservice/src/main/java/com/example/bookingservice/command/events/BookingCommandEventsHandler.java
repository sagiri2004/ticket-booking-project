package com.example.bookingservice.command.events;

import com.example.bookingservice.command.data.Booking;
import com.example.bookingservice.command.data.BookingRepository;
import com.example.bookingservice.command.data.BookingStatus;
import com.example.bookingservice.command.kafka.producer.TicketEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingCommandEventsHandler {

	private final BookingRepository bookingRepository;
	private final TicketEventProducer ticketEventProducer;

	@EventHandler
	public void on(BookingCreatedEvent event) {
		Booking booking = new Booking();
		booking.setId(event.getId());
		booking.setUserId(event.getUserId());
		booking.setTicketId(event.getTicketId());
		booking.setQuantity(event.getQuantity());
		booking.setStatus(BookingStatus.PENDING);

		bookingRepository.save(booking);
		log.info("✅ Booking Created: {}", booking);

		ticketEventProducer.sendBookingCreatedEvent(event);
	}

	@EventHandler
	public void on(BookingConfirmedEvent event) {
		Booking booking = bookingRepository.findById(event.getId())
				.orElseThrow(() -> new RuntimeException("Booking not found"));

		booking.setStatus(BookingStatus.CONFIRMED);
		bookingRepository.save(booking);
		log.info("✅ Booking Confirmed: {}", booking);
	}

	@EventHandler
	public void on(BookingCancelledEvent event) {
		Booking booking = bookingRepository.findById(event.getId())
				.orElseThrow(() -> new RuntimeException("Booking not found"));

		booking.setStatus(BookingStatus.CANCELLED);
		bookingRepository.save(booking);
		log.info("🚫 Booking Cancelled: {}", booking);
	}

	@EventHandler
	public void on(BookingRejectedEvent event) {
		Booking booking = bookingRepository.findById(event.getId())
				.orElseThrow(() -> new RuntimeException("Booking not found"));

		booking.setStatus(BookingStatus.REJECTED);
		bookingRepository.save(booking);
		log.info("❌ Booking Rejected: {}", booking);
	}
}
