package com.jpa.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ObjectController {

	@RequestMapping("/")
	public String index() {
		return "Welcome to Sprint Boot JPA";
	}
}
