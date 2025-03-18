package com.example.authenticationservice.dto.response;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
	private String message;
	private String token;
}
