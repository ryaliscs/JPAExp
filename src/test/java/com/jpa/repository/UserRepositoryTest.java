package com.jpa.repository;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

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
	public void testUser_01() {
		createUser(10);
		User user = this.usrRepository.findByLastName("last_10");
		assertNotNull(user);
		assertEquals("email10@mail", user.getEmail());
	}

	@Test
	public void testUser_02() {
		User newUser = createUser(20);
		Optional<User> userO = this.usrRepository.findById(newUser.getId());
		assertTrue(userO.isPresent());
		compareResult(newUser, userO);
	}

	@Test
	public void testUpdateUser() {
		User newUser = createUser(50);
		Optional<User> userO = this.usrRepository.findById(newUser.getId());
		assertTrue(userO.isPresent());
		compareResult(newUser, userO);
		User user = userO.get();
		// update
		user.setFirstName(user.getFirstName() + "_100");
		user = this.usrRepository.save(user);
		// newUser is updated after save
		assertEquals(newUser.getFirstName(), user.getFirstName());
	}

	private void compareResult(User newUser, Optional<User> userO) {
		User user = userO.get();

		assertEquals(newUser.getFirstName(), user.getFirstName());
		assertEquals(newUser.getLastName(), user.getLastName());
		assertEquals(newUser.getEmail(), user.getEmail());
	}

}
