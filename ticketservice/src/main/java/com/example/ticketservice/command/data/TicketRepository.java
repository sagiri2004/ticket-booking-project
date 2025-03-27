package com.example.ticketservice.command.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TicketRepository extends JpaRepository<Ticket, String> {
	@Modifying
	@Query("""
    UPDATE Ticket t 
    SET t.remainingQuantity = t.remainingQuantity - :quantity, 
        t.status = CASE WHEN (t.remainingQuantity - :quantity) = 0 THEN 'SOLD_OUT' ELSE t.status END
    WHERE t.id = :id AND t.remainingQuantity >= :quantity
""")
	int updateRemainingQuantity(@Param("id") String id, @Param("quantity") int quantity);
}
