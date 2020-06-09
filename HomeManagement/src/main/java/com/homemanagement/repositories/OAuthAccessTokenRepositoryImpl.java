package com.homemanagement.repositories;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.homemanagement.domain.MongoOAuth2AccessToken;
import com.mongodb.client.result.DeleteResult;

@Component
public class OAuthAccessTokenRepositoryImpl implements OAuthAccessTokenRepositoryBase {

    public static final String ID = "_id";
    private final MongoTemplate mongoTemplate;

    public OAuthAccessTokenRepositoryImpl(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public MongoOAuth2AccessToken findByTokenId(final String tokenId) {
        final Query query = Query.query(Criteria.where(ID).is(tokenId));
        return mongoTemplate.findOne(query, MongoOAuth2AccessToken.class);
    }

    @Override
    public boolean deleteByTokenId(final String tokenId) {
        final Query query = Query.query(Criteria.where(ID).is(tokenId));
        final DeleteResult deleteResult = mongoTemplate.remove(query, MongoOAuth2AccessToken.class);
        return deleteResult.wasAcknowledged();
    }

    @Override
    public boolean deleteByRefreshTokenId(String refreshTokenId) {
        final Query query = Query.query(Criteria.where("refreshToken").is(refreshTokenId));
        final DeleteResult deleteResult = mongoTemplate.remove(query, MongoOAuth2AccessToken.class);
        return deleteResult.wasAcknowledged();
    }

    @Override
    public MongoOAuth2AccessToken findByAuthenticationId(String key) {
        final Query query = Query.query(Criteria.where("authenticationId").is(key));
        return mongoTemplate.findOne(query, MongoOAuth2AccessToken.class);
    }

    @Override
    public List<MongoOAuth2AccessToken> findByUsernameAndClientId(final String username,
                                                                  final String clientId) {
        final Query query = Query.query(Criteria.where("username").is(username).andOperator(Criteria.where("clientId").is(clientId)));
        return mongoTemplate.find(query, MongoOAuth2AccessToken.class);
    }

    @Override
    public List<MongoOAuth2AccessToken> findByClientId(final String clientId) {
        final Query query = Query.query(Criteria.where("clientId").is(clientId));
        return mongoTemplate.find(query, MongoOAuth2AccessToken.class);
    }
    
    @Override
    public void addClientDetails(MongoOAuth2AccessToken mongoOAuth2AccessToken) {
    	mongoTemplate.save(mongoOAuth2AccessToken);
    }
}
