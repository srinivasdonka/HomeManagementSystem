package com.homemanagement.repositories;

import java.util.List;

import com.homemanagement.domain.Privileges;
import com.homemanagement.domain.PrivilegesMapping;


public interface PrivilegeRepositoryBase {

	void addPrivilege(PrivilegesMapping privilege);

	void updatePrivilege(PrivilegesMapping privilege);

	List<Privileges> getAllPrivilegeList();

	List<Privileges> getByPrivilegeId(String privilegeId);

	List<PrivilegesMapping> getByPrivilegeByRoleAndUserId(String user_id);
	
	PrivilegesMapping getIndividualRoleAndUser(String privilegeId, String user_id);
	
	void deletePrivilegesByUserId(String user_id);
}
