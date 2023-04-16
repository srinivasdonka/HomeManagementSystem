package com.homemanagement.service;

import com.homemanagement.domain.DeviceNetwork;
import com.homemanagement.domain.HomeExpendature;
import com.homemanagement.dto.ServiceStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface HomeService {
    ServiceStatus<?> createRole(HomeExpendature homeExp);
    ServiceStatus<?> checkHomeExp(String company_id, boolean is_active);
    ServiceStatus<?> getSingleItem(String id);
    ServiceStatus<?> getHomeExp(String device_id, String version);
    ServiceStatus<?> getHomeExpById(String company_id, boolean is_active, Integer page, Integer size, String sort);
    ServiceStatus<?> changeCompany(String currentCompanyId, String destCompanyId,String device_id);
    ServiceStatus<?> updateHomeExp(HomeExpendature homeExp);
    void getHomeExpReport(String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException;
    ServiceStatus<?> getHomeWeeklyReport(String info);
    ServiceStatus<?> getPropertyExpById(String device_mac_id);
    ServiceStatus<?> getAllHomeExps(String network_id);
    ServiceStatus<?> getProperty(String info);
    ServiceStatus<?> getExp(String userId);
    ServiceStatus<?> addExp(DeviceNetwork deviceNetwork);
    ServiceStatus<?> getPropertyExp();
    ServiceStatus<?> getPropertyReport(String device_mac_id, String device_key);
}
