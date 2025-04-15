package com.example.ticketservice.command.kafka.consumer;

import com.example.ticketservice.command.commands.UpdateTicketQuantityCommand;
import com.example.ticketservice.command.kafka.events.BookingCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BookingEventConsumer {
	@Autowired
	private CommandGateway commandGateway;
	@Autowired
	private ObjectMapper objectMapper;

	@KafkaListener(topics = "booking-created-topic", groupId = "ticket-service-group")
	public void consumeBookingCreatedEvent(@Payload String message) {
		try {
			BookingCreatedEvent event = objectMapper.readValue(message, BookingCreatedEvent.class);
			log.info("✅ Received BookingCreatedEvent: {}", event);

			UpdateTicketQuantityCommand command = new UpdateTicketQuantityCommand();
			command.setId(event.getTicketId());
			command.setQuantity(event.getQuantity());
			command.setBookingId(event.getId());
			commandGateway.send(command);

		} catch (Exception e) {
			log.error("❌ Failed to deserialize BookingCreatedEvent", e);
		}
	}
}
