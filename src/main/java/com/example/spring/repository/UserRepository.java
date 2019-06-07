package com.example.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.spring.entity.User;

public interface UserRepository
		extends MongoRepository<User, String> {

	public User findByUsername(String username);

	public User findByEmail(String email);
}
