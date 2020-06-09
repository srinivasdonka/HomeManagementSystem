package com.homemanagement.domain;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Fota")
public class FOTA {
	
	    private String device_id;
		private String version;
		private String description;
		private String file_path;
		
		public String getDevice_id() {
			return device_id;
		}

		public void setDevice_id(String device_id) {
			this.device_id = device_id;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getFile_path() {
			return file_path;
		}

		public void setFile_path(String file_path) {
			this.file_path = file_path;
		}

		@PersistenceConstructor
	    public FOTA(final String device_id,
	                final String version,
	                final String description,final String file_path) {
	        this.device_id = device_id;
	        this.version = version;
	        this.description = description;
	        this.file_path=file_path;
	    }

}
