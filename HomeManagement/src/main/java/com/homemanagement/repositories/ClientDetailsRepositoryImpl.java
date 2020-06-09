package com.homemanagement.repositories;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.homemanagement.domain.MongoClientDetails;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

@Component
public class ClientDetailsRepositoryImpl implements ClientDetailsRepositoryBase {

    public static final String ID = "client_id";
    public static final String CLIENT_SECRET = "clientSecret";
    private final MongoTemplate mongoTemplate;

    public ClientDetailsRepositoryImpl(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean deleteByClientId(String clientId) {
        final Query query = Query.query(Criteria.where(ID).is(clientId));
        final DeleteResult deleteResult = mongoTemplate.remove(query, MongoClientDetails.class);
        return deleteResult.wasAcknowledged();
    }

    @Override
    public boolean update(final MongoClientDetails mongoClientDetails) {
        final Query query = Query.query(Criteria.where(ID).is(mongoClientDetails.getClientId()));

        final Update update = Update.update("scope", mongoClientDetails.getScope())
                .set("resourceIds", mongoClientDetails.getResourceIds())
                .set("authorizedGrantTypes", mongoClientDetails.getAuthorizedGrantTypes())
                .set("authorities", mongoClientDetails.getAuthorities())
                .set("accessTokenValiditySeconds", mongoClientDetails.getAccessTokenValiditySeconds())
                .set("refreshTokenValiditySeconds", mongoClientDetails.getRefreshTokenValiditySeconds())
                .set("additionalInformation", mongoClientDetails.getAdditionalInformation())
                .set("autoApproveScopes", mongoClientDetails.getAutoApproveScopes())
                .set("registeredRedirectUris", mongoClientDetails.getRegisteredRedirectUri());

        final UpdateResult updateResult = mongoTemplate.updateFirst(query, update, MongoClientDetails.class);

        return updateResult.wasAcknowledged();
    }

    @Override
    public boolean updateClientSecret(final String clientId,
                                      final String newSecret) {
        final Query query = Query.query(Criteria.where(ID).is(clientId));

        final Update update = Update.update(CLIENT_SECRET, newSecret);

        final UpdateResult updateResult = mongoTemplate.updateFirst(query, update, MongoClientDetails.class);

        return updateResult.wasAcknowledged();
    }

    @Override
    public MongoClientDetails findByClientId(final String clientId) {
        final Query query = Query.query(Criteria.where(ID).is(clientId));
        final MongoClientDetails mongoClientDetails = mongoTemplate.findOne(query, MongoClientDetails.class);
        if (mongoClientDetails == null) {
            throw new IllegalArgumentException("No valid client id");
        }
        return mongoClientDetails;
    }


}
