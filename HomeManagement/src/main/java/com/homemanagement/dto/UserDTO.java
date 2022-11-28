package com.homemanagement.dto;

import com.homemanagement.domain.Privileges;
import com.homemanagement.domain.PrivilegesMapping;
import com.homemanagement.dto.PrivilegesMappingDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	String userId;
	String username;
	String password;
	String firstName;
	String lastName;
	String designation;
	String roleId;
	String phone;
	String companyId;
	String companyName;
	String companyAddress;
	String isActive;
	String status;
	List<Privileges> privileges;
	List<PrivilegesMapping> privilegesMapping;
	String isSI;
	int noOfUsers;
}
