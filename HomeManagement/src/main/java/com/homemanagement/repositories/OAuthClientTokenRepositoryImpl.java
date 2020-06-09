package com.homemanagement.repositories;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.homemanagement.domain.MongoOAuth2ClientToken;
import com.mongodb.client.result.DeleteResult;

@Component
public class OAuthClientTokenRepositoryImpl implements OAuthClientTokenRepositoryBase {

    private final MongoTemplate mongoTemplate;

    public OAuthClientTokenRepositoryImpl(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean deleteByAuthenticationId(final String authenticationId) {
        final Query query = Query.query(Criteria.where("authenticationId").is(authenticationId));
        final DeleteResult deleteResult = mongoTemplate.remove(query, MongoOAuth2ClientToken.class);
        return deleteResult.wasAcknowledged();
    }

    @Override
    public MongoOAuth2ClientToken findByAuthenticationId(final String authenticationId) {
        final Query query = Query.query(Criteria.where("authenticationId").is(authenticationId));
        return mongoTemplate.findOne(query, MongoOAuth2ClientToken.class);
    }
}
