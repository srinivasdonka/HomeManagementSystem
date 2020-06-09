package com.homemanagement.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.homemanagement.domain.MongoOAuth2RefreshToken;

public interface OAuthRefreshTokenRepository extends MongoRepository<MongoOAuth2RefreshToken, String>, OAuthRefreshTokenRepositoryBase {
}
