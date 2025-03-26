package com.example.bookingservice.command.kafka.producer;

import com.example.bookingservice.command.events.BookingCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketEventProducer {

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;

	private static final String TOPIC = "booking-created-topic";

	public void sendBookingCreatedEvent(BookingCreatedEvent event) {
		try {
			String message = objectMapper.writeValueAsString(event);
			kafkaTemplate.send(TOPIC, message);
			log.info("📤 Sent BookingCreatedEvent: {}", message);
		} catch (JsonProcessingException e) {
			log.error("❌ Failed to serialize BookingCreatedEvent", e);
		}
	}
}
