package com.homemanagement.authentication.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.homemanagement.domain.User;
import com.homemanagement.repositories.UserDetailsDecorator;

@Service
public class UserAuthenticationDetailsService implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationDetailsService.class);
	private final UserAuthService userService;

	@Autowired
	public UserAuthenticationDetailsService(UserAuthService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetailsDecorator loadUserByUsername(String email) throws UsernameNotFoundException {
		LOGGER.debug("Authenticating user with email={}", email.replaceFirst("@.*", "@***"));
		User user = userService.getUserByEmail(email).orElseThrow(
				() -> new UsernameNotFoundException(String.format("User with email=%s was not found", email)));
		return new UserDetailsDecorator(user);
	}

}
