package com.homemanagement.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.homemanagement.domain.HomeExpendature;
import com.homemanagement.domain.DeviceNetwork;



public interface HomeExpedatureRepositoryBase {

	void addDevice(HomeExpendature device);

	List<HomeExpendature> getDeviceListByCompanyIdAndActiveStatus(String CompanyId,boolean activeStatus);

	HomeExpendature getDeviceByDeviceIdAndActive(String deviceId,boolean activeStatus);

	public HomeExpendature getItemeByItemId(String itemId);

	Page<HomeExpendature> getDeviceListByCompanyIdAndActiveStatusPageable(String CompanyId,boolean activeStatus,Pageable pageable);

	boolean changeDeviceCompany(String companyId,String destComapnyId,String deviceId,String deviceToken);

	void updateDevice(HomeExpendature device);

	void addNetwork(DeviceNetwork deviceNetwork);

	List<HomeExpendature> getDeviceByParentId(String device_parent_id);

	List<DeviceNetwork> getDeviceNetworsByNetworkId();
	
	 boolean checkDeviceExists(String device_mac_id, String device_key);
	 
	 public List<HomeExpendature> getItemByUserId(String userId);
	 
	 DeviceNetwork getDeviceNetworkByNetworkId(String networkId);
	
	


}
