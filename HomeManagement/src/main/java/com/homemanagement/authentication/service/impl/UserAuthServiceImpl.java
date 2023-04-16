package com.homemanagement.authentication.service.impl;



import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.homemanagement.authentication.service.UserAuthService;
import com.homemanagement.domain.User;
import com.homemanagement.repositories.UserRepository;

@Service
public class UserAuthServiceImpl implements UserAuthService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthServiceImpl.class);
	private final UserRepository userRepository;
	@Autowired
	public UserAuthServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	@Override
	public Optional<User> getUserById(String id) {
		LOGGER.debug("Getting user={}", id);
		return userRepository.findById(id);
	}
	@Override
	public Optional<User> getUserByEmail(String email) {
		LOGGER.debug("Getting user by email={}", email.replaceFirst("@.*", "@***"));
		return userRepository.findByUsername(email);
	}
	@SuppressWarnings("deprecation")
	@Override
	public Collection<User> getAllUsers() {
		LOGGER.debug("Getting all users");
		return userRepository.findAll(new Sort("username"));
	}
}
