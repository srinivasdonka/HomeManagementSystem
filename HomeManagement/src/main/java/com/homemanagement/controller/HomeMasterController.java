package com.homemanagement.controller;


import com.homemanagement.dto.ServiceStatus;
import com.homemanagement.service.HomeMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HomeMasterController {
	@Autowired
	HomeMasterService homeMasterService;
	@PostMapping("/phoneHome/createPhoneHomedevice")
	public ServiceStatus<Object> createMasterDevice(@RequestParam("PhoneHomeRequest") String device) {
		return homeMasterService.createMonthlyReport(device);
	}
	@RequestMapping(value = "/phoneHome/getPhoneHomeData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<?> getDeviceMaster(@RequestParam("device_mac_id") String device_mac_id,
			@RequestParam("hardware_key") String device_key) {
		return homeMasterService.downloadMonthlyReport(device_mac_id, device_key);
	}
}
