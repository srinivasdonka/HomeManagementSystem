package com.homemanagement.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Company_Master")
@NoArgsConstructor
@Data
public class CompanyMaster {
	String id;
	String name;
	String address;
	String status;
	Integer is_SI;
	Integer no_of_users;
	Integer no_of_active_users;
	Integer no_of_devices;
	String email;
	String created_date;
	String last_updated;
	String membership;
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
}
