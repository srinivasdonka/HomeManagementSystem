package com.homemanagement.dto;

import java.util.List;

import com.homemanagement.constant.HomeManagementConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVo {
	String template;
	List<HomeManagementConstant> templateReplaceItems;
	String fromUser;
	String toUser;
	String userType;
	String email_url;
	String templateType;
	String email_subject;
	String messsage;
}
