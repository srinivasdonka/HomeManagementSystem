package com.homemanagement.domain;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Roles_Service_Mapping")
public class RolesServiceMapping {
	
	    private String id;
		private String controller_name;
		private String role;
		
	
		@PersistenceConstructor
	    public RolesServiceMapping(final String id,
	                final String controller_name,
	                final String role) {
	        this.id = id;
	        this.controller_name = controller_name;
	        this.role = role;
	    }


		public String getId() {
			return id;
		}


		public void setId(String id) {
			this.id = id;
		}


		public String getController_name() {
			return controller_name;
		}


		public void setController_name(String controller_name) {
			this.controller_name = controller_name;
		}


		public String getRole() {
			return role;
		}


		public void setRole(String role) {
			this.role = role;
		}
		
		
}
