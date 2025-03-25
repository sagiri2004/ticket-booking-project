package com.example.ticketservice.query.event;

import com.example.ticketservice.command.data.TicketRepository;
import com.example.ticketservice.command.event.TicketCreatedEvent;
import com.example.ticketservice.command.event.TicketDeletedEvent;
import com.example.ticketservice.command.event.TicketQuantityUpdatedEvent;
import com.example.ticketservice.command.event.TicketUpdatedEvent;
import com.example.ticketservice.query.model.TicketResponseModel;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class TicketQueryEventsHandler {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private TicketRepository ticketRepository;

	@EventHandler
	public void on(TicketCreatedEvent event) {
		// Xóa cache danh sách tất cả ticket khi có ticket mới
		redisTemplate.delete("tickets::all_tickets");
		System.out.println("🗑️ Deleted cache for all tickets (Ticket Created)");
	}

	@EventHandler
	public void on(TicketUpdatedEvent event) {
		String ticketCacheKey = "tickets::" + event.getId();
		redisTemplate.delete(ticketCacheKey);
		redisTemplate.delete("tickets::all_tickets");
		System.out.println("🗑️ Deleted cache for ticket ID: " + event.getId());

		// Cập nhật cache nếu ticket tồn tại trong DB
		ticketRepository.findById(event.getId()).ifPresent(ticket -> {
			TicketResponseModel ticketModel = new TicketResponseModel();
			BeanUtils.copyProperties(ticket, ticketModel);
			redisTemplate.opsForValue().set(ticketCacheKey, ticketModel);
			System.out.println("✅ Updated cache for ticket ID: " + event.getId());
		});
	}

	@EventHandler
	public void on(TicketDeletedEvent event) {
		String ticketCacheKey = "tickets::" + event.getId();
		redisTemplate.delete(ticketCacheKey);
		redisTemplate.delete("tickets::all_tickets");
		System.out.println("🗑️ Deleted cache for ticket ID: " + event.getId() + " (Ticket Deleted)");
	}

	@EventHandler
	public void on(TicketQuantityUpdatedEvent event) {
		String ticketCacheKey = "tickets::" + event.getId();
		redisTemplate.delete(ticketCacheKey);
		redisTemplate.delete("tickets::all_tickets");
		System.out.println("🗑️ Deleted cache for ticket ID: " + event.getId() + " (Booking Created)");

		// Cập nhật cache nếu ticket vẫn còn trong DB
		ticketRepository.findById(event.getId()).ifPresent(ticket -> {
			TicketResponseModel ticketModel = new TicketResponseModel();
			BeanUtils.copyProperties(ticket, ticketModel);
			redisTemplate.opsForValue().set(ticketCacheKey, ticketModel);
			System.out.println("✅ Updated cache for ticket ID: " + event.getId());
		});
	}
}