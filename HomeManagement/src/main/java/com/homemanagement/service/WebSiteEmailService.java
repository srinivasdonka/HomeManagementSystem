package com.homemanagement.service;

import com.homemanagement.dto.EmailVo;
import com.homemanagement.dto.ServiceStatus;

public interface WebSiteEmailService {
    public ServiceStatus<Object> sendEmailToUser(EmailVo emailVo);
}
