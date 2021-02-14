package com.jpa.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.jpa.builder.query.QueryBuilder;
import com.jpa.model.User;
import com.jpa.repository.UserRepository;
import com.jpa.search.LIOperator;
import com.jpa.search.SCOperator;
import com.jpa.search.SearchCriteria;
import com.jpa.search.SearchNode;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest extends BaseTestController {

	@MockBean
	private UserRepository usrRepository;

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

	// @Test
	// @TODO fix the test
	public void testCreateUserFail() throws Exception {
		User user = new User("test1", "test11", "test11@gmail.com");
		String inputInJson = this.mapToJson(user);

		User spyUser = Mockito.spy(User.class);
		Mockito.doReturn(anyLong()).when(spyUser).getId();

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
		// Mockito.when(usrRepository.save(Mockito.any(User.class))).thenReturn(user);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/user").accept(MediaType.APPLICATION_JSON)
				.content(inputInJson).contentType(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
	}

	@Test
	public void testFindUserByLastName() throws Exception {
		User user = new User("test1", "test11", "test11@gmail.com");

		Mockito.when(usrRepository.findByLastName("test11")).thenReturn(user);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user-last-name?lastname=test11")
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

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user-last-name?lastname=test11")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mvc.perform(requestBuilder).andReturn();

		Assert.assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
	}

	@Test
	public void testUserBySearchCriteria() throws Exception {
		User user = new User("test1", "test11", "test11@gmail.com");
		SearchNode node = new SearchNode("firstName", "test1", LIOperator.EQUAL.name());
		SearchCriteria sc = new SearchCriteria(List.of(node), SCOperator.AND);
	
		QueryBuilder<User> qb = Mockito.mock(QueryBuilder.class);
		Mockito.when(qb.getResult(Mockito.any(EntityManager.class))).thenReturn(List.of(user));
		String scString = mapToJson(sc);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user-search")
				.accept(MediaType.APPLICATION_JSON).content(scString).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mvc.perform(requestBuilder).andReturn();
		Assert.assertEquals(HttpStatus.FOUND.value(), result.getResponse().getStatus());
	}

}
