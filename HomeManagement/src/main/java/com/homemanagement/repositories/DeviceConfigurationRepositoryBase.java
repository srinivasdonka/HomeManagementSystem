package com.homemanagement.repositories;

import java.util.List;

import com.homemanagement.domain.DeviceConfiguration;


public interface DeviceConfigurationRepositoryBase {
	
	void addDeviceConfiguration(DeviceConfiguration deviceconfiguration);
	void updateDeviceConfiguration(DeviceConfiguration deviceconfiguration);
	List<DeviceConfiguration> getDeviceConfigurationListByDeviceId(String device_id);
	DeviceConfiguration getDeviceConfigurationByDeviceIdAndConfigPropName(String device_id,String config_property_name );
	
	void updateDeviceSyncConfiguration(DeviceConfiguration deviceconfiguration) ;
	
	void addDeviceConfigurationList(List<DeviceConfiguration> deviceconfiguration);
	void updateDeviceConfigurationList(List<DeviceConfiguration> deviceconfiguration);
	 public List<DeviceConfiguration> getDeviceConfigurationByDeviceIdAndConfigPropType(String device_id,String config_property_type);
	 public List<DeviceConfiguration> getDeviceConfigurationbySync(String device_id);
}
