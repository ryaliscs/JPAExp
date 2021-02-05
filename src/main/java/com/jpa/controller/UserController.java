package com.jpa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jpa.model.User;
import com.jpa.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class UserController {
	
	@Autowired
	UserRepository usrRepository;
	
	@GetMapping("/users")
	public List<User> getAllUsers()
	{
		return this.usrRepository.findAll();
	}
	
	@RequestMapping(value = "/user/{name}",   produces = "application/json", method =  RequestMethod.GET)
	public ResponseEntity<User> findByName(@PathVariable("name") String name){
		User user = this.usrRepository.findByLastName(name);
		if(user!=null ) {
			return new ResponseEntity<User>(user, HttpStatus.FOUND);
		}
		return new ResponseEntity<User>(user, HttpStatus.NO_CONTENT);
	}
	
	@PostMapping(value = "/user")
	public ResponseEntity<User> createUser(@RequestBody User user){
		User newUser = this.usrRepository.save(user);
		return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
	}

}
