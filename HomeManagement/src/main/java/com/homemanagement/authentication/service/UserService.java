package com.homemanagement.authentication.service;



import java.util.Collection;
import java.util.Optional;

import com.homemanagement.domain.User;

public interface UserService {

	public Optional<User> getUserById(String id);

	public Optional<User> getUserByEmail(String email);

	public Collection<User> getAllUsers();
}
