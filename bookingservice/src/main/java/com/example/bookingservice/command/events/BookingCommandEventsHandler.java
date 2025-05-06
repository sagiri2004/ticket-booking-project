package com.example.bookingservice.command.events;

import com.example.bookingservice.command.data.Booking;
import com.example.bookingservice.command.data.BookingRepository;
import com.example.bookingservice.command.data.BookingStatus;
import com.example.bookingservice.command.kafka.events.NotificationEvent;
import com.example.bookingservice.command.kafka.producer.NotificationEventProducer;
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
	private final NotificationEventProducer notificationEventProducer;
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

		NotificationEvent notificationEvent = new NotificationEvent();
		notificationEvent.setUserId(booking.getUserId());
		notificationEvent.setTitle("Booking Confirmed");
		notificationEvent.setContent(String.format(
				"Your booking for %d ticket(s) has been confirmed. Booking ID: %s",
				booking.getQuantity(),
				booking.getId()
		));
		notificationEvent.setType("BOOKING_CONFIRMED");

		notificationEventProducer.sendNotificationEvent(notificationEvent);
	}

	@EventHandler
	public void on(BookingCancelledEvent event) {
		Booking booking = bookingRepository.findById(event.getId())
				.orElseThrow(() -> new RuntimeException("Booking not found"));

		booking.setStatus(BookingStatus.CANCELLED);
		bookingRepository.save(booking);
		log.info("🚫 Booking Cancelled: {}", booking);

		NotificationEvent notificationEvent = new NotificationEvent();
		notificationEvent.setUserId(booking.getUserId());
		notificationEvent.setTitle("Booking Cancelled");
		notificationEvent.setContent(String.format(
				"Your booking for %d ticket(s) has been cancelled. Booking ID: %s",
				booking.getQuantity(),
				booking.getId()
		));
		notificationEvent.setType("BOOKING_CANCELLED");

		notificationEventProducer.sendNotificationEvent(notificationEvent);
	}

	@EventHandler
	public void on(BookingRejectedEvent event) {
		Booking booking = bookingRepository.findById(event.getId())
				.orElseThrow(() -> new RuntimeException("Booking not found"));

		booking.setStatus(BookingStatus.REJECTED);
		bookingRepository.save(booking);
		log.info("❌ Booking Rejected: {}", booking);

		NotificationEvent notificationEvent = new NotificationEvent();
		notificationEvent.setUserId(booking.getUserId());
		notificationEvent.setTitle("Booking Rejected");
		notificationEvent.setContent(String.format(
				"Your booking for %d ticket(s) has been rejected. Booking ID: %s",
				booking.getQuantity(),
				booking.getId()
		));
		notificationEvent.setType("BOOKING_REJECTED");

		notificationEventProducer.sendNotificationEvent(notificationEvent);
	}
}