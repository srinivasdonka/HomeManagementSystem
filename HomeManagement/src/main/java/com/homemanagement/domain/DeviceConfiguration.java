package com.homemanagement.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Device_Configuration")
public class DeviceConfiguration {
	
	private String id;
	private String device_id;
	private String config_property_name;
	private String config_property_value;
	private LocalDateTime created_date;
	private LocalDateTime last_updated;
	private Integer is_sync;// 0-synched 1 -not synched 3-Device Not connected
	private String config_property_type;
	

	public Integer getIs_sync() {
		return is_sync;
	}
	public void setIs_sync(Integer is_sync) {
		this.is_sync = is_sync;
	}
	@PersistenceConstructor
    public DeviceConfiguration(final String id,
                final String device_id,
                final String config_property_name,final String config_property_value,
                final LocalDateTime created_date,
                final LocalDateTime last_updated,final Integer is_sync,final String config_property_type) {
        this.id = id;
        this.device_id = device_id;
        this.config_property_name = config_property_name;
        this.config_property_value = config_property_value;        
        this.created_date=created_date;
        this.last_updated=last_updated;
        this.is_sync= is_sync;
        this.config_property_type= config_property_type;
    }
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getConfig_property_name() {
		return config_property_name;
	}

	public void setConfig_property_name(String config_property_name) {
		this.config_property_name = config_property_name;
	}

	public String getConfig_property_value() {
		return config_property_value;
	}

	public void setConfig_property_value(String config_property_value) {
		this.config_property_value = config_property_value;
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
	
	
	public String getConfig_property_type() {
		return config_property_type;
	}
	public void setConfig_property_type(String config_property_type) {
		this.config_property_type = config_property_type;
	}

}
