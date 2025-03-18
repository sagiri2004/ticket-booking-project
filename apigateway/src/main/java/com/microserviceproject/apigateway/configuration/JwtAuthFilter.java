package com.microserviceproject.apigateway.configuration;

import com.microserviceproject.apigateway.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

	@Autowired
	private JwtUtil jwtUtil;

	public JwtAuthFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();

			log.info("Request URI : {}", request.getURI());

			// Kiểm tra xem header Authorization có tồn tại không
			if (!request.getHeaders().containsKey("Authorization")) {
				return handleUnauthorized(exchange, "Missing authorization header");
			}

			// Lấy token từ header Authorization
			String authHeader = request.getHeaders().getFirst("Authorization");
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				return handleUnauthorized(exchange, "Invalid token format");
			}

			// Bỏ "Bearer " prefix để lấy JWT
			String token = authHeader.substring(7);

			try {
				// Kiểm tra token hợp lệ
				if (!jwtUtil.validateToken(token)) {
					return handleUnauthorized(exchange, "Invalid token");
				}

				// Trích xuất thông tin user từ token
				String userId = jwtUtil.extractUserId(token);
				String username = jwtUtil.extractUsername(token);

				// Tạo request mới và gắn thông tin user vào header
				ServerHttpRequest modifiedRequest = request.mutate()
						.header("X-Auth-User-Id", userId)
						.header("X-Auth-Username", username)
						.build();

				return chain.filter(exchange.mutate().request(modifiedRequest).build());

			} catch (Exception e) {
				log.error("JWT validation error: {}", e.getMessage());
				return handleUnauthorized(exchange, "Token validation failed");
			}
		};
	}

	private Mono<Void> handleUnauthorized(ServerWebExchange exchange, String message) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		String errorResponse = String.format(
				"{\"timestamp\": \"%s\", \"status\": %d, \"error\": \"%s\", \"message\": \"%s\", \"path\": \"%s\"}",
				java.time.ZonedDateTime.now(),
				HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(),
				message, exchange.getRequest().getURI().getPath()
		);
		return response.writeWith(Mono.just(response.bufferFactory().wrap(errorResponse.getBytes())));
	}

	public static class Config {
	}
}
