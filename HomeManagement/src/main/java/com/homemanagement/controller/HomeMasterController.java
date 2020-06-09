package com.homemanagement.controller;

import java.nio.file.FileSystems;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.homemanagement.domain.HomeExpendature;
import com.homemanagement.domain.DeviceMaster;
import com.homemanagement.dto.ServiceStatus;
import com.homemanagement.repositories.DeviceMasterRepository;
import com.homemanagement.repositories.HomeExpedatureRepository;
import com.homemanagement.utils.HomeManagementUtil;

@RestController
@RequestMapping("/phoneHome")
public class HomeMasterController {

	@Autowired
	DeviceMasterRepository deviceMasterRepository;

	@Autowired
	HomeExpedatureRepository deviceRepository;

	/** The Constant logger is used to specify the . */
	static final Logger logger = Logger.getLogger(HomeMasterController.class);

	/**
	 * This method is use to Creates the Device.
	 * 
	 * @param HomeExpendature * specify the Device Info
	 * @return the service status class object with response status and payload .
	 */
	@RequestMapping(value = "/createPhoneHomedevice", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)

	public ServiceStatus<Object> createMasterDevice(@RequestParam("PhoneHomeRequest") String device) {

		ServiceStatus<Object> serviceStatus = new ServiceStatus<Object>();
		DeviceMaster byDeviceSeqNum;
		String adminUrl;
		String firstSubString;
		String secondSubString;
		Map<String,String> hashMap = new HashMap<String,String>();
		String[] splitDevice = device.split(",");
		for(String deviceFields : splitDevice) {
			Pattern p = Pattern.compile("(.*?)" + "=" + "(.*)");
			Matcher m = p.matcher(deviceFields);
			if (m.matches()) {
				firstSubString = m.group(1); 
				secondSubString = m.group(2);
				hashMap.put(firstSubString, secondSubString);
			} 
		}


		DeviceMaster deviceMaster = new DeviceMaster();
		deviceMaster.setModel_name(hashMap.get("model_name"));
		deviceMaster.setDevice_mac_id(hashMap.get("device_mac_id"));
		deviceMaster.setHardware_key(hashMap.get("hardware_key"));
		deviceMaster.setSerial_number(hashMap.get("serial_number"));
		deviceMaster.setHardware_version(hashMap.get("hardware_version"));
		String devices = new Gson().toJson(hashMap); 
		deviceMaster.setMiscellaneous(devices);

		if(deviceMaster!=null && deviceMaster.getDevice_mac_id() != null || !HomeManagementUtil.isEmptyString(deviceMaster.getDevice_mac_id())) { 
			try {
				DeviceMaster byDeviceSeqNumber = deviceMasterRepository.getByDeviceMacId(deviceMaster.getDevice_mac_id(), null); 
				if(byDeviceSeqNumber != null) {
					deviceMaster.setId(byDeviceSeqNumber.getId());
					deviceMaster.setCreated_date(byDeviceSeqNumber.getCreated_date());
					deviceMaster.setLast_updated(byDeviceSeqNumber.getLast_updated());
					deviceMaster.setDevice_type(byDeviceSeqNumber.getDevice_type()); 
				} 
				String deviceMasters = new Gson().toJson(deviceMaster); 
				String byDeviceSeqNumbers = new Gson().toJson(byDeviceSeqNumber);
				if(deviceMasters.equals(byDeviceSeqNumbers)){
					logger.info("isDeviceSeqNoExists"+byDeviceSeqNumber.getDevice_mac_id());
					serviceStatus.setStatus("success");
					serviceStatus.setMessage("Device already has existed"); 
					byDeviceSeqNum = deviceMasterRepository.getByDeviceMacId(deviceMaster.getDevice_mac_id(), null); 
					adminUrl = FileSystems.getDefault().getPath(".").toAbsolutePath().normalize().toString() ; 
					String fileName = HomeManagementUtil.fileName(adminUrl);
					serviceStatus.setAdminUrl(adminUrl+"resources/"+fileName);
					serviceStatus.setResult(byDeviceSeqNum); 
				}else {
					logger.info("createMasterDevice"+deviceMaster.getDevice_mac_id());

					boolean isDeviceSeqNoExists = deviceMasterRepository.checkDeviceExists(deviceMaster.getDevice_mac_id(),null );
					if(isDeviceSeqNoExists) { 
						LocalDateTime localDateTime = HomeManagementUtil.convertTolocalDateTimeFrom(new Date());
						deviceMaster.setLast_updated(localDateTime);
						deviceMasterRepository.updatePhoneHomeDevice(deviceMaster);
						serviceStatus.setStatus("success");
						serviceStatus.setMessage("Device has been updated succssfully");
						byDeviceSeqNum =  deviceMasterRepository.getByDeviceMacId(deviceMaster.getDevice_mac_id(), null); 
						adminUrl = FileSystems.getDefault().getPath(".").toAbsolutePath().normalize().toString(); 
						String fileName = HomeManagementUtil.fileName(adminUrl);
						serviceStatus.setAdminUrl(adminUrl+"resources/"+fileName);
						serviceStatus.setResult(byDeviceSeqNum); 
					} else { 
						String deviceMasterId = UUID.randomUUID().toString(); deviceMaster.setId(deviceMasterId);
						deviceMaster.setDevice_type("Mesh Router"); LocalDateTime localDateTime = HomeManagementUtil.convertTolocalDateTimeFrom(new Date());
						deviceMaster.setCreated_date(localDateTime);
						deviceMasterRepository.addDeviceMaster(deviceMaster);
						serviceStatus.setStatus("success");
						serviceStatus.setMessage("Device has been added succssfully"); 
						byDeviceSeqNum  = deviceMasterRepository.getByDeviceMacId(deviceMaster.getDevice_mac_id(), null); 
						adminUrl = FileSystems.getDefault().getPath(".").toAbsolutePath().normalize().toString();
						String fileName = HomeManagementUtil.fileName(adminUrl);
						serviceStatus.setAdminUrl(adminUrl+"resources/"+fileName);
						serviceStatus.setResult(byDeviceSeqNum); 
					} 
					return serviceStatus;
				} 

			}catch (Exception e) { 
				e.printStackTrace();
				serviceStatus.setStatus("failure"); serviceStatus.setMessage("failure"); 
				if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION"); 
				} 
			} }else {
				logger.debug("invalid phone home Device payload");
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("invalid phone home Device payload");
			}
		return serviceStatus;
	}

	/**
	 * This method is used to get all the Roles.
	 *
	 * @return the service status class object with response status and payload .
	 */

	@RequestMapping(value = "/getPhoneHomeData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<?> getDeviceMaster(@RequestParam("device_mac_id") String device_mac_id,
			@RequestParam("hardware_key") String device_key) {

		ServiceStatus<Object> serviceStatus = new ServiceStatus<Object>();

		if (device_mac_id != null) {

			try {
				logger.info("Fetch Phonehome data" + device_mac_id);

				boolean isDeviceSeqNoExists = deviceMasterRepository.checkDeviceExists(device_mac_id, device_key);
				if (isDeviceSeqNoExists) {
					DeviceMaster phoneHomeData = deviceMasterRepository.getByDeviceMacId(device_mac_id, device_key);
					serviceStatus.setStatus("success");
					serviceStatus.setMessage("Device already has existed");
					serviceStatus.setResult(phoneHomeData);
					if (phoneHomeData != null) {
						List<HomeExpendature> deviceList = deviceRepository.getDeviceByParentId(null);
						logger.info("Fetch phoneHomeData" + phoneHomeData);
						serviceStatus.setDeviceList(deviceList);
					}

				} else {
					serviceStatus.setMessage("Device does not exist");
				}
			} catch (Exception e) {
				e.printStackTrace();
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
			}
		} else {
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("invalid param values ");
		}

		return serviceStatus;
	}

}
