package com.example.ticketservice.query.model;

import com.example.ticketservice.command.data.TicketStatus;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TicketResponseModel  implements Serializable {
	private String id;
	private String userId;
	private String name;
	private Date startDate;
	private Double price;
	private int totalQuantity;
	private int remainingQuantity;
	private TicketStatus status;
}
