package com.jpa.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	
	@Test
	public void getAllUsers() throws Exception {
	
		mvc.perform(MockMvcRequestBuilders.get("/api/user?lastname=five").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isFound())
		.andExpect(content().json("{\"id\":14,\"firstName\":\"test5\",\"lastName\":\"five\",\"email\":\"test5@test.com\"}"));
	}
	
}
