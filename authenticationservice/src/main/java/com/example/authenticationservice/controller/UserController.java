package com.example.authenticationservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

	@GetMapping("/user")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public String userAccess() {
		return "Welcome, User!";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Welcome, Admin!";
	}

	@GetMapping("/nonuser")
	@PreAuthorize("permitAll()")
	public String nonUserAccess() {
		return "Welcome, Non User!";
	}
}
