package com.example.ticketservice.command.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket {
	@Id
	private String id;
	private String userId;
	private String name;

	@Temporal(TemporalType.DATE)
	private Date startDate;

	private Double price;
	private int totalQuantity;
	private int remainingQuantity;

	@Enumerated(EnumType.STRING)
	private TicketStatus status;
}
