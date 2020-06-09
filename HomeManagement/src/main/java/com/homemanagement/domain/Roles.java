package com.homemanagement.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Roles")
public class Roles {
	
		@Id
	    private String id;
		private String name;
		private String description;
		
		@PersistenceConstructor
	    public Roles(final String id,
	                final String name,
	                final String description) {
	        this.id = id;
	        this.name = name;
	        this.description = description;
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
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		
}
