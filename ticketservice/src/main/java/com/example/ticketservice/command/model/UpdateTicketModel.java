package com.example.ticketservice.command.model;

import lombok.Data;

@Data
public class UpdateTicketModel {
	private String name;
	private double price;
	private int totalQuantity;
	private int remainingQuantity;
	private String status;
}
