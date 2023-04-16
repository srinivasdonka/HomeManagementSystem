package com.homemanagement.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(MongoDBSettings.class)
@ComponentScan(basePackages = {"com.homemanagement.security"})
@EnableMongoRepositories(basePackages = {"com.homemanagement.repositories"})
public class HomeManagementConfiguration {
	@Bean
	public MongoTemplate mongoTemplate(final MongoClient mongoClient,
			final MongoDBSettings mongoSettings) {
		return new MongoTemplate(mongoClient, mongoSettings.getDatabase());
	}

	@Configuration
	@EnableConfigurationProperties(MongoDBSettings.class)
	@ConditionalOnProperty({
		"mongo.uri",
		"mongo.host",
		"mongo.port",
		"mongo.database",
		"mongo.username",
	"mongo.password"})
	@Profile("!test")
	static class MongoClientConfiguration {

		@Bean
		public MongoClient mongoClient(final MongoDBSettings mongoSettings) {
			new ServerAddress(
					mongoSettings.getHost(), mongoSettings.getPort());

			MongoCredential.createScramSha1Credential(
					mongoSettings.getUsername(),
					mongoSettings.getDatabase(),
					mongoSettings.getPassword().toCharArray());
			MongoClientURI uri = new MongoClientURI(mongoSettings.getUri());
			return new MongoClient(uri);
		}
	}



}
