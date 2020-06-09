package com.homemanagement.dto;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.homemanagement.domain.HomeExpendature;
import com.homemanagement.utils.HomeManagementUtil;

public class ServiceStatus<T> {

	private String status;
	private String message;
	private String adminUrl;
	private T result;
	private String apiKey;
	private String email_Token;
	private List<HomeExpendature> deviceList;
	 
	
	
	public ServiceStatus(){}
	
	public ServiceStatus(final String status, final String message) {
		
		super();
		this.status = status;
		this.message = message;
	}
	
	@SuppressWarnings("unchecked")
	public ServiceStatus(final String status, final List<FieldError> fieldErrors, 
							final List<ObjectError> globalErrors) {
        super();
        try {
        	this.status = status;
        	
        	List<Object> errorsList = new ArrayList<>();
        	errorsList.add(fieldErrors);
        	errorsList.add(globalErrors);
        	JsonObject errorResultsObj = new JsonObject();
        	Type fieldErrorsType = new TypeToken<List<FieldError>>() {}.getType();
        	errorResultsObj.add("fieldErrors", HomeManagementUtil.convertToJsonElement(fieldErrors, fieldErrorsType));
        	
        	Type globalErrorsType = new TypeToken<List<ObjectError>>() {}.getType();
        	errorResultsObj.add("globalErrors", HomeManagementUtil.convertToJsonElement(globalErrors, globalErrorsType));
        	
        	result = (T)errorResultsObj.toString();
            this.message = "Request failed due to invalid input";
            
        } catch (final Exception e) {
        	this.status = "Bad Request";
            this.message = "Request failed due to invalid input";
        }
    }
	
	public String getAdminUrl() {
		return adminUrl;
	}

	public void setAdminUrl(String adminUrl) {
		this.adminUrl = adminUrl;
	}

	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public T getResult() {
		return result;
	}
	
	public void setResult(T result) {
		this.result = result;
	}
	
	public String getApiKey() {
		return apiKey;
	}
	
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getEmail_Token() {
		return email_Token;
	}

	public void setEmail_Token(String email_Token) {
		this.email_Token = email_Token;
	}

	public List<HomeExpendature> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<HomeExpendature> deviceList) {
		this.deviceList = deviceList;
	}
	
}
