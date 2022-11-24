package com.homemanagement.service.impl;

import com.homemanagement.dto.EmailVo;
import com.homemanagement.dto.ServiceStatus;
import com.homemanagement.service.WebSiteEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class WebSiteEmailServiceImpl implements WebSiteEmailService {
    @Autowired
    private JavaMailSender sender;
    private ServiceStatus<Object> serviceStatus = new ServiceStatus<>();
    public ServiceStatus<Object> sendEmailToUser(EmailVo emailVo) {
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
