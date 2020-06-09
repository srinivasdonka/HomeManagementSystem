package com.homemanagement.repositories;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.homemanagement.domain.User;


public class UserDetailsDecorator implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8476320779208122721L;

	public static final String ROLES_PREFIX = "ROLE_";

	private User user;

	public UserDetailsDecorator(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		String roles[] ={ "read,write"};

		return Arrays.stream(roles).map(
				role -> (GrantedAuthority) () -> ROLES_PREFIX + role
				).collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
