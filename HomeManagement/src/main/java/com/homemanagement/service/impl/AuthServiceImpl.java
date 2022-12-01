package com.homemanagement.service.impl;

import com.homemanagement.constant.HomeManagementKeyConstant;
import com.homemanagement.domain.Privileges;
import com.homemanagement.domain.PrivilegesMapping;
import com.homemanagement.domain.Roles;
import com.homemanagement.dto.ServiceStatus;
import com.homemanagement.repositories.PrivilegeRepository;
import com.homemanagement.repositories.RoleRepository;
import com.homemanagement.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    @Qualifier("privilegeRepository")
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private RoleRepository roleRepository;
    private static final Logger logger = Logger.getLogger(AuthServiceImpl.class);
    ServiceStatus<Object> serviceStatus=new ServiceStatus<>();
    public ServiceStatus<Object> createPrivilegesToUser(PrivilegesMapping privileges) {
        if(privileges !=null && privileges.getName()!=null && privileges.getRole_id()!=null)
        {
            try {
                logger.info("addPrivileges"+ privileges.getName());
                Optional<List<Roles>> roleExist = Optional.ofNullable(roleRepository.getByRoleId(privileges.getRole_id()));
                if(roleExist.isPresent())
                {
                    String privilegeId = UUID.randomUUID().toString();
                    privileges.setId(privilegeId);
                    privilegeRepository.addPrivilege(privileges);
                    serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                    serviceStatus.setMessage("successfully privileges added");
                }
                else {
                    logger.info("addPrivileges -Role Id Not Exist"+ privileges.getRole_id());
                    serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                    serviceStatus.setMessage("failire while adding the privilges");
                }
                return serviceStatus;
            }catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
                }
            }
        }else {
            logger.debug(HomeManagementKeyConstant.INVALID_PAYLOAD);
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage("invalid privilege payload");
        }
        return serviceStatus;
    }
    public ServiceStatus<Object> updatePrivilegesToUser(PrivilegesMapping privilegesMapping) {
        if(privilegesMapping.getName()!=null && privilegesMapping.getRole_id()!=null)
        {
            try {
                logger.info("updatePrivileges"+ privilegesMapping.getName());

                Optional<List<Roles>> roleExist = Optional.ofNullable(roleRepository.getByRoleId(privilegesMapping.getRole_id()));
                if(!roleExist.isPresent())
                {
                    Optional<Privileges> existPrivileges = privilegeRepository.findById(privilegesMapping.getId());
                    logger.info("updatePrivileges"+existPrivileges.isPresent());
                    if(existPrivileges.isPresent())
                    {
                        privilegeRepository.updatePrivilege(privilegesMapping);
                        serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                        serviceStatus.setMessage("successfully privileges updated");
                    }
                    else {
                        logger.info("Update Privileges -Privilege Id Not Exist"+ privilegesMapping.getId());
                        serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                        serviceStatus.setMessage("failire while updating the privilges");
                    }
                }
                else {
                    logger.info("Update Privileges -Role Id Not Exist"+ privilegesMapping.getRole_id());
                    serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                    serviceStatus.setMessage("failire while updating the privilges");
                }
                return serviceStatus;
            }catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
                }
            }
        }else {
            logger.debug(HomeManagementKeyConstant.INVALID_PAYLOAD);
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage("invalid privilege payload");
        }

        return serviceStatus;
    }
    public ServiceStatus<Object> getPrivilegesToUser(Integer page, Integer size) {
        if(page !=null && size !=null && size >0){
            try {
                serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                serviceStatus.setMessage(HomeManagementKeyConstant.SUCCESS);
                serviceStatus.setResult(getPrivileges(privilegeRepository));
            } catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
                }
            }
        }else{
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage(HomeManagementKeyConstant.INVALID_PAYLOAD);
        }
        return serviceStatus;
    }

    public static List<Privileges> getPrivileges(PrivilegeRepository privilegeRepository) {
        List<Privileges> privileges = privilegeRepository.getAllPrivilegeList();
        Map<String, Privileges> cleanMap = new LinkedHashMap<>();
        for (int i = 0; i < privileges.size(); i++) {
            cleanMap.put(privileges.get(i).getName(), privileges.get(i));
        }
        return new ArrayList<>(cleanMap.values());
    }

    public ServiceStatus<Object> getPrivilegesByUser(String userId) {
        if(userId !=null){
            try {
                List<PrivilegesMapping> privileges = privilegeRepository.getByPrivilegeByRoleAndUserId(userId);
                serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                serviceStatus.setMessage(HomeManagementKeyConstant.SUCCESS);
                serviceStatus.setResult(privileges);
            } catch (Exception e) {
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
                }
            }
        }else{
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage(HomeManagementKeyConstant.INVALID_PAYLOAD);
        }
        return serviceStatus;
    }
    public ServiceStatus<Object> createRoleForUser(Roles role) {
        if(role !=null && role.getName()!=null && role.getDescription()!=null)
        {
            try {
                logger.info("createRole"+ role.toString());
                String roleId = UUID.randomUUID().toString();
                role.setId(roleId);
                Roles roleExist = roleRepository.checkRoleExsists(role.getName());
                if(roleExist!=null)
                {
                    logger.info("createRole -Role Already Exist"+roleExist.getName());
                    serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                    serviceStatus.setMessage("Role Already exist");
                }
                else {
                    roleRepository.addRole(role);
                    serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                    serviceStatus.setMessage("successfully registered");
                }
                return serviceStatus;
            }catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
                }
            }
        }else {
            logger.debug(HomeManagementKeyConstant.INVALID_PAYLOAD);
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage(HomeManagementKeyConstant.INVALID_PAYLOAD);
        }
        return serviceStatus;
    }
    public ServiceStatus<Object> updateRole(Roles role) {
        if(role !=null && role.getName()!=null && role.getDescription()!=null)
        {
            try {
                logger.info("updateRole"+ role.getName());
                Optional<Roles> roleExist = roleRepository.findById(role.getId());
                if(roleExist.isPresent())
                {
                    roleRepository.updateRole(role);
                    logger.info("updateRole -Role updated"+ role.getName());
                    serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                    serviceStatus.setMessage("successfully updated role");
                }
                else {
                    logger.info("updateRole -failure while updating role"+ role.getName());
                    serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                    serviceStatus.setMessage("failure invalid roleId");
                }
                return serviceStatus;
            }catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
                }
            }
        }else {
            logger.debug(HomeManagementKeyConstant.INVALID_PAYLOAD);
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage(HomeManagementKeyConstant.INVALID_PAYLOAD);
        }
        return serviceStatus;
    }
    public ServiceStatus<Object> getRoleForUser(Integer page, Integer size) {
        if(page !=null && size !=null&& size >0){
            try {
                List<Roles> roles 	=roleRepository.getAllRoleList();
                serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                serviceStatus.setMessage(HomeManagementKeyConstant.SUCCESS);
                serviceStatus.setResult(roles);
            } catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
                }
            }
        }else{
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage(HomeManagementKeyConstant.INVALID_PAYLOAD);
        }
        return serviceStatus;
    }
    public ServiceStatus<Object> updateListOfPrivileges(List<PrivilegesMapping> privileges) {
        if (!privileges.isEmpty()) {
            try {
                if(null != privileges.get(0).getUser_id()) {
                    privilegeRepository.deletePrivilegesByUserId(privileges.get(0).getUser_id()) ;
                    for (PrivilegesMapping privilege : privileges) {
                        privilegeRepository.addPrivilege(privilege);
                        serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                        serviceStatus.setMessage("Privileges added successfully");
                    }
                }
                return serviceStatus;
            } catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
            }
        } else {
            logger.debug("Invalid Privilege payload");
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage("Invalid Privilege payload");
        }
        return serviceStatus;
    }
}
