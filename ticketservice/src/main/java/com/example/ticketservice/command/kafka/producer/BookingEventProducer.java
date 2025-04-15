package com.example.ticketservice.command.kafka.producer;

import com.example.ticketservice.command.event.TicketQuantityUpdatedEvent;
import com.example.ticketservice.command.event.TicketReservationFailedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingEventProducer {

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;

	private static final String TICKET_RESERVED_TOPIC = "ticket-reserved-topic";
	private static final String TICKET_RESERVATION_FAILED_TOPIC = "ticket-reservation-failed-topic";

	public void sendTicketReservedEvent(TicketQuantityUpdatedEvent event) {
		try {
			String message = objectMapper.writeValueAsString(event);
			kafkaTemplate.send(TICKET_RESERVED_TOPIC, message);
			log.info("📤 Sent TicketReservedEvent: {}", message);
		} catch (JsonProcessingException e) {
			log.error("❌ Failed to serialize TicketReservedEvent", e);
		}
	}

	public void sendTicketReservationFailedEvent(TicketReservationFailedEvent event) {
		try {
			String message = objectMapper.writeValueAsString(event);
			kafkaTemplate.send(TICKET_RESERVATION_FAILED_TOPIC, message);
			log.info("📤 Sent TicketReservationFailedEvent: {}", message);
		} catch (JsonProcessingException e) {
			log.error("❌ Failed to serialize TicketReservationFailedEvent", e);
		}
	}
}
