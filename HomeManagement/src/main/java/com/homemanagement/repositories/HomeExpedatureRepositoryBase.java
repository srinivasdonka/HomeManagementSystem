package com.homemanagement.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.homemanagement.domain.HomeExpendature;
import com.homemanagement.domain.DeviceNetwork;



public interface HomeExpedatureRepositoryBase {

	public void addDevice(HomeExpendature device);

	public List<HomeExpendature> getDeviceListByCompanyIdAndActiveStatus(String CompanyId,boolean activeStatus);


	public HomeExpendature getItemeByItemId(String id);

	public Page<HomeExpendature> getDeviceListByCompanyIdAndActiveStatusPageable(String CompanyId,boolean activeStatus,Pageable pageable);

	public boolean changeDeviceCompany(String companyId,String destComapnyId,String deviceId,String deviceToken);

	public void updateItem(HomeExpendature device);

	public void addNetwork(DeviceNetwork deviceNetwork);

	public List<HomeExpendature> getDeviceByParentId(String device_parent_id);

	public List<DeviceNetwork> getDeviceNetworsByNetworkId();
	
	public boolean checkDeviceExists(String device_mac_id, String device_key);
	 
	public  List<HomeExpendature> getItemByUserId(String userId);
	 
	public DeviceNetwork getDeviceNetworkByNetworkId(String networkId);
	
	


}
