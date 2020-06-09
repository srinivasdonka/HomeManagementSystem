package com.homemanagement.repositories;

import com.homemanagement.domain.MongoOAuth2RefreshToken;

public interface OAuthRefreshTokenRepositoryBase {
	
	MongoOAuth2RefreshToken findByTokenId(String tokenId);

    boolean deleteByTokenId(String tokenId);
    
    public void addrefreshToken(MongoOAuth2RefreshToken refreshToken);
    
}
