package com.example.bookingservice.command.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
	@Id
	private String id;

	private String userId;
	private String ticketId;
	private int quantity;

	@Enumerated(EnumType.STRING)
	private BookingStatus status;
}
