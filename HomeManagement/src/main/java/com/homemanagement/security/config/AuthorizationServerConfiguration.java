package com.homemanagement.security.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import com.homemanagement.repositories.RepositoryUserDetailsService;
import com.homemanagement.security.MongoClientDetailsService;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private ApplicationConfigurationProperties configuration;

	private TokenStore tokenStore = new InMemoryTokenStore();

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	@Autowired
	private RepositoryUserDetailsService userDetailsService;

	@Autowired
	private MongoClientDetailsService clientDetailsService;

	/*
	 * @Autowired private OAuthAccessTokenRepository oAuthAccessTokenRepository;
	 */


	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints)    
			throws Exception {
		endpoints
		.tokenStore(this.tokenStore)
		.authenticationManager(this.authenticationManager)
		.userDetailsService(userDetailsService);
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		ClientDetailsService clientService = clients.withClientDetails(clientDetailsService).build();
		/*
		 * clients.inMemory() .withClient(configuration.getClientId())
		 * .authorizedGrantTypes("password", "refresh_token") .authorities("USER")
		 * .scopes("read", "write")
		 * .resourceIds(RestApiResourceServerConfiguration.RESOURCE_ID)
		 * .secret(configuration.getClientSecret());
		 */
	}

	@Bean
	@Primary
	public DefaultTokenServices tokenServices() {

		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setTokenStore(this.tokenStore);
		return tokenServices;
	}

}
