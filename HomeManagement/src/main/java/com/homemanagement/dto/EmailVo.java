package com.homemanagement.dto;

import java.util.List;

import com.homemanagement.constant.HomeManagementConstant;

public class EmailVo {

	private String template;

	private List<HomeManagementConstant> templateReplaceItems;

	private String fromUser;

	private String toUser;

	private String userType;

	private String email_url;
	
	private String templateType;
	
	private String email_subject;
	
	private String messsage;
	

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public List<HomeManagementConstant> getTemplateReplaceItems() {
		return templateReplaceItems;
	}

	public void setTemplateReplaceItems(List<HomeManagementConstant> templateReplaceItems) {
		this.templateReplaceItems = templateReplaceItems;
	}

	public String getEmail_url() {
		return email_url;
	}

	public void setEmail_url(String email_url) {
		this.email_url = email_url;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public String getEmail_subject() {
		return email_subject;
	}

	public void setEmail_subject(String email_subject) {
		this.email_subject = email_subject;
	}

	public String getMesssage() {
		return messsage;
	}

	public void setMesssage(String messsage) {
		this.messsage = messsage;
	}

	
		
}
