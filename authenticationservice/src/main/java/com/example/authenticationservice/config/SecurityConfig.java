package com.example.authenticationservice.config;

import com.example.authenticationservice.security.CustomAccessDeniedHandler;
import com.example.authenticationservice.security.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	private final String[] PUBLIC_ENDPOINTS = {"/users",
			"/api/v1/auth/register", "/api/v1/auth/login", "/api/v1/auth/validate", "/api/v1/auth/nonuser", "/actuator/**", "/actuator/prometheus"
	};

	@Value("${jwt.signerKey}")
	private String signerKey;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.csrf(AbstractHttpConfigurer::disable) // Tắt CSRF
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Không sử dụng session
				.authorizeHttpRequests(auth -> auth
						// Cho phép truy cập public endpoints
						.requestMatchers(PUBLIC_ENDPOINTS).permitAll()

						// Chỉ admin mới có thể truy cập các endpoint quản trị
						.requestMatchers("/api/v1/admin/**").hasRole("ADMIN")

						// Chỉ user đã đăng nhập mới có thể truy cập endpoint này
						.requestMatchers("/api/v1/user/**").hasAnyRole("USER", "ADMIN")

						// Các request khác phải được xác thực
						.anyRequest().authenticated()
				)
				// Cấu hình xác thực bằng JWT
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))

				// Cấu hình xử lý lỗi
				.exceptionHandling(exception -> exception
						.authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // Xử lý lỗi 401
						.accessDeniedHandler(new CustomAccessDeniedHandler()) // Xử lý lỗi 403
				);

		return httpSecurity.build();
	}

	@Bean
	JwtDecoder jwtDecoder(){
		SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
		return NimbusJwtDecoder
				.withSecretKey(secretKeySpec)
				.macAlgorithm(MacAlgorithm.HS512)
				.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_"); // Prefix cho vai trò
		grantedAuthoritiesConverter.setAuthoritiesClaimName("roles"); // Đọc từ claim "roles"

		JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
		authenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

		return authenticationConverter;
	}
}
