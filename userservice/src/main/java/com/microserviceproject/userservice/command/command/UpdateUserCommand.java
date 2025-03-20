package com.microserviceproject.userservice.command.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserCommand {
	@TargetAggregateIdentifier
	private String id;
	private String email;
	private String name;
	private boolean active;
}
