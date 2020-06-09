package com.homemanagement.repositories;

import java.util.List;

import com.homemanagement.domain.Privileges;
import com.homemanagement.domain.Roles;


public interface RoleRepositoryBase {

	void addRole(Roles role);

	void updateRole(Roles role);
	
	List<Roles> getAllRoleList();
	
	List<Roles> getByRoleId(String roleId);
	
	List<Privileges> getPrivilegesByRoleID(String role_id);
	
	Roles checkRoleExsists( String roleNane);

}
