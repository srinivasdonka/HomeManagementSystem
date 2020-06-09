package com.homemanagement.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Company_Master")
public class CompanyMaster {

    private String id;
	private String name;
	private String address;
	private Integer is_active;
	private Integer is_SI;
	private Integer no_of_users;
	private LocalDateTime created_date;
	private LocalDateTime last_updated;
	
    public CompanyMaster() {
    }

    @PersistenceConstructor
    public CompanyMaster(final String id,
                final String name,
                final String address,
                final Integer is_active,
                final Integer is_SI,
                final Integer no_of_users,
                final LocalDateTime created_date,
                final LocalDateTime last_updated) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.is_active = is_active;
        this.is_SI = is_SI;
        this.no_of_users = no_of_users;
        this.created_date = created_date;
        this.last_updated = last_updated;
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

	public Integer getIs_active() {
		return is_active;
	}

	public void setIs_active(Integer is_active) {
		this.is_active = is_active;
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

	public LocalDateTime getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDateTime created_date) {
		this.created_date = created_date;
	}

	public LocalDateTime getLast_updated() {
		return last_updated;
	}

	public void setLast_updated(LocalDateTime last_updated) {
		this.last_updated = last_updated;
	}

  
}
