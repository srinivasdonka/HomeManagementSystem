package com.homemanagement.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.homemanagement.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.homemanagement.domain.DeviceNetwork;
import com.homemanagement.domain.HomeExpendature;
import com.homemanagement.dto.ServiceStatus;
import com.homemanagement.exception.StorageException;
import com.homemanagement.utils.UploadResponse;
@RestController
public class HomeController {
	@Autowired
	HomeService homeService;
	@PostMapping("/homeExp/addExpendature")
	ServiceStatus<Object> createRole(@RequestBody HomeExpendature homeExp){
		return homeService.createRole(homeExp);
	}
	@GetMapping("/homeExp/getDeviceListByCompanyIdAndActiveStatus")
	ServiceStatus<Object> checkDeviceMaster(@RequestParam("company_id")String company_id,@RequestParam("is_active")boolean is_active){
		return homeService.checkHomeExp(company_id, is_active);
	}
	@GetMapping("/homeExp/getSingleItemById")
	ServiceStatus<Object> getSingleItemById(@RequestParam("id")String id){
		return homeService.getSingleItem(id);
	}
	@ExceptionHandler(StorageException.class)
	public ResponseEntity<UploadResponse> handleException(StorageException ex) {
		UploadResponse response = new UploadResponse(false, ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
	@GetMapping("/homeExp/getFotaPath")
	ServiceStatus<Object> getFotaPath(@RequestParam("device_id")String device_id,@RequestParam("version")String version){
		return homeService.getHomeExp(device_id, version);
	}

	@GetMapping("/homeExp/getDeviceListByCompanyIdAndActiveStatusPageable")
	ServiceStatus<Object> getDeviceListByCompanyIdAndActiveStatusPageable(@RequestParam("company_id")String company_id,@RequestParam("is_active")boolean is_active,@RequestParam("page")Integer page,
			@RequestParam("size") Integer size,@RequestParam("sort") String sort){
		return homeService.getHomeExpById(company_id, is_active, page, size, sort);
	}
	@PostMapping("/homeExp/changeDeviceCompany")
	public ServiceStatus<Object> changeDeviceCompany(
			@RequestParam("currentCompanyId") String currentCompanyId,
			@RequestParam("destCompanyId") String destCompanyId,@RequestParam(value = "device_id") String device_id) {
		return homeService.changeCompany(currentCompanyId, destCompanyId, device_id);
	}
	@PutMapping("/homeExp/updateHomeExpedature")
	public ServiceStatus<Object> updateHomeExpedature(@RequestBody HomeExpendature homeExp) {
		return homeService.updateHomeExp(homeExp);
	}
	@CrossOrigin(origins = "*")
	@GetMapping("/homeExp/getFile")
	public void getFile(@RequestParam(value="fileName", required=false) String fileName, 
			HttpServletRequest request,HttpServletResponse response) throws IOException, Exception
	{
		homeService.getHomeExpReport(fileName, request, response);
	}
	@CrossOrigin(origins = "*")
	@GetMapping("/homeExp/serverAResponse")
	ServiceStatus<Object> serverAResponse(@RequestParam("info")String info){
		return homeService.getHomeWeeklyReport(info);
	}
	@CrossOrigin(origins = "*")
	@GetMapping("/homeExp/serverBResponse")
	ServiceStatus<Object> serverBResponse(@RequestParam("info")String info) {
		return homeService.getProperty(info);
	}
	@GetMapping("/homeExp/getHomeExpendature")
	ServiceStatus<Object> getHomeExpendature(@RequestParam("userId") String userId){
		return homeService.getExp(userId);
	}
	@PostMapping("/homeExp/addDeviceNetwork")
	ServiceStatus<Object> AddNetwork(@RequestBody DeviceNetwork deviceNetwork){
		return homeService.addExp(deviceNetwork);
	}
	@GetMapping("/homeExp/getdeviceNetworks")
	ServiceStatus<Object> getDeviceNetwork(){
		return homeService.getPropertyExp();
	}
	@GetMapping("/homeExp/checkDeviceExists")
	ServiceStatus<?> getDeviceMaster(@RequestParam("device_id")String device_mac_id, @RequestParam("hardware_key")String device_key){
		return homeService.getPropertyReport(device_mac_id, device_key);
	}
	@GetMapping("/homeExp/deviceByDeviceId")
	ServiceStatus<?> getDeviceByDeviceId(@RequestParam("device_id")String device_mac_id){

		return homeService.getPropertyExpById(device_mac_id);
	}
	@GetMapping("/homeExp/deviceNetworkByNetworkId")
	ServiceStatus<Object> getDeviceNetworkByNetworkId(@RequestParam("network_id")String network_id){
		return homeService.getAllHomeExps(network_id);
	}
}
