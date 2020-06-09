/*
 * 
 */
package com.homemanagement.controller;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.homemanagement.dto.EmailVo;
import com.homemanagement.dto.ServiceStatus;


@RestController
@RequestMapping("/website")
public class WebSiteEmailController {

	@Autowired
	private JavaMailSender sender;

	/** The Constant logger is used to specify the . */
	static final Logger logger = Logger.getLogger(WebSiteEmailController.class);

	@RequestMapping(value = "/sentMail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceStatus<Object> sendEmail(@RequestBody EmailVo emailVo) throws Exception {

		ServiceStatus<Object> serviceStatus = new ServiceStatus<Object>();

		if (emailVo != null) {
			try {
				
				MimeMessage message = sender.createMimeMessage();
				InternetAddress[] iAdressArray = InternetAddress.parse(emailVo.getToUser());
				message.setRecipients(Message.RecipientType.TO, iAdressArray);
				message.setText(emailVo.getMesssage());
				message.setSubject(emailVo.getEmail_subject());
				sender.send(message);
				serviceStatus.setStatus("success");
				serviceStatus.setMessage("successfully email sent");  

			} catch (Exception eMag) {
				eMag.printStackTrace();
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if (eMag instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
			}
		} else {
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("invalid email payload");
		}
		return serviceStatus;
	}

}
