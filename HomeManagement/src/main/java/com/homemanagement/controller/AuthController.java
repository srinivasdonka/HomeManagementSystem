package com.homemanagement.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.homemanagement.domain.Privileges;
import com.homemanagement.domain.PrivilegesMapping;
import com.homemanagement.domain.Roles;
import com.homemanagement.dto.ServiceStatus;
import com.homemanagement.repositories.PrivilegeRepository;
import com.homemanagement.repositories.RoleRepository;
import com.homemanagement.utils.HomeManagementUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {




	@Autowired
	@Qualifier("privilegeRepository")
	PrivilegeRepository privilegeRepository;

	@Autowired
	RoleRepository roleRepository;


	/** The Constant logger is used to specify the . */
	static final Logger logger = Logger.getLogger(AuthController.class);


	/**
	 * This method is used to Add the privileges to the Role.
	 *
	 * @param privilege
	 *            specify the privilege info
	 * @return the service status class object with response status and payload
	 *         .
	 */
	@RequestMapping(value="/addPrivileges",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> createPrivileges(@RequestBody PrivilegesMapping privileges){

		ServiceStatus<Object> serviceStatus=new ServiceStatus<Object>();

		if(privileges!=null && privileges.getName()!=null && privileges.getRole_id()!=null)
		{
			try {
				logger.info("addPrivileges"+privileges.getName());

				List<Roles> roleExist = roleRepository.getByRoleId(privileges.getRole_id());
				if(roleExist!=null&roleExist.size()>0)
				{
					String privilegeId = UUID.randomUUID().toString();
					privileges.setId(privilegeId);
					//Roles roleExist = roleRepository.checkRoleExsists(role.getName());

					privilegeRepository.addPrivilege(privileges);

					serviceStatus.setStatus("success");
					serviceStatus.setMessage("successfully privileges added");	
				}
				else {
					logger.info("addPrivileges -Role Id Not Exist"+privileges.getRole_id());
					serviceStatus.setStatus("failure");
					serviceStatus.setMessage("failire while adding the privilges");	

				}
				return serviceStatus;
			}catch (Exception e) {
				e.printStackTrace();
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
			}


		}else {
			logger.debug("invalid role payload");
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("invalid privilege payload");
		}

		return serviceStatus;
	}

	/**
	 * This method is used to update the privileges to the Role.
	 *
	 * @param privilege
	 *            specify the privilege info
	 * @return the service status class object with response status and payload
	 *         .
	 */
	@RequestMapping(value="/updatePrivilege",method=RequestMethod.PUT,consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> updatePrivilege(@RequestBody PrivilegesMapping privilegesMapping){

		ServiceStatus<Object> serviceStatus=new ServiceStatus<Object>();

		if(privilegesMapping!=null && privilegesMapping.getName()!=null && privilegesMapping.getRole_id()!=null)
		{
			try {
				logger.info("updatePrivileges"+privilegesMapping.getName());

				List<Roles> roleExist = roleRepository.getByRoleId(privilegesMapping.getRole_id());
				if(roleExist!=null&roleExist.size()>0)
				{

					Optional<Privileges> existPrivileges = privilegeRepository.findById(privilegesMapping.getId());

					logger.info("updatePrivileges"+existPrivileges.isPresent());

					if(existPrivileges.isPresent())
					{
						privilegeRepository.updatePrivilege(privilegesMapping);

						serviceStatus.setStatus("success");
						serviceStatus.setMessage("successfully privileges updated");
					}
					else {
						logger.info("Update Privileges -Privilege Id Not Exist"+privilegesMapping.getId());
						serviceStatus.setStatus("failure");
						serviceStatus.setMessage("failire while updating the privilges");	

					}
				}
				else {
					logger.info("Update Privileges -Role Id Not Exist"+privilegesMapping.getRole_id());
					serviceStatus.setStatus("failure");
					serviceStatus.setMessage("failire while updating the privilges");	

				}

				return serviceStatus;
			}catch (Exception e) {
				e.printStackTrace();
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
			}


		}else {
			logger.debug("invalid role payload");
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("invalid privilege payload");
		}

		return serviceStatus;
	}

	@RequestMapping(value="/getPrivileges",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<List<Privileges>> getPrivileges(@RequestParam("page")Integer page, @RequestParam("size") Integer size){

		ServiceStatus<List<Privileges>> serviceStatus=new ServiceStatus<List<Privileges>>();

		if(page!=null && size!=null&&size>0){
			try {
				List<Privileges> privileges = privilegeRepository.getAllPrivilegeList();

				Map<String, Privileges> cleanMap = new LinkedHashMap<String, Privileges>();
				for (int i = 0; i < privileges.size(); i++) {
					cleanMap.put(privileges.get(i).getName(), privileges.get(i)); }
				List<Privileges> privilegeList = new ArrayList<Privileges>(cleanMap.values());

				serviceStatus.setStatus("success");
				serviceStatus.setMessage("successfully fetched");
				serviceStatus.setResult(privilegeList);
			} catch (Exception e) {
				e.printStackTrace();
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
			}
		}else{
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("invalid param values ");
		}
		return serviceStatus;
	}
	
	@RequestMapping(value="/getPrivilegesByRole",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<List<PrivilegesMapping>> getPrivilegesByRole(@RequestParam("roleId")String roleId,@RequestParam("userId")String userId){

		ServiceStatus<List<PrivilegesMapping>> serviceStatus=new ServiceStatus<List<PrivilegesMapping>>();

		if(userId!=null){
			try {
				List<PrivilegesMapping> privileges = privilegeRepository.getByPrivilegeByRoleAndUserId(userId);
				serviceStatus.setStatus("success");
				serviceStatus.setMessage("successfully fetched");
				serviceStatus.setResult(privileges);
			} catch (Exception e) {
				e.printStackTrace();
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
			}
		}else{
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("invalid param values ");
		}
		return serviceStatus;
	}

	/**
	 * This method is use to Creates the Role.
	 *
	 * @param Role	 *            specify the Role Info
	 * @return the service status class object with response status and payload
	 *         .
	 */
	@RequestMapping(value="/createRole",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> createRole(@RequestBody Roles role){

		ServiceStatus<Object> serviceStatus=new ServiceStatus<Object>();

		if(role!=null && role.getName()!=null && role.getDescription()!=null)
		{
			try {
				logger.info("createRole"+role.toString());
				String roleId = UUID.randomUUID().toString();
				role.setId(roleId);
				Roles roleExist = roleRepository.checkRoleExsists(role.getName());
				if(roleExist!=null)
				{
					logger.info("createRole -Role Already Exist"+roleExist.getName());
					serviceStatus.setStatus("failure");
					serviceStatus.setMessage("Role Already exist");	
				}
				else {
					roleRepository.addRole(role);
					serviceStatus.setStatus("success");
					serviceStatus.setMessage("successfully registered");	
				}
				return serviceStatus;
			}catch (Exception e) {
				e.printStackTrace();
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
			}
		}else {
			logger.debug("invalid role payload");
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("invalid role payload");
		}
		return serviceStatus;
	}


	/**
	 * This method is use to update the role.
	 *
	 * @param Role	specify the Role Info
	 * @return the service status class object with response status and payload
	 *         .
	 */
	@RequestMapping(value="/updateRole",method=RequestMethod.PUT,consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> updateRole(@RequestBody Roles role){

		ServiceStatus<Object> serviceStatus=new ServiceStatus<Object>();

		if(role!=null && role.getName()!=null && role.getDescription()!=null)
		{
			try {
				logger.info("updateRole"+role.getName());
				Optional<Roles> roleExist = roleRepository.findById(role.getId());
				if(roleExist.isPresent())
				{
					roleRepository.updateRole(role);
					logger.info("updateRole -Role updated"+role.getName());
					serviceStatus.setStatus("success");
					serviceStatus.setMessage("successfully updated role");	
				}
				else {
					//  roleRepository.addRole(role);
					logger.info("updateRole -failure while updating role"+role.getName());
					serviceStatus.setStatus("failure");
					serviceStatus.setMessage("failure invalid roleId");	
				}
				return serviceStatus;
			}catch (Exception e) {
				e.printStackTrace();
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
			}
		}else {
			logger.debug("invalid role payload");
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("invalid role payload");
		}
		return serviceStatus;
	}
	/**
	 * This method is used to get all the Roles.
	 *
	 * @return the service status class object with response status and payload
	 *         .
	 */
	@RequestMapping(value="/getRoles",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<List<Roles>> getRoles(@RequestParam("page")Integer page,
			@RequestParam("size") Integer size){

		ServiceStatus<List<Roles>> serviceStatus=new ServiceStatus<List<Roles>>();

		if(page!=null && size!=null&&size>0){
			try {
				List<Roles> roles 	=roleRepository.getAllRoleList();
				serviceStatus.setStatus("success");
				serviceStatus.setMessage("successfully fetched");
				serviceStatus.setResult(roles);
			} catch (Exception e) {
				e.printStackTrace();
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
			}
		}else{
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("invalid param values ");
		}
		return serviceStatus;
	}
	/**
	 * This method is used to update the Access Point configuration.
	 *
	 * @param DeviceConfiguration
	              specify the DeviceConfiguration Info
	 * @return the service status class object with response status and payload
	 *         .
	 */
	@RequestMapping(value = "/updatePrivileges", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> updatePrivileges(@RequestBody List<PrivilegesMapping> privileges) {

		ServiceStatus<Object> serviceStatus = new ServiceStatus<Object>();

		if (privileges != null) {
			try {
				if(privileges.get(0).getUser_id() != null || !HomeManagementUtil.isEmptyString(privileges.get(0).getUser_id())) {
					privilegeRepository.deletePrivilegesByUserId(privileges.get(0).getUser_id()) ;
					for (PrivilegesMapping privilege : privileges) {
						privilegeRepository.addPrivilege(privilege);
						serviceStatus.setStatus("success");
						serviceStatus.setMessage("Privileges added successfully");
					}
				}
				return serviceStatus;
			} catch (Exception e) {
				e.printStackTrace();
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
			}
		} else {
			logger.debug("Invalid Privilege payload");
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("Invalid Privilege payload");
		}
		return serviceStatus;
	}	
}
