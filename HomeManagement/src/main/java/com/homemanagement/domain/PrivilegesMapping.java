package com.homemanagement.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Privileges_Mapping")
@NoArgsConstructor
@Data
public class PrivilegesMapping  {
	@Id
	String id;
	String name;
	String value;
	String role_id;
	String user_id;
	String privilegeId;
	@PersistenceConstructor
	public PrivilegesMapping(final String id,
			final String name,
			final String value,
			final String role_id, final String user_id, final String privilegeId) {
		this.id = id;
		this.name = name;
		this.role_id = role_id;
		this.value = value;
		this.user_id=user_id;
		this.privilegeId=privilegeId;
	}
}
