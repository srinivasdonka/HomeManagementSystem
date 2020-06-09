package com.homemanagement.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.homemanagement.domain.User;

public interface UserRepository extends MongoRepository<User, String>, UserRepositoryBase {

	void deleteByUsername(String username);

	Optional<User> findByUsername(String username);

}
