package com.jpa.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jpa.model.User;
import com.jpa.search.SCOperator;
import com.jpa.search.SearchCriteria;
import com.jpa.search.SearchNode;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTest {

	@Autowired
	private TestEntityManager entityManager;

	UserService usrService;

	@BeforeEach
	public void init() {
		this.usrService = new UserService();
	}

	private User createUser(int suffix) {
		User user = new User("first_" + suffix, "last_" + suffix, "email" + suffix + "@mail");
		user = this.entityManager.persist(user);
		assertNotNull(user.getId());
		return user;
	}

	@Test
	public void testUserSearch_01() {
		User user1 = createUser(30);
		User user2 = createUser(31);
		

		List<SearchNode> nodes = new ArrayList();
		nodes.add(new SearchNode("firstName", "first_30", "EUQAL"));
		
		
		SearchCriteria sc = new SearchCriteria(nodes, SCOperator.AND);
		ResponseEntity<Object> searchUser = usrService.searchUser(sc);
		assertEquals(searchUser.getStatusCode().value(), HttpStatus.OK);
		assertTrue(user1.equals(searchUser.getBody()));
	}

}
