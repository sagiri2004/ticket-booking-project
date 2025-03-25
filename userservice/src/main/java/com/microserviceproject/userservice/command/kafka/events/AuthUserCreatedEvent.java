package com.microserviceproject.userservice.command.kafka.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserCreatedEvent {
	private String id;
	private String email;
	private String name;
}
