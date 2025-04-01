package com.example.bookingservice.command.model;

import lombok.Data;

@Data
public class CreateBookingModel {
	private String userId;
	private String ticketId;
	private Integer quantity;
}