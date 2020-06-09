package com.homemanagement.repositories;

import java.util.List;

import com.homemanagement.domain.MongoOAuth2AccessToken;

public interface OAuthAccessTokenRepositoryBase {
	MongoOAuth2AccessToken findByTokenId(String tokenId);

    boolean deleteByTokenId(String tokenId);

    boolean deleteByRefreshTokenId(String refreshTokenId);

    MongoOAuth2AccessToken findByAuthenticationId(String key);

    List<MongoOAuth2AccessToken> findByUsernameAndClientId(String username, String clientId);

    List<MongoOAuth2AccessToken> findByClientId(String clientId);
    
    public void addClientDetails(MongoOAuth2AccessToken mongoOAuth2AccessToken);
}
