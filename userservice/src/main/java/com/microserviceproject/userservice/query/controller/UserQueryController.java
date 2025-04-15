package com.microserviceproject.userservice.query.controller;

import com.microserviceproject.userservice.query.model.UserResponseModel;
import com.microserviceproject.userservice.query.queries.GetAllUserQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserQueryController {
	@Autowired
	private QueryGateway queryGateway;

	@GetMapping
	public List<UserResponseModel> getAllUsers() {
		log.info("Fetching all users");

		return queryGateway
				.query(new GetAllUserQuery(), ResponseTypes.multipleInstancesOf(UserResponseModel.class))
				.join();
	}
}
