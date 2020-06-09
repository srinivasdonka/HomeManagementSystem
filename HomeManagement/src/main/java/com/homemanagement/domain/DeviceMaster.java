package com.homemanagement.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Device_Master")
public class DeviceMaster	 {

	@Id
	public String id;
	public String model_name;
	public String device_mac_id;
	public String hardware_key;
	public String serial_number;
	public String hardware_version;
	public String device_type;
	public LocalDateTime created_date;
	public LocalDateTime last_updated;
	private String miscellaneous;

	public DeviceMaster() {

	}

	@PersistenceConstructor
	public DeviceMaster(final String id,
			final String device_mac_id,
			final String device_type,
			final String model_name,
			final String hardware_version,
			final String serial_number,
			final String hardware_key,
			final LocalDateTime created_date,
			final LocalDateTime last_updated,final String miscellaneous) {
		this.id = id;
		this.device_mac_id = device_mac_id;
		this.device_type = device_type;
		this.model_name = model_name;
		this.hardware_version = hardware_version;
		this.serial_number = serial_number;
		this.hardware_key = hardware_key;
		this.created_date=created_date;
		this.last_updated=last_updated;
		this.miscellaneous=miscellaneous;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDevice_mac_id() {
		return device_mac_id;
	}

	public void setDevice_mac_id(String device_mac_id) {
		this.device_mac_id = device_mac_id;
	}

	public String getDevice_type() {
		return device_type;
	}

	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}

	public String getModel_name() {
		return model_name;
	}

	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}

	public String getHardware_version() {
		return hardware_version;
	}

	public void setHardware_version(String hardware_version) {
		this.hardware_version = hardware_version;
	}

	public String getSerial_number() {
		return serial_number;
	}

	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}

	public String getHardware_key() {
		return hardware_key;
	}

	public void setHardware_key(String hardware_key) {
		this.hardware_key = hardware_key;
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

	public String getMiscellaneous() {
		return miscellaneous;
	}

	public void setMiscellaneous(String miscellaneous) {
		this.miscellaneous = miscellaneous;
	}

}
