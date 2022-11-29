package com.homemanagement.controller;

import java.io.*;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.homemanagement.service.HomeConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.homemanagement.domain.DeviceConfiguration;
import com.homemanagement.dto.ServiceStatus;
import com.homemanagement.utils.UploadResponse;
@RestController
public class HomeConfigurationController {
	@Autowired
	HomeConfigService homeConfigService;
	@PostMapping("/deviceconfiguration/addDeviceConfiguration")
	ServiceStatus<Object> addDeviceConfiguration(@RequestBody DeviceConfiguration deviceconfiguration){
		return homeConfigService.addHomeProperty(deviceconfiguration);
	}
	@PutMapping("/deviceconfiguration/updateDeviceConfiguration")
	ServiceStatus<Object> updateDeviceConfiguration(@RequestBody DeviceConfiguration deviceconfiguration){
		return homeConfigService.updateHomeProperty(deviceconfiguration);
	}
	@GetMapping("/deviceconfiguration/getDeviceConfigurationListByDeviceId")
	ServiceStatus<Object> getDeviceConfigurationListByDeviceId(@RequestParam("device_id")String device_id){
		return homeConfigService.getHomeProperty(device_id);
	}
	@PostMapping("/deviceconfiguration/setupMeshdeviceNetwork")
	ServiceStatus<Object> setupMeshdeviceNetwork(@RequestBody List<DeviceConfiguration> deviceconfiguration) {
		return homeConfigService.uploadProperties(deviceconfiguration);
	}
	@PostMapping("/deviceconfiguration/deviceConfigUpload")
	public ResponseEntity<UploadResponse> deviceConfigUpload(
			@RequestParam("qqfile") String file,
			@RequestParam("qquuid") String uuid,
			@RequestParam("qqfilename") String fileName,
			@RequestParam(value = "version") String version,
			@RequestParam(value = "device_id") String device_id,
			@RequestParam(value = "checkSum") String checkSum) {
		return homeConfigService.uploadProperties(file, uuid, fileName);

	}
	@GetMapping("/getConfiguration")
	public void getFile(@RequestParam(value="fileName", required=true) String fileName, 
			HttpServletRequest request,HttpServletResponse response) throws FileNotFoundException {
		homeConfigService.getPropertiesByCustomer(fileName, request, response);
	}
	@GetMapping("/deviceconfiguration/getDeviceConfifg")
	ServiceStatus<Object> getDeviceConfifg(@RequestParam("device_id")String device_id){
		return homeConfigService.getPropertyByOwner(device_id);
	}
}
