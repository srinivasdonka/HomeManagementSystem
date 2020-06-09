package com.homemanagement.domain;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

@Document(collection="Auth_Client_Details")
public class MongoClientDetails implements ClientDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7000277318372529840L;

	@Id
	private String id;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String client_id;
	private String client_secret;
	private Set<String> scope = Collections.emptySet();
	private Set<String> resource_ids = Collections.emptySet();
	private Set<String> authorized_grant_types = Collections.emptySet();
	private Set<String> web_server_redirect_uri;
	private List<GrantedAuthority> authorities = Collections.emptyList();
	private Integer access_token_validity;
	private Integer refresh_token_validity;
	private Set<String> autoapprove;

	public MongoClientDetails() {
	}

	@PersistenceConstructor
	public MongoClientDetails(final String client_id,
			final String client_secret,
			final Set<String> scope,
			final Set<String> resource_ids,
			final Set<String> authorized_grant_types,
			final Set<String> web_server_redirect_uri,
			final List<GrantedAuthority> authorities,
			final Integer access_token_validity,
			final Integer refresh_token_validity,
			final Set<String> autoapprove 
			) {
		this.client_id = client_id;
		this.client_secret = client_secret;
		this.scope = scope;
		this.resource_ids = resource_ids;
		this.authorized_grant_types = authorized_grant_types;
		this.web_server_redirect_uri = web_server_redirect_uri;
		this.authorities = authorities;
		this.access_token_validity = access_token_validity;
		this.refresh_token_validity = refresh_token_validity;
		//this.additional_information = additional_information;
		this.autoapprove = autoapprove;
	}

	public String getClientId() {
		return client_id;
	}

	public String getClientSecret() {
		return client_secret;
	}

	public Set<String> getScope() {
		return scope;
	}

	public Set<String> getResourceIds() {
		return resource_ids;
	}

	public Set<String> getAuthorizedGrantTypes() {
		return authorized_grant_types;
	}

	public Collection<GrantedAuthority> getAuthorities() {

		GrantedAuthority grantedAuthority = new GrantedAuthority() {

			private static final long serialVersionUID = 1L;

			@Override
			public String getAuthority() {

				return "USER";
			}
		};

		List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		roles.add(grantedAuthority);

		return roles;
	}

	public Integer getAccessTokenValiditySeconds() {
		return access_token_validity;
	}

	public Integer getRefreshTokenValiditySeconds() {
		return refresh_token_validity;
	}



	public void setAutoApproveScopes(final Set<String> autoapprove) {
		this.autoapprove = autoapprove;
	}

	public Set<String> getAutoApproveScopes() {
		return autoapprove;
	}

	@Override
	public boolean isScoped() {
		return this.scope != null && !this.scope.isEmpty();
	}

	@Override
	public boolean isSecretRequired() {
		return this.client_secret != null;
	}

	@Override
	public Set<String> getRegisteredRedirectUri() {
		return web_server_redirect_uri;
	}

	@Override
	public boolean isAutoApprove(final String scope) {
		if (isNull(autoapprove)) {
			return false;
		}
		for (String auto : autoapprove) {
			if ("true".equals(auto) || scope.matches(auto)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof MongoClientDetails)) return false;

		MongoClientDetails that = (MongoClientDetails) o;

		return new EqualsBuilder()
				.append(client_id, that.client_id)
				.append(scope, that.scope)
				.append(resource_ids, that.resource_ids)
				.append(authorized_grant_types, that.authorized_grant_types)
				.append(web_server_redirect_uri, that.web_server_redirect_uri)
				.append(authorities, that.authorities)
				.append(access_token_validity, that.access_token_validity)
				.append(refresh_token_validity, that.refresh_token_validity)
				.append(autoapprove, that.autoapprove)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(client_id)
				.append(client_secret)
				.append(scope)
				.append(resource_ids)
				.append(authorized_grant_types)
				.append(web_server_redirect_uri)
				.append(authorities)
				.append(access_token_validity)
				.append(refresh_token_validity)
				.append(autoapprove)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("clientId", client_id)
				.append("clientSecret", client_secret)
				.append("scope", scope)
				.append("resourceIds", resource_ids)
				.append("authorizedGrantTypes", authorized_grant_types)
				.append("registeredRedirectUris", web_server_redirect_uri)
				.append("authorities", authorities)
				.append("accessTokenValiditySeconds", access_token_validity)
				.append("refreshTokenValiditySeconds", refresh_token_validity)
				.append("autoApproveScopes", autoapprove)
				.toString();
	}

	@Override
	public Map<String, Object> getAdditionalInformation() {
		// TODO Auto-generated method stub
		return null;
	}
}
