package com.homemanagement.service;

import com.homemanagement.domain.DeviceConfiguration;
import com.homemanagement.dto.ServiceStatus;
import com.homemanagement.utils.UploadResponse;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface HomeConfigService {
    ServiceStatus<Object> addHomeProperty(DeviceConfiguration deviceconfiguration);
    ServiceStatus<Object> updateHomeProperty(DeviceConfiguration deviceconfiguration);
    ServiceStatus<Object> getHomeProperty(String device_id);
    ServiceStatus<Object> uploadProperties(List<DeviceConfiguration> deviceconfiguration);
    ResponseEntity<UploadResponse> uploadProperties(String file, String uuid,String fileName);
    void getPropertiesByCustomer(String fileName, HttpServletRequest request, HttpServletResponse response);
    ServiceStatus<Object> getPropertyByOwner(String device_id);
}
