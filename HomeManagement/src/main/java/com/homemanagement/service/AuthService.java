package com.homemanagement.service;

import com.homemanagement.domain.PrivilegesMapping;
import com.homemanagement.domain.Roles;
import com.homemanagement.dto.ServiceStatus;

import java.util.List;

public interface AuthService {
     ServiceStatus<Object> createPrivilegesToUser(PrivilegesMapping privileges);
     ServiceStatus<Object> updatePrivilegesToUser(PrivilegesMapping privilegesMapping);
     ServiceStatus<Object> getPrivilegesToUser(Integer page, Integer size);
     ServiceStatus<Object> getPrivilegesByUser(String userId);
     ServiceStatus<Object> createRoleForUser(Roles role);
     ServiceStatus<Object> updateRole(Roles role);
     ServiceStatus<Object> getRoleForUser(Integer page, Integer size);
     ServiceStatus<Object> updateListOfPrivileges(List<PrivilegesMapping> privileges);
}

