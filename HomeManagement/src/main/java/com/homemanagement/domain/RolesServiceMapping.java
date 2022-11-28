package com.homemanagement.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Roles_Service_Mapping")
@NoArgsConstructor
@Data
public class RolesServiceMapping {
	    String id;
		String controller_name;
		String role;
		@PersistenceConstructor
	    public RolesServiceMapping(final String id,
	                final String controller_name,
	                final String role) {
	        this.id = id;
	        this.controller_name = controller_name;
	        this.role = role;
	    }
}
