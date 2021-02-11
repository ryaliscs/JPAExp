package com.jpa.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.Optional;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpa.model.User;
import com.jpa.repository.UserRepository;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UserRepository usrRepository;

	@Autowired
	WebApplicationContext webApplicationContext;

	protected void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

	protected <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	@Test
	public void testCreateUser() throws Exception {
		User user = new User("test1", "test11", "test11@gmail.com");
		String inputInJson = this.mapToJson(user);

		Mockito.when(usrRepository.save(Mockito.any(User.class))).thenReturn(user);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/user").accept(MediaType.APPLICATION_JSON)
				.content(inputInJson).contentType(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
		User mapFromJson = mapFromJson(response.getContentAsString(), User.class);
		assertTrue(mapFromJson.equals(user));
	}
	
	@Test
	public void testCreateUserFail() throws Exception {
		User user = new User("test1", "test11", "test11@gmail.com");
		String inputInJson = this.mapToJson(user);

		User spyUser = Mockito.spy(user);
		Mockito.doReturn(11l).when(spyUser).getId();
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/user").accept(MediaType.APPLICATION_JSON)
				.content(inputInJson).contentType(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
		
	}
	
	@Test
	public void testUpdateUser() throws Exception {
		User user = new User("test1", "test11", "test11@gmail.com");
		String inputInJson = this.mapToJson(user);

		// ID is null in tests
		Mockito.when(usrRepository.findById(null)).thenReturn(Optional.of(user));		
		Mockito.when(usrRepository.save(Mockito.any(User.class))).thenReturn(user);		

		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/user").accept(MediaType.APPLICATION_JSON)
				.content(inputInJson).contentType(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
		User mapFromJson = mapFromJson(response.getContentAsString(), User.class);
		assertTrue(mapFromJson.equals(user));
	}
	
	
	@Test
	public void testUpdateUserFail() throws Exception {
		User user = new User("test1", "test11", "test11@gmail.com");
		String inputInJson = this.mapToJson(user);

		Mockito.when(usrRepository.findById(anyLong())).thenReturn(Optional.empty());		
		//Mockito.when(usrRepository.save(Mockito.any(User.class))).thenReturn(user);		

		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/user").accept(MediaType.APPLICATION_JSON)
				.content(inputInJson).contentType(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
	}

	@Test
	public void testFindUserByLastName() throws Exception {
		User user = new User("test1", "test11", "test11@gmail.com");

		Mockito.when(usrRepository.findByLastName("test11")).thenReturn(user);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user?lastname=test11")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mvc.perform(requestBuilder).andReturn();

		Assert.assertEquals(HttpStatus.FOUND.value(), result.getResponse().getStatus());
		String content = result.getResponse().getContentAsString();
		User mapFromJson = mapFromJson(content, User.class);
		assertTrue(mapFromJson.equals(user));
	}
	
	@Test
	public void testFindUserByLastNameFail() throws Exception {

		Mockito.when(usrRepository.findByLastName(anyString())).thenReturn(null);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user?lastname=test11")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mvc.perform(requestBuilder).andReturn();

		Assert.assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
	}

}
