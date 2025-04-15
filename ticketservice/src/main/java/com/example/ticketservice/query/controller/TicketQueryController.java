package com.example.ticketservice.query.controller;

import com.example.ticketservice.query.model.TicketResponseModel;
import com.example.ticketservice.query.queries.GetAllTicketsQuery;
import com.example.ticketservice.query.queries.GetTicketByIdQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketQueryController {

	@Autowired
	private QueryGateway queryGateway;

	@GetMapping
	public CompletableFuture<List<TicketResponseModel>> getAllTickets() {
		return queryGateway.query(new GetAllTicketsQuery(),
				ResponseTypes.multipleInstancesOf(TicketResponseModel.class));
	}

	@GetMapping("/{ticketId}")
	public CompletableFuture<TicketResponseModel> getTicketById(@PathVariable String ticketId) {
		return queryGateway.query(new GetTicketByIdQuery(ticketId), TicketResponseModel.class);
	}
}