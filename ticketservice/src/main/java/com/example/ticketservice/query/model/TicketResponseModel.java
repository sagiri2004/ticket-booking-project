package com.example.ticketservice.query.model;

import lombok.Data;

@Data
public class TicketResponseModel {
	private String ticketId;
	private String name;
	private double price;
	private int totalQuantity;
	private int remainingQuantity;
	private String status;
}
