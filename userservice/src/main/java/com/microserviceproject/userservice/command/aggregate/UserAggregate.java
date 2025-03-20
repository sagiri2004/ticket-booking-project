package com.microserviceproject.userservice.command.aggregate;

import com.microserviceproject.userservice.command.command.CreateUserCommand;
import com.microserviceproject.userservice.command.command.DeleteUserCommand;
import com.microserviceproject.userservice.command.command.UpdateUserCommand;
import com.microserviceproject.userservice.command.event.UserCreatedEvent;
import com.microserviceproject.userservice.command.event.UserDeletedEvent;
import com.microserviceproject.userservice.command.event.UserUpdatedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@NoArgsConstructor
@Aggregate
public class UserAggregate {
	@AggregateIdentifier
	private String id;
	private String name;
	private String email;
	private Boolean active;

	@CommandHandler
	public UserAggregate(CreateUserCommand command) {
		UserCreatedEvent event = new UserCreatedEvent();
		BeanUtils.copyProperties(command, event);
		AggregateLifecycle.apply(event);
	}

	@CommandHandler
	public void handle(UpdateUserCommand command) {
		UserUpdatedEvent event = new UserUpdatedEvent();
		BeanUtils.copyProperties(command, event);
		AggregateLifecycle.apply(event);
	}

	@CommandHandler
	public void handle(DeleteUserCommand command) {
		UserDeletedEvent event = new UserDeletedEvent();
		BeanUtils.copyProperties(command, event);
		AggregateLifecycle.apply(event);
	}

	@EventSourcingHandler
	public void on(UserCreatedEvent event) {
		this.id = event.getId();
		this.name = event.getName();
		this.email = event.getEmail();
		this.active = event.getActive();
	}

	@EventSourcingHandler
	public void on(UserUpdatedEvent event) {
		this.id = event.getId();
		this.name = event.getName();
		this.email = event.getEmail();
		this.active = event.getActive();
	}

	@EventSourcingHandler
	public void on(UserDeletedEvent event) {
		this.id = event.getId();
	}
}
