package com.example.authenticationservice.kafka;

import com.example.authenticationservice.event.AuthUserCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaService {
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;

	public void sendMessage(String topic, String message) {
		kafkaTemplate.send(topic, message).whenComplete((result, ex) -> {
			if (ex == null) {
				log.info("✅ Message sent successfully: {}", message);
			} else {
				log.error("❌ Failed to send message", ex);
			}
		});
	}

	public void sendAuthUserCreatedEvent(AuthUserCreatedEvent event) {
		try {
			String message = objectMapper.writeValueAsString(event);
			CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("auth-user-created-topic", message);

			future.whenComplete((result, ex) -> {
				if (ex == null) {
					log.info("✅ AuthUserCreatedEvent sent successfully: {}", message);
				} else {
					log.error("❌ Failed to send AuthUserCreatedEvent, retrying...", ex);
					sendAuthUserCreatedEvent(event); // Thử gửi lại khi gặp lỗi
				}
			});

		} catch (JsonProcessingException e) {
			log.error("❌ Error serializing AuthUserCreatedEvent", e);
		}
	}
}
