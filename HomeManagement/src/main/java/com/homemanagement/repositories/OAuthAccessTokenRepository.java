package com.homemanagement.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.homemanagement.domain.MongoOAuth2AccessToken;

public interface OAuthAccessTokenRepository extends MongoRepository<MongoOAuth2AccessToken, String>, OAuthAccessTokenRepositoryBase {

}
