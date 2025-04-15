package com.example.bookingservice.query.model;

import com.example.bookingservice.command.data.BookingStatus;
import lombok.Data;

import java.io.Serializable;

@Data
public class BookingResponseModel implements Serializable {
	private String id;
	private String userId;
	private String ticketId;
	private int quantity;
	private BookingStatus status;
}
