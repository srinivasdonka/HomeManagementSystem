package com.homemanagement.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection="Roles")
@NoArgsConstructor
@Data
public class Roles {
		@Id
		String id;
		String name;
		String description;
		
		@PersistenceConstructor
	    public Roles(final String id,
	                final String name,
	                final String description) {
	        this.id = id;
	        this.name = name;
	        this.description = description;
	    }
}
