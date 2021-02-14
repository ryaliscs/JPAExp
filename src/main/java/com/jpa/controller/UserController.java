package com.jpa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jpa.model.User;
import com.jpa.repository.UserRepository;
import com.jpa.search.SearchCriteria;
import com.jpa.service.UserService;

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
	
	@RequestMapping(path = "/user", method = RequestMethod.POST)
	public ResponseEntity<User> createUser(@RequestBody User user) {
		return this.userService.createUser(user);
	}

	@RequestMapping(path = "/user", method = RequestMethod.PUT)
	public ResponseEntity<User> updateUser(@RequestBody User user) {
		return this.userService.updateUser(user);
	}
	
	@GetMapping(path = "/user")
	public ResponseEntity<User> findUserById(@RequestParam(value = "id") Long id) {
		return this.userService.getUser(id);
	}

	@GetMapping(path = "/user-last-name")
	public ResponseEntity<User> findByLastName(@RequestParam(value = "lastname") String lastname) {
		return this.userService.findByLastName(lastname);
	}

	@GetMapping(path = "/user-search")
	public ResponseEntity<Object> searchUser(@RequestBody SearchCriteria searchCriteria) {
		return this.userService.searchUser(searchCriteria);
	}
}
