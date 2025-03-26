package com.example.ticketservice.command.event;

import com.example.ticketservice.command.data.Ticket;
import com.example.ticketservice.command.data.TicketRepository;
import com.example.ticketservice.command.kafka.producer.BookingEventProducer;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.DisallowReplay;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TicketCommandEventsHandler {
	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private BookingEventProducer bookingEventProducer;

	@EventHandler
	public void on(TicketCreatedEvent event) {
		Ticket ticket = new Ticket();
		BeanUtils.copyProperties(event, ticket);
		ticketRepository.save(ticket);
	}

	@EventHandler
	public void on(TicketUpdatedEvent event) {
		Ticket ticket = ticketRepository.findById(event.getId())
				.orElseThrow(() -> new RuntimeException("Ticket not found"));

		ticket.setName(event.getName());
		ticket.setPrice(event.getPrice());
		ticket.setTotalQuantity(event.getTotalQuantity());
		ticket.setRemainingQuantity(event.getRemainingQuantity());
		ticket.setStatus(event.getStatus());

		ticketRepository.save(ticket);
	}

	@EventHandler
	@DisallowReplay
	public void on(TicketDeletedEvent event) {
		try {
			ticketRepository.findById(event.getId())
					.orElseThrow(() -> new Exception("Ticket not found"));
			ticketRepository.deleteById(event.getId());
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	@EventHandler
	public void on(TicketQuantityUpdatedEvent event) {
		int updatedRows = ticketRepository.updateRemainingQuantity(event.getId(), event.getQuantity());

		if (updatedRows == 0) {
			TicketReservationFailedEvent failedEvent = new TicketReservationFailedEvent(event.getId(), event.getQuantity(), event.getBookingId());
			bookingEventProducer.sendTicketReservationFailedEvent(failedEvent);
		} else {
			bookingEventProducer.sendTicketReservedEvent(event);
		}
	}

	@EventHandler
	public void on(TicketReservationFailedEvent event) {
		bookingEventProducer.sendTicketReservationFailedEvent(event);
	}
}
