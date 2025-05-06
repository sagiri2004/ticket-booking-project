package com.example.ticketservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TicketserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketserviceApplication.class, args);
	}

}
