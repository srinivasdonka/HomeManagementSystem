package com.homemanagement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ServiceStatus<T> {
	private String status;
	private String message;
	private T result;
}
