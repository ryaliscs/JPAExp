package com.jpa;

import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.jpa.builder.query.QueryBuilder;
import com.jpa.model.User;
import com.jpa.repository.UserRepository;
import com.jpa.search.SearchCriteria;

@Service
public class UserService {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	UserRepository usrRepository;
	

	/**
	 * Creates the User if new, otherwise returns CONFLICT
	 * 
	 * @param user user to be created
	 * @return the user created with id, otherwise CONFLICT
	 */
	public ResponseEntity<User> createUser(User user) {
		if (!isValidUser(user)) {
			return new ResponseEntity<User>(HttpStatus.CONFLICT);
		}
		User newUser = this.usrRepository.save(user);
		return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
	}

	/**
	 * Returns the user by given id, if found
	 * 
	 * @param id ID of the user
	 * @return the user if found, otherwise returns BAD_REQUEST
	 */
	public ResponseEntity<User> getUser(Long id) {
		Optional<User> user = this.usrRepository.findById(id);
		if (user.isPresent()) {
			return new ResponseEntity<User>(user.get(), HttpStatus.FOUND);
		}
		return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Update the given info
	 * 
	 * @param user
	 * @return the updated user, if found, other returns as CONFLICT
	 */
	public ResponseEntity<User> updateUser(User user) {
		Optional<User> findUserById = this.usrRepository.findById(user.getId());
		if (!findUserById.isPresent()) {
			return new ResponseEntity<User>(HttpStatus.UNPROCESSABLE_ENTITY);
		}

		User newUser = this.usrRepository.save(user);
		return new ResponseEntity<User>(newUser, HttpStatus.ACCEPTED);
	}

	/**
	 * findByLastName: searches the users table by the given last name
	 * 
	 * @param lastname lastName of the user to be searched
	 * @return the User if found, otherwise returns NOT_FOUND
	 */
	public ResponseEntity<User> findByLastName(String lastname) {
		User user = this.usrRepository.findByLastName(lastname);
		if (user != null) {
			return new ResponseEntity<User>(user, HttpStatus.FOUND);
		}
		return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
	}

	/**
	 * searchUser: Search the User by the given SearchCriteria
	 * 
	 * @param searchCriteria
	 * @return the list of Users found by search, can be empty or BAD_REQUEST if the
	 *         search operator is not supported
	 */
	public ResponseEntity<Object> searchUser(SearchCriteria searchCriteria) {
		try {
			QueryBuilder<User> qb = new QueryBuilder<User>(User.class, searchCriteria);
			return new ResponseEntity<Object>(qb.getQuery(this.sessionFactory.openSession()).getResultList(),
					HttpStatus.OK);
		} catch (IllegalArgumentException ex) {
			return new ResponseEntity<Object>("UnSupported Operator " + searchCriteria.getOp(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Validates the user.
	 * 
	 * <pre>
	 * User is valid if:
	 * 1. there is no ID
	 * 2. the email is  not already registered
	 * 3. firstName, lastName and email is not null
	 * </pre>
	 * 
	 * @param user
	 * @return
	 */
	private boolean isValidUser(User user) {
		if (user.getId() != null) {
			return false;
		}

		if (user.getFirstName() == null || user.getLastName() == null || user.getEmail() == null) {
			return false;
		}
		User userByEmail = this.usrRepository.findByEmail(user.getEmail());

		if (userByEmail != null) {
			return false;
		}

		return true;
	}
}
