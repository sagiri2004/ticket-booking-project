package com.example.ticketservice.command.event;

import com.example.ticketservice.command.data.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketCreatedEvent {
	private String id;
	private String name;
	private Double price;
	private Integer totalQuantity;
	private Integer remainingQuantity;
	private TicketStatus status;
}
