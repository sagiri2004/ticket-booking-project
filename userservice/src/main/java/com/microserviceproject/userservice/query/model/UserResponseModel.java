package com.microserviceproject.userservice.query.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseModel implements Serializable {
	private String id;
	private String name;
	private String email;
	private Boolean active;
}
