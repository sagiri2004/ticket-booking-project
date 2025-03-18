package com.microserviceproject.userservice.command.controller;

import com.microserviceproject.userservice.command.command.CreateUserCommand;
import com.microserviceproject.userservice.command.command.DeleteUserCommand;
import com.microserviceproject.userservice.command.command.UpdateUserCommand;
import com.microserviceproject.userservice.command.model.CreateUserModel;
import com.microserviceproject.userservice.command.model.UpdateUserModel;
import jakarta.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserCommandController {
	@Autowired
	private CommandGateway commandGateway;

	@PostMapping
	public String createUser(@Valid @RequestBody CreateUserModel model) {
		CreateUserCommand command =
				new CreateUserCommand(UUID.randomUUID().toString(), model.getUsername(), model.getPassword());
		return commandGateway.sendAndWait(command);
	}

	@PutMapping("/{userId}")
	public String updateUser(@Valid @RequestBody UpdateUserModel model, @PathVariable String userId) {
		UpdateUserCommand command = new UpdateUserCommand(userId, model.getUsername(), model.getPassword());
		return commandGateway.sendAndWait(command);
	}

	@DeleteMapping("/{userId}")
	public String deleteUser(@PathVariable String userId) {
		DeleteUserCommand command = new DeleteUserCommand(userId);
		return commandGateway.sendAndWait(command);
	}
}
