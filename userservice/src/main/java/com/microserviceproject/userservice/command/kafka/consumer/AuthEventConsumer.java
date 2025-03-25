package com.microserviceproject.userservice.command.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microserviceproject.userservice.command.command.CreateUserCommand;
import com.microserviceproject.userservice.command.kafka.events.AuthUserCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthEventConsumer {
	@Autowired
	private CommandGateway commandGateway;

	@Autowired
	private ObjectMapper objectMapper;

	@KafkaListener(topics = "auth-user-created-topic", groupId = "user-service-group")
	public void consumeAuthUserCreatedEvent(@Payload String message) {
		try {
			AuthUserCreatedEvent event = objectMapper.readValue(message, AuthUserCreatedEvent.class);
			log.info("✅ Received AuthUserCreatedEvent: {}", event);

			CreateUserCommand command = new CreateUserCommand();
			BeanUtils.copyProperties(event, command);
			command.setActive(true);

			log.info("✅ Received AuthUserCreatedEvent: {}", command);
			commandGateway.send(command);

		} catch (Exception e) {
			log.error("❌ Failed to deserialize AuthUserCreatedEvent", e);
		}
	}
}
