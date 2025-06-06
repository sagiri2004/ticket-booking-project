package com.microserviceproject.userservice.command.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreatedEvent {
	private String id;
	private String name;
	private String email;
	private Boolean active;
}
