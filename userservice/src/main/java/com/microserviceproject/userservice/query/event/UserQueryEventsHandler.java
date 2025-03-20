package com.microserviceproject.userservice.query.event;

import com.microserviceproject.userservice.command.event.UserCreatedEvent;
import com.microserviceproject.userservice.command.event.UserUpdatedEvent;
import com.microserviceproject.userservice.command.event.UserDeletedEvent;
import com.microserviceproject.userservice.query.model.UserResponseModel;
import com.microserviceproject.userservice.command.data.UserRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserQueryEventsHandler {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private UserRepository userRepository;

	@EventHandler
	public void on(UserCreatedEvent event) {
		redisTemplate.delete("users::all_users");
		System.out.println("🗑️ Deleted cache for all users (User Created)");
	}

	@EventHandler
	public void on(UserUpdatedEvent event) {
		String userCacheKey = "users::" + event.getId();
		redisTemplate.delete(userCacheKey);
		redisTemplate.delete("users::all_users");
		System.out.println("🗑️ Deleted cache for user ID: " + event.getId());

		userRepository.findById(event.getId()).ifPresent(user -> {
			UserResponseModel userModel = new UserResponseModel();
			org.springframework.beans.BeanUtils.copyProperties(user, userModel);
			redisTemplate.opsForValue().set(userCacheKey, userModel);
			System.out.println("✅ Updated cache for user ID: " + event.getId());
		});
	}

	@EventHandler
	public void on(UserDeletedEvent event) {
		String userCacheKey = "users::" + event.getId();
		redisTemplate.delete(userCacheKey);
		redisTemplate.delete("users::all_users");
		System.out.println("🗑️ Deleted cache for user ID: " + event.getId() + " (User Deleted)");
	}
}
