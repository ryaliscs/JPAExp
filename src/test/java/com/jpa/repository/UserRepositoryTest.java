package com.jpa.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.jpa.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserRepository usrRepository;

	private User createUser(int suffix) {
		User user = new User("first_" + suffix, "last_" + suffix, "email" + suffix + "@mail");
		user = this.entityManager.persist(user);
		assertNotNull(user.getId());
		return user;
	}

	@Test
	public void testGetUser() {
		createUser(10);
		User user = this.usrRepository.findByLastName("last_10");
		assertNotNull(user);
		assertEquals("email10@mail", user.getEmail());
	}
}
