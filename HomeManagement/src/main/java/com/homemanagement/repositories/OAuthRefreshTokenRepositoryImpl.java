package com.homemanagement.repositories;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.homemanagement.domain.MongoOAuth2RefreshToken;
import com.mongodb.client.result.DeleteResult;

@Component
public class OAuthRefreshTokenRepositoryImpl implements OAuthRefreshTokenRepositoryBase {

	public static final String ID = "_id";
	private MongoTemplate mongoTemplate;

	public OAuthRefreshTokenRepositoryImpl(final MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public MongoOAuth2RefreshToken findByTokenId(final String tokenId) {
		final Query query = Query.query(Criteria.where(ID).is(tokenId));
		return mongoTemplate.findOne(query, MongoOAuth2RefreshToken.class);
	}

	@Override
	public boolean deleteByTokenId(final String tokenId) {
		final Query query = Query.query(Criteria.where(ID).is(tokenId));
		final DeleteResult deleteResult = mongoTemplate.remove(query, MongoOAuth2RefreshToken.class);
		return deleteResult.wasAcknowledged();
	}

	@Override
	public void addrefreshToken(MongoOAuth2RefreshToken refreshToken) {
		mongoTemplate.save(refreshToken);
	}
}
