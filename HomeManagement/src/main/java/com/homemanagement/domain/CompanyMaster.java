package com.homemanagement.domain;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Company_Master")
public class CompanyMaster {

	private String id;
	private String name;
	private String address;
	private String status;
	private Integer is_SI;
	private Integer no_of_users;
	private Integer no_of_active_users;
	private Integer no_of_devices;
	private String email;
	private String created_date;
	private String last_updated;
	private String membership;

	public CompanyMaster() {
	}

	@PersistenceConstructor
	public CompanyMaster(final String id,
			final String name,
			final String address,
			final String status,
			final Integer is_SI,
			final Integer no_of_users,
			final Integer no_of_active_users,
			final Integer no_of_devices,
			final String email,
			final String created_date,
			final String last_updated,
			final String membership) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.status = status;
		this.is_SI = is_SI;
		this.no_of_users = no_of_users;
		this.no_of_active_users = no_of_active_users;
		this.no_of_devices = no_of_devices;
		this.email = email;
		this.created_date = created_date;
		this.last_updated = last_updated;
		this.membership = membership;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getIs_SI() {
		return is_SI;
	}

	public void setIs_SI(Integer is_SI) {
		this.is_SI = is_SI;
	}

	public Integer getNo_of_users() {
		return no_of_users;
	}

	public void setNo_of_users(Integer no_of_users) {
		this.no_of_users = no_of_users;
	}

	public Integer getNo_of_devices() {
		return no_of_devices;
	}

	public void setNo_of_devices(Integer no_of_devices) {
		this.no_of_devices = no_of_devices;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCreated_date() {
		return created_date;
	}

	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}

	public String getLast_updated() {
		return last_updated;
	}

	public void setLast_updated(String last_updated) {
		this.last_updated = last_updated;
	}

	public String getMembership() {
		return membership;
	}

	public void setMembership(String membership) {
		this.membership = membership;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getNo_of_active_users() {
		return no_of_active_users;
	}

	public void setNo_of_active_users(Integer no_of_active_users) {
		this.no_of_active_users = no_of_active_users;
	}

  
}
