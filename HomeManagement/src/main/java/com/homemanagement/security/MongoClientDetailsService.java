package com.homemanagement.security;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Component;

import com.homemanagement.domain.MongoClientDetails;
import com.homemanagement.repositories.ClientDetailsRepository;

@Component
public class MongoClientDetailsService implements ClientDetailsService, ClientRegistrationService {

	private final ClientDetailsRepository mongoClientDetailsRepository;

	private final PasswordEncoder passwordEncoder;

	public MongoClientDetailsService(final ClientDetailsRepository mongoClientDetailsRepository,
			final PasswordEncoder passwordEncoder) {
		this.mongoClientDetailsRepository = mongoClientDetailsRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public ClientDetails loadClientByClientId(final String clientId) {
		try {
			return mongoClientDetailsRepository.findByClientId(clientId);
		} catch (IllegalArgumentException e) {
			throw new ClientRegistrationException("No Client Details for client id", e);
		}
	}

	@Override
	public void addClientDetails(final ClientDetails clientDetails) {
		final MongoClientDetails mongoClientDetails = new MongoClientDetails(clientDetails.getClientId(),
				passwordEncoder.encode(clientDetails.getClientSecret()),
				clientDetails.getScope(),
				clientDetails.getResourceIds(),
				clientDetails.getAuthorizedGrantTypes(),
				clientDetails.getRegisteredRedirectUri(),
				newArrayList(clientDetails.getAuthorities()),
				clientDetails.getAccessTokenValiditySeconds(),
				clientDetails.getRefreshTokenValiditySeconds(),
				getAutoApproveScopes(clientDetails));

		mongoClientDetailsRepository.save(mongoClientDetails);
	}

	@Override
	public void updateClientDetails(final ClientDetails clientDetails) {
		final MongoClientDetails mongoClientDetails = new MongoClientDetails(clientDetails.getClientId(),
				clientDetails.getClientSecret(),
				clientDetails.getScope(),
				clientDetails.getResourceIds(),
				clientDetails.getAuthorizedGrantTypes(),
				clientDetails.getRegisteredRedirectUri(),
				newArrayList(clientDetails.getAuthorities()),
				clientDetails.getAccessTokenValiditySeconds(),
				clientDetails.getRefreshTokenValiditySeconds(),
				getAutoApproveScopes(clientDetails));
		final boolean result = mongoClientDetailsRepository.update(mongoClientDetails);

		if (!result) {
			throw new NoSuchClientException("No such Client Id");
		}
	}

	@Override
	public void updateClientSecret(final String clientId,
			final String secret) {
		final boolean result = mongoClientDetailsRepository.updateClientSecret(clientId, passwordEncoder.encode(secret));
		if (!result) {
			throw new NoSuchClientException("No such client id");
		}
	}

	@Override
	public void removeClientDetails(String clientId) {
		final boolean result = mongoClientDetailsRepository.deleteByClientId(clientId);
		if (!result) {
			throw new NoSuchClientException("No such client id");
		}
	}

	@Override
	public List<ClientDetails> listClientDetails() {
		final List<MongoClientDetails> allClientDetails = mongoClientDetailsRepository.findAll();
		return newArrayList(allClientDetails);
	}

	private Set<String> getAutoApproveScopes(final ClientDetails clientDetails) {
		if (clientDetails.isAutoApprove("true")) {
			return newHashSet("true"); // all scopes autoapproved
		}

		return clientDetails.getScope().stream()
				.filter(clientDetails::isAutoApprove)
				.collect(Collectors.toSet());
	}
}
