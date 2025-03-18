package com.microserviceproject.userservice.query.projection;

import com.microserviceproject.userservice.command.data.User;
import com.microserviceproject.userservice.command.data.UserRepository;
import com.microserviceproject.userservice.query.model.UserResponseModel;
import com.microserviceproject.userservice.query.queries.GetAllUserQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserProjection {
	@Autowired
	private UserRepository userRepository;

	@QueryHandler
	public List<UserResponseModel> handle(GetAllUserQuery query) {
		List<User> users = userRepository.findAll();
		return users.stream().map(user -> {
			UserResponseModel model = new UserResponseModel();
			BeanUtils.copyProperties(user, model);
			return model;
		}).toList();
	}
}
