package com.example.bookingservice.command.kafka.consumer;

import com.example.bookingservice.command.kafka.events.TicketQuantityUpdatedEvent;
import com.example.bookingservice.command.kafka.events.TicketReservationFailedEvent;
import com.example.bookingservice.command.commands.ConfirmBookingCommand;
import com.example.bookingservice.command.commands.FailBookingCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TicketEventConsumer {
	@Autowired
	private CommandGateway commandGateway;

	@Autowired
	private ObjectMapper objectMapper;

	@KafkaListener(topics = "ticket-reserved-topic", groupId = "booking-service-group")
	public void consumeTicketQuantityUpdatedEvent(@Payload String message) {
		try {
			TicketQuantityUpdatedEvent event = objectMapper.readValue(message, TicketQuantityUpdatedEvent.class);

			ConfirmBookingCommand command = new ConfirmBookingCommand(event.getBookingId());
			commandGateway.send(command);

		} catch (Exception e) {
			log.error("❌ Failed to deserialize TicketQuantityUpdatedEvent", e);
		}
	}

	@KafkaListener(topics = "ticket-reservation-failed-topic", groupId = "booking-service-group")
	public void consumeTicketReservationFailedEvent(@Payload String message) {
		try {
			TicketReservationFailedEvent event = objectMapper.readValue(message, TicketReservationFailedEvent.class);

			FailBookingCommand command = new FailBookingCommand(event.getBookingId());
			commandGateway.send(command);

		} catch (Exception e) {
			log.error("❌ Failed to deserialize TicketReservationFailedEvent", e);
		}
	}
}
