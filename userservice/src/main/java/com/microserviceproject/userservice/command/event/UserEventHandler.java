package com.microserviceproject.userservice.command.event;

import com.microserviceproject.userservice.command.data.User;
import com.microserviceproject.userservice.command.data.UserRepository;
import org.axonframework.eventhandling.DisallowReplay;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserEventHandler {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@EventHandler
	public void on(UserCreatedEvent event) {
		User user = new User();
		BeanUtils.copyProperties(event, user);

		user.setPassword(passwordEncoder.encode(event.getPassword()));

		userRepository.save(user);
	}

	@EventHandler
	public void on(UserUpdatedEvent event) throws Exception {
		Optional<User> oldUser = userRepository.findById(event.getId());
		User user = oldUser.orElseThrow(() -> new Exception("User not found"));

		user.setUsername(event.getUsername());

		if (event.getPassword() != null && !event.getPassword().isEmpty()) {
			user.setPassword(passwordEncoder.encode(event.getPassword()));
		}

		userRepository.save(user);
	}

	@EventHandler
	@DisallowReplay
	public void on(UserDeletedEvent event) {
		try {
			userRepository.findById(event.getId()).orElseThrow(() -> new Exception("User not found"));
			userRepository.deleteById(event.getId());
		} catch (Exception ex) {
			// log.error(ex.getMessage());
		}
	}
}
