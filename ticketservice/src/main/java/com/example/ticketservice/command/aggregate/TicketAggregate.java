package com.example.ticketservice.command.aggregate;

import com.example.ticketservice.command.commands.CreateTicketCommand;
import com.example.ticketservice.command.commands.DeleteTicketCommand;
import com.example.ticketservice.command.commands.UpdateTicketCommand;
import com.example.ticketservice.command.commands.UpdateTicketQuantityCommand;
import com.example.ticketservice.command.event.TicketCreatedEvent;
import com.example.ticketservice.command.event.TicketDeletedEvent;
import com.example.ticketservice.command.event.TicketQuantityUpdatedEvent;
import com.example.ticketservice.command.event.TicketUpdatedEvent;
import com.example.ticketservice.command.event.TicketReservationFailedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@NoArgsConstructor
@Aggregate
public class TicketAggregate {
	@AggregateIdentifier
	private String id;
	private String name;
	private Double price;
	private Integer totalQuantity;
	private Integer remainingQuantity;
	private String status;

	@CommandHandler
	public TicketAggregate(CreateTicketCommand command) {
		TicketCreatedEvent event = new TicketCreatedEvent();
		BeanUtils.copyProperties(command, event);
		AggregateLifecycle.apply(event);
	}

	@CommandHandler
	public void handle(UpdateTicketCommand command) {
		TicketUpdatedEvent event = new TicketUpdatedEvent();
		BeanUtils.copyProperties(command, event);
		AggregateLifecycle.apply(event);
	}

	@CommandHandler
	public void handle(DeleteTicketCommand command) {
		TicketDeletedEvent event = new TicketDeletedEvent();
		BeanUtils.copyProperties(command, event);
		AggregateLifecycle.apply(event);
	}

	@CommandHandler
	public void handle(UpdateTicketQuantityCommand command) {
		if (this.remainingQuantity < command.getQuantity()) {
			TicketReservationFailedEvent event = new TicketReservationFailedEvent(this.id, command.getQuantity(), command.getBookingId());
			AggregateLifecycle.apply(event);
		} else {
			TicketQuantityUpdatedEvent event = new TicketQuantityUpdatedEvent(this.id, command.getQuantity(), command.getBookingId());
			AggregateLifecycle.apply(event);
		}
	}

	@EventSourcingHandler
	public void on(TicketCreatedEvent event) {
		this.id = event.getId();
		this.name = event.getName();
		this.price = event.getPrice();
		this.totalQuantity = event.getTotalQuantity();
		this.remainingQuantity = event.getRemainingQuantity();
		this.status = event.getStatus();
	}

	@EventSourcingHandler
	public void on(TicketUpdatedEvent event) {
		this.id = event.getId();
		this.name = event.getName();
		this.price = event.getPrice();
		this.totalQuantity = event.getTotalQuantity();
		this.remainingQuantity = event.getRemainingQuantity();
		this.status = event.getStatus();
	}

	@EventSourcingHandler
	public void on(TicketDeletedEvent event) {
		this.id = event.getId();
	}

	@EventSourcingHandler
	public void on(TicketQuantityUpdatedEvent event) {
		this.remainingQuantity -= event.getQuantity();
	}

	@EventSourcingHandler
	public void on(TicketReservationFailedEvent event) {
	}
}
