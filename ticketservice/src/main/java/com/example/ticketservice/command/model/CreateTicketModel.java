package com.example.ticketservice.command.model;

import lombok.Data;

@Data
public class CreateTicketModel {
	private String name;
	private double price;
	private int totalQuantity;
	private int remainingQuantity;
}