package com.example.bookingservice.command.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, String> {
	List<Booking> findByUserId(String userId);

	List<Booking> findByTicketId(String ticketId);
}
