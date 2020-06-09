package com.homemanagement.repositories;

import com.homemanagement.domain.MongoOAuth2ClientToken;

public interface OAuthClientTokenRepositoryBase {
    boolean deleteByAuthenticationId(String authenticationId);

    MongoOAuth2ClientToken findByAuthenticationId(String authenticationId);
}
