package com.homemanagement.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Privileges")
public class Privileges {

	@Id
	private String id;
	private String name;
	private String value;
	private String role_id;
	private String user_id;

	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	@PersistenceConstructor
	public Privileges(final String id,
			final String name,
			final String value,
			final String role_id, final String user_id) {
		this.id = id;
		this.name = name;
		this.role_id = role_id;
		this.value = value;
		this.user_id=user_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRole_id() {
		return role_id;
	}
	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}
}
