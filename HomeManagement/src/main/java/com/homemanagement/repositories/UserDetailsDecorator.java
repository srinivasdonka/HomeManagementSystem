package com.homemanagement.repositories;

import org.springframework.security.core.authority.AuthorityUtils;

import com.homemanagement.domain.User;

public class UserDetailsDecorator extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = -2033337903887924902L;
	private User user;

	public UserDetailsDecorator(User user) {
		super(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getDesignation()));
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public String getId() {
		return user.getId();
	}

	public String getRole() {
		return user.getDesignation();
	}

	@Override
	public String toString() {
		return "CurrentUser{" + "user=" + user + "} " + super.toString();
	}
}
