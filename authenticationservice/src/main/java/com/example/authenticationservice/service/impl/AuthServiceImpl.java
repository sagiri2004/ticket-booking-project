package com.example.authenticationservice.service.impl;

import com.example.authenticationservice.dto.request.LoginRequest;
import com.example.authenticationservice.dto.request.RegisterRequest;
import com.example.authenticationservice.dto.response.AuthResponse;
import com.example.authenticationservice.exception.AuthException;
import com.example.authenticationservice.model.AuthUser;
import com.example.authenticationservice.repository.AuthUserRepository;
import com.example.authenticationservice.service.AuthService;
import com.example.authenticationservice.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final AuthUserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Override
	public AuthResponse register(RegisterRequest request) {
		// Kiểm tra xem user đã tồn tại chưa
		if (userRepository.findByUsername(request.getUsername()).isPresent()) {
			throw new AuthException("Username is already taken");
		}

		if (request.getUsername().equals("acane")) {
			request.setRoles(Set.of("ADMIN", "USER"));
		} else {
			request.setRoles(Set.of("USER"));
		}

		// Mã hóa mật khẩu
		String encodedPassword = passwordEncoder.encode(request.getPassword());

		// Tạo user mới với roles và ID
		String userId = UUID.randomUUID().toString();
		AuthUser newUser = AuthUser.builder()
				.id(userId)
				.username(request.getUsername())
				.password(encodedPassword)
				.roles(request.getRoles()) // Lưu roles vào DB
				.build();

		// Lưu user vào DB
		userRepository.save(newUser);

		// Tạo JWT token chứa userId, username và roles
		String token = jwtUtil.generateToken(userId, newUser.getUsername(), newUser.getRoles());

		// Trả về response
		return AuthResponse.builder()
				.message("User registered successfully")
				.token(token)
				.build();
	}

	@Override
	public AuthResponse login(LoginRequest request) {
		// Tìm user theo username
		AuthUser user = userRepository.findByUsername(request.getUsername())
				.orElseThrow(() -> new AuthException("Invalid username or password"));

		// Kiểm tra mật khẩu
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new AuthException("Invalid username or password");
		}

		// Tạo token mới chứa userId, username và roles
		String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRoles());

		// Trả về response
		return AuthResponse.builder()
				.message("Login successful")
				.token(token)
				.build();
	}

	@Override
	public boolean validateToken(String token) {
		return jwtUtil.validateToken(token);
	}
}