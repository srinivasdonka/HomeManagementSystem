package com.homemanagement.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Document(collection="User")
public class User implements UserDetails, CredentialsContainer {
	private static final long serialVersionUID = -7630196540278243591L;
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	private String username;
	private String password;
	private String first_name;
	private String last_name;
	private String designation;
	private String company_id;
	private String company_name;
	private String role_id;
	private String phone;
	private LocalDateTime created_timestamp;
	private LocalDateTime upated_timestamp;
	private String status;
	private String email_Token;
	private String lastLogin;
	public User() {
    }
    @PersistenceConstructor
    public User(final String id,
    		final String password,
                final String username,
                final String first_name,
                final String last_name,
                final String designation,
                final String company_id,
                final String company_name,
                final String phone,
                final String role_id,
                final LocalDateTime created_timestamp,LocalDateTime upated_timestamp,final String status,final String email_Token,String lastLogin) {
    	this.id = id;
        this.password = password;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.designation = designation;
        this.company_id=company_id;
        this.company_name = company_name;
        this.phone = phone;
        this.role_id = role_id;
        this.created_timestamp = created_timestamp;
        this.status=status;
        this.email_Token = email_Token;
        this.lastLogin=lastLogin;
    }

    @Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		GrantedAuthority grantedAuthority = new GrantedAuthority() {
			private static final long serialVersionUID = 1L;
			@Override
			public String getAuthority() {
				return "ROLE_USER";
			}
		};
		
		List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		roles.add(grantedAuthority);
		
		return roles;
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
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return username;
    }
	public void setUserName(String userName) {
		this.username = userName;
	}
	public String getFirstName() {
		return first_name;
	}
	public void setFirstName(String firstName) {
		this.first_name = firstName;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getLastName() {
		return last_name;
	}
	public void setLastName(String lastName) {
		this.last_name = lastName;
	}
	public String getRole_id() {
		return role_id;
	}
	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}
	public String getCompanyName() {
		return  company_name;
	}
	public void setCompanyName(String company_name) {
		this.company_name = company_name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public LocalDateTime getCreateTimestamp() {
		return created_timestamp;
	}
	public void setCreaterTimestamp(LocalDateTime created_timestamp) {
		this.created_timestamp = created_timestamp;
	}
	public LocalDateTime getUpdatedTimestamp() {
		return upated_timestamp;
	}
	public void setUpdatedTimestamp(LocalDateTime upated_timestamp) {
		this.upated_timestamp = upated_timestamp;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
    public void eraseCredentials() {
        password = null;
    }
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmail_Token() {
		return email_Token;
	}
	public void setEmail_Token(String email_Token) {
		this.email_Token = email_Token;
	}
	public String getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}
	public String getCompany_id() {
		return company_id;
	}
	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}
}
