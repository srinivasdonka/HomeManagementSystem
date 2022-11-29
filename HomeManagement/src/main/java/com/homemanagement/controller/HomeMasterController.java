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

import com.homemanagement.service.HomeMasterService;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;
import com.homemanagement.domain.HomeExpendature;
import com.homemanagement.domain.DeviceMaster;
import com.homemanagement.dto.ServiceStatus;
import com.homemanagement.repositories.DeviceMasterRepository;
import com.homemanagement.repositories.HomeExpedatureRepository;
import com.homemanagement.utils.HomeManagementUtil;

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
