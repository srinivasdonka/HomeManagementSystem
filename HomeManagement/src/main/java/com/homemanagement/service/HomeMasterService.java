package com.homemanagement.service;

import com.homemanagement.dto.ServiceStatus;

public interface HomeMasterService {
    ServiceStatus<Object> createMonthlyReport(String device);
    ServiceStatus<?> downloadMonthlyReport(String device_mac_id, String device_key);
}
