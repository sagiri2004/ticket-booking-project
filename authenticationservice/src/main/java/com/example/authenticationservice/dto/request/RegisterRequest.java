package com.example.authenticationservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.HashSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
	String username;
	String password;
	String name;
	String email;
	@Builder.Default
	Set<String> roles = new HashSet<>(Set.of("USER"));
}
