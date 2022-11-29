package com.homemanagement.service;

import com.homemanagement.domain.DeviceNetwork;
import com.homemanagement.domain.HomeExpendature;
import com.homemanagement.dto.ServiceStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface HomeService {
    ServiceStatus<Object> createRole(HomeExpendature homeExp);
    ServiceStatus<Object> checkHomeExp(String company_id, boolean is_active);
    ServiceStatus<Object> getSingleItem(String id);
    ServiceStatus<Object> getHomeExp(String device_id, String version);
    ServiceStatus<Object> getHomeExpById(String company_id, boolean is_active, Integer page, Integer size, String sort);
    ServiceStatus<Object> changeCompany(String currentCompanyId, String destCompanyId,String device_id);
    ServiceStatus<Object> updateHomeExp(HomeExpendature homeExp);
    void getHomeExpReport(String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException;
    ServiceStatus<Object> getHomeWeeklyReport(String info);
    ServiceStatus<?> getPropertyExpById(String device_mac_id);
    ServiceStatus<Object> getAllHomeExps(String network_id);
    ServiceStatus<Object> getProperty(String info);
    ServiceStatus<Object> getExp(String userId);
    ServiceStatus<Object> addExp(DeviceNetwork deviceNetwork);
    ServiceStatus<Object> getPropertyExp();
    ServiceStatus<?> getPropertyReport(String device_mac_id, String device_key);
}
