package com.homemanagement.repositories;

import com.homemanagement.domain.DeviceMaster;

public interface DeviceMasterRepositoryBase {

	boolean checkDeviceExists(String device_mac_id, String device_key);

	void addDeviceMaster(DeviceMaster deviceMaster);

	void updatePhoneHomeDevice(DeviceMaster device_mac_id);

	DeviceMaster getByDeviceMacId(String device_mac_id,String device_key);

}
