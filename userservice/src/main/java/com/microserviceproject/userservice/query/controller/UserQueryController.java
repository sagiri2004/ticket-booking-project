package com.microserviceproject.userservice.query.controller;

import com.microserviceproject.userservice.query.model.UserResponseModel;
import com.microserviceproject.userservice.query.queries.GetAllUserQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Query")
@Slf4j
public class UserQueryController {
	@Autowired
	private QueryGateway queryGateway;

	@Operation(
			summary = "Get List of Users",
			description = "Fetch all users",
			responses = {
					@ApiResponse(responseCode = "200", description = "Success"),
					@ApiResponse(responseCode = "401", description = "Unauthorized")
			}
	)
	@GetMapping
	public List<UserResponseModel> getAllUsers() {
		log.info("Fetching all users");

		return queryGateway
				.query(new GetAllUserQuery(), ResponseTypes.multipleInstancesOf(UserResponseModel.class))
				.join();
	}
}
