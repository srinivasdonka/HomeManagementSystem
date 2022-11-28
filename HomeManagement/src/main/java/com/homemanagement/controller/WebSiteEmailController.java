/*
 * 
 */
package com.homemanagement.controller;


import com.homemanagement.service.WebSiteEmailService;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.homemanagement.dto.EmailVo;
import com.homemanagement.dto.ServiceStatus;

@RestController
public class WebSiteEmailController{
	@Autowired
	WebSiteEmailService webSiteEmailService;
	/** The Constant logger is used to specify the . */
	private static final Logger logger = Logger.getLogger(WebSiteEmailController.class);
	@PostMapping("/website/sentMailToUser")
	public ServiceStatus<Object> sendEmailToUser(@RequestBody EmailVo emailVo) {
		logger.info("sendEmail");
		return webSiteEmailService.sendEmailToUser(emailVo);
	}
}
