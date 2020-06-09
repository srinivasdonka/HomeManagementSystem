package com.homemanagement.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.homemanagement.domain.MongoOAuth2ClientToken;

public interface OAuthClientTokenRepository extends MongoRepository<MongoOAuth2ClientToken, String>, OAuthClientTokenRepositoryBase {
}
