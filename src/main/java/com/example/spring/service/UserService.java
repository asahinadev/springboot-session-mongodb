package com.example.spring.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.spring.entity.User;
import com.example.spring.form.UserForm;
import com.example.spring.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService
		implements UserDetailsService {

	public static final int START_PAGE = 0;

	public static final int DEFAULT_SIZE = 20;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {

		Objects.requireNonNull(username, "username is null");

		return Optional
				.<User>ofNullable(userRepository.findByUsername(username))
				.orElse(userRepository.findByEmail(username));
	}

	public Page<User> findAll() {
		return findAll(PageRequest.of(START_PAGE, DEFAULT_SIZE, Sort.by(Order.asc("id"))));
	}

	public Page<User> findAll(Pageable page) {
		return userRepository.findAll(page);
	}

	public User findById(String id) {
		return logging(userRepository.findById(id).orElseThrow());
	}

	public User findByUsername(String username) {
		return logging(userRepository.findByUsername(username));
	}

	public User findByEmail(String email) {
		return logging(userRepository.findByEmail(email));
	}

	private User logging(User user) {
		log.debug("user => {}", user);
		return user;
	}

	public User insert(UserForm form) {
		User user = new User();
		BeanUtils.copyProperties(form, user, "password");
		user.setId(UUID.randomUUID().toString());
		user.setEnabled(true);
		if (!StringUtils.isEmpty(form.getPassword())) {
			user.setPassword(form.getPassword());
			changePassword(user);
		}
		return userRepository.insert(user);
	}

	public User save(UserForm form, User user) {
		BeanUtils.copyProperties(form, user, "password");
		if (!StringUtils.isEmpty(form.getPassword())) {
			user.setPassword(form.getPassword());
			changePassword(user);
		}
		return userRepository.save(user);
	}

	public void delete(User entity) {

		User old = userRepository.findById(entity.getId()).orElseThrow();

		if (!StringUtils.equals(entity.getPassword(), old.getPassword())) {
			changePassword(entity);
		}

		userRepository.delete(entity);
	}

	public List<User> insert(List<User> entities) {
		entities = entities.stream()
				.map(entity -> {
					entity.setId(UUID.randomUUID().toString());
					changePassword(entity);
					return entity;
				}).collect(Collectors.toList());
		return userRepository.insert(entities);
	}

	public List<User> saveAll(List<User> entities) {
		entities = entities.stream()
				.map(entity -> {
					User old = userRepository.findById(entity.getId()).orElseThrow();
					if (!StringUtils.equals(entity.getPassword(), old.getPassword())) {
						changePassword(entity);
					}
					return entity;
				}).collect(Collectors.toList());
		return userRepository.saveAll(entities);
	}

	private void changePassword(User entity) {
		entity.setPassword(passwordEncoder.encode(entity.getPassword()));
		entity.setAccountExpired(LocalDateTime.now().plusDays(60));
		entity.setCredentialsExpired(LocalDateTime.now().plusDays(30));
	}

}
