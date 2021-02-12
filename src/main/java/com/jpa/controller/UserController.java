package com.jpa.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jpa.UserService;
import com.jpa.model.User;
import com.jpa.repository.UserRepository;
import com.jpa.search.SearchCriteria;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	UserRepository usrRepository;
	
	@Autowired
	private UserService userService;

	@GetMapping(path = "/users")
	public List<User> getAllUsers() {
		return this.usrRepository.findAll();
	}

	@GetMapping(path = "/user")
	public ResponseEntity<User> findByName(@RequestParam(value = "lastname") String lastname) {
		User user = this.usrRepository.findByLastName(lastname);
		if (user != null) {
			return new ResponseEntity<User>(user, HttpStatus.FOUND);
		}
		return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path = "/user", method = RequestMethod.POST)
	public ResponseEntity<User> createUser(@RequestBody User user) {
		if (user.getId() != null) {
			return new ResponseEntity<User>(HttpStatus.CONFLICT);
		}
		User newUser = this.usrRepository.save(user);
		return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
	}

	@RequestMapping(path = "/user", method = RequestMethod.PUT)
	public ResponseEntity<User> updateUser(@RequestBody User user) {
		Optional<User> findUserById = this.usrRepository.findById(user.getId());
		if (!findUserById.isPresent()) {
			return new ResponseEntity<User>(HttpStatus.CONFLICT);
		}

		User newUser = this.usrRepository.save(user);
		return new ResponseEntity<User>(newUser, HttpStatus.ACCEPTED);
	}

	@GetMapping(path = "/search-user")
	public List<User> searchUser(@RequestBody SearchCriteria searchCriteria) {
		return this.userService.searchUser(searchCriteria);
	}
}
