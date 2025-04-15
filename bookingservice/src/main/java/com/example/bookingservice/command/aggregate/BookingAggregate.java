package com.example.bookingservice.command.aggregate;

import com.example.bookingservice.command.commands.CancelBookingCommand;
import com.example.bookingservice.command.commands.ConfirmBookingCommand;
import com.example.bookingservice.command.commands.CreateBookingCommand;
import com.example.bookingservice.command.commands.FailBookingCommand;
import com.example.bookingservice.command.events.BookingCancelledEvent;
import com.example.bookingservice.command.events.BookingConfirmedEvent;
import com.example.bookingservice.command.events.BookingCreatedEvent;
import com.example.bookingservice.command.events.BookingRejectedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;


@NoArgsConstructor
@Aggregate
public class BookingAggregate {

	@AggregateIdentifier
	private String id;
	private String userId;
	private String ticketId;
	private int quantity;
	private String status; // "PENDING", "CONFIRMED", "CANCELLED", "REJECTED"

	@CommandHandler
	public BookingAggregate(CreateBookingCommand command) {
		BookingCreatedEvent event = new BookingCreatedEvent();
		BeanUtils.copyProperties(command, event);
		AggregateLifecycle.apply(event);
	}

	@CommandHandler
	public void handle(CancelBookingCommand command) {
		BookingCancelledEvent event = new BookingCancelledEvent();
		BeanUtils.copyProperties(command, event);
		AggregateLifecycle.apply(event);
	}

	@CommandHandler
	public void handle(ConfirmBookingCommand command) {
		BookingConfirmedEvent event = new BookingConfirmedEvent(command.getId());
		AggregateLifecycle.apply(event);
	}

	@CommandHandler
	public void handle(FailBookingCommand command) {
		BookingRejectedEvent event = new BookingRejectedEvent(command.getId());
		AggregateLifecycle.apply(event);
	}

	@EventSourcingHandler
	public void on(BookingCreatedEvent event) {
		this.id = event.getId();
		this.userId = event.getUserId();
		this.ticketId = event.getTicketId();
		this.quantity = event.getQuantity();
		this.status = "PENDING";
	}

	@EventSourcingHandler
	public void on(BookingCancelledEvent event) {
		this.id = event.getId();
		this.status = "CANCELLED";
	}

	@EventSourcingHandler
	public void on(BookingConfirmedEvent event) {
		this.id = event.getId();
		this.status = "CONFIRMED";
	}

	@EventSourcingHandler
	public void on(BookingRejectedEvent event) {
		this.id = event.getId();
		this.status = "REJECTED";
	}
}