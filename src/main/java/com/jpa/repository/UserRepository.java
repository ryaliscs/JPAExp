package com.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByLastName(String name);
}
