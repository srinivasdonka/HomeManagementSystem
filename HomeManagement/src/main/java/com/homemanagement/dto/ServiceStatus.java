package com.homemanagement.dto;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.homemanagement.utils.HomeManagementUtil;

@Data
@NoArgsConstructor
public class ServiceStatus<T> {
	private String status;
	private String message;
	private T result;

	/*public ServiceStatus(final String status, final List<FieldError> fieldErrors,
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
    }*/
}
