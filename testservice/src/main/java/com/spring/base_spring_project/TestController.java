package com.spring.base_spring_project;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tests")
public class TestController {
	@GetMapping
	public String test() {
		return "test";
	}
}
