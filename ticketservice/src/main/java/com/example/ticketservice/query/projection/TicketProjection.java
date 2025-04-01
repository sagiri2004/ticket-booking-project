package com.example.ticketservice.query.projection;

import com.example.ticketservice.command.data.Ticket;
import com.example.ticketservice.command.data.TicketRepository;
import com.example.ticketservice.query.model.TicketResponseModel;
import com.example.ticketservice.query.queries.GetAllTicketsQuery;
import com.example.ticketservice.query.queries.GetTicketByIdQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class TicketProjection {
	@Autowired
	private TicketRepository ticketRepository;

	@QueryHandler
	@Cacheable(value = "tickets", key = "'all_tickets'")
	public List<TicketResponseModel> handle(GetAllTicketsQuery query) {
		List<Ticket> tickets = ticketRepository.findAll();
		return tickets.stream().map(ticket -> {
			TicketResponseModel model = new TicketResponseModel();
			BeanUtils.copyProperties(ticket, model);
			return model;
		}).toList();
	}

	@QueryHandler
	@Cacheable(value = "tickets", key = "#query.id")
	public TicketResponseModel handle(GetTicketByIdQuery query) throws Exception {
		TicketResponseModel ticketResponseModel = new TicketResponseModel();
		Ticket ticket = ticketRepository.findById(query.getId())
				.orElseThrow(() -> new Exception("Ticket not found: " + query.getId()));
		BeanUtils.copyProperties(ticket, ticketResponseModel);
		return ticketResponseModel;
	}
}