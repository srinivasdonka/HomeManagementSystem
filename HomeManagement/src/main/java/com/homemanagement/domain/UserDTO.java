package com.homemanagement.domain;

import java.io.Serializable;
import java.util.List;

public class UserDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1584069006005221662L;
	
	private String UserId;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String designation;
	
	private String roleId;
	private String phone;
	
	
	private String companyId;
	private String companyName;
	private String companyAddress;
	private String isActive;
	private String status;
	
	private List<Privileges> privileges;
	private List<PrivilegesMapping> privilegesMapping;
	
	public List<PrivilegesMapping> getPrivilegesMapping() {
		return privilegesMapping;
	}
	public void setPrivilegesMapping(List<PrivilegesMapping> privilegesMapping) {
		this.privilegesMapping = privilegesMapping;
	}
	public List<Privileges> getPrivileges() {
		return privileges;
	}
	public void setPrivileges(List<Privileges> privileges) {
		this.privileges = privileges;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	private String isSI;
	private int noOfUsers;
	

	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getIsSI() {
		return isSI;
	}
	public void setIsSI(String isSI) {
		this.isSI = isSI;
	}
	public int getNoOfUsers() {
		return noOfUsers;
	}
	public void setNoOfUsers(int noOfUsers) {
		this.noOfUsers = noOfUsers;
	}
	
	
	

}
