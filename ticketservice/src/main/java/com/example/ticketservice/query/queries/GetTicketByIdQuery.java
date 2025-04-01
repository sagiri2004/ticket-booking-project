package com.example.ticketservice.query.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTicketByIdQuery {
	private String id;
}
