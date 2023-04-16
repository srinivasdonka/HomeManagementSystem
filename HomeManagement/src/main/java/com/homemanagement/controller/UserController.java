
package com.homemanagement.controller;

import java.util.List;

import com.homemanagement.dto.CompanyMasterDTO;
import com.homemanagement.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.homemanagement.domain.CompanyMaster;
import com.homemanagement.dto.UserDTO;
import com.homemanagement.dto.EmailVo;
import com.homemanagement.dto.ServiceStatus;

/**
 * {@link UserController} class provides the user management and check session
 * services end-points .
 *
 * @author caprusit
 * @version 1.0
 */

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private ModelMapper modelMapper;
	/**
	 * This method is use to Creates the user.
	 * @return the service status class object with response status and payload .
	 */
	@PostMapping("/user/createUser")
	ServiceStatus<Object> createUser(@RequestBody UserDTO userDTO) {
		return userService.getCreateUserService(userDTO);
	}
	@GetMapping("/user/get")
	ServiceStatus<Object> getUser(@RequestParam("userName") String userName) {
		return userService.getSingleUser(userName);
	}
	@GetMapping("/user/getAll")
	ServiceStatus<Object> getUser(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
		return userService.getUserDetailsInPage(page, size);
	}
	@GetMapping("/user/getAllCompanies")
	ServiceStatus<Object> getAllCompanies() {
		return userService.getCompanies();
	}
	@GetMapping("/user/getCompanyUsersById")
	ServiceStatus<Object> getCompanyUsersById(@RequestParam("Id") String id) {
		return userService.getCompanyById(id);
	}
	@GetMapping("/user/getCompanyUsersByIdPageable")
	ServiceStatus<Object> getCompanyUsersByIdPageable(@RequestParam("Id") String id,
			@RequestParam("status") String status, @RequestParam("page") Integer page,
			@RequestParam("size") Integer size) {
		return userService.getPageForCompanyById(id, status, page, size);
	}
	@GetMapping("/user/getCompanyByUserName")
	ServiceStatus<Object> getCompanyByUserName(@RequestParam("username") String username) {
		return userService.getCompanyByUser(username);
	}
	@PutMapping("/user/updateCompany")
	ServiceStatus<Object> updateCompany(@RequestBody CompanyMasterDTO companyMasterDTO) {
		CompanyMaster companyMaster = this.modelMapper.map(companyMasterDTO, CompanyMaster.class);
		return userService.updateCompany(companyMaster);
	}
	@GetMapping("/user/checkEmail")
	ServiceStatus<Object> checkEmail(@RequestParam(required = true, name = "email") String email) {
		return userService.getMail(email);
	}
	@GetMapping("/user/getCompaniesByStatusAndSI")
	ServiceStatus<Object> getCompaniesByStatusAndSI(@RequestParam("status") String status,
			@RequestParam("SI") String si, @RequestParam("page") Integer page, @RequestParam("size") Integer size,
			@RequestParam("sort") String sort) {
		return userService.getCompanyStatus(status, si, page, size, sort);
	}
	@PutMapping("/user/updateCompanyToSI")
	ServiceStatus<Object> updateCompanyToSI(@RequestBody CompanyMasterDTO companyMasterDTO) {
		CompanyMaster companyMaster = this.modelMapper.map(companyMasterDTO, CompanyMaster.class);
		return userService.updateCompanyToOwner(companyMaster);
	}
	@PutMapping("/user/updateUser")
	ServiceStatus<Object> editUser(@RequestBody UserDTO userDTO) {
		return userService.getUpdateUser(userDTO);
	}
	@GetMapping("/user/getUserBYID")
	ServiceStatus<Object> getUserBYID(@RequestParam("id") String id) {
		return userService.getUserByUserId(id);
	}
	/**
	 * This method is use to Creates the user.
	 *
	 * @return the service status class object with response status and payload .
	 */
	@PostMapping("/user/createMultipleUsers")
	ServiceStatus<Object> createMultipleUsers(@RequestBody List<UserDTO> userDTOs) {
		return userService.createMultipleUsersForHome(userDTOs);
	}
	@PutMapping("/user/updateMutlipleUser")
	ServiceStatus<Object> updateMutlipleUser(@RequestBody List<UserDTO> userDTOs) {
		return userService.updateMultipleUsersForHome(userDTOs);
	}
	@PostMapping("/user/sentMail")
	public ServiceStatus<Object> sendEmail(@RequestBody EmailVo emailVo) {
		return userService.sendMailToCustomer(emailVo);
	}
	@GetMapping("/user/getEmailToken")
	ServiceStatus<Object> getEmailToken(@RequestParam("id") String emailId) {
		return userService.getMailToken(emailId);
	}
	@PutMapping("/user/lastLogin")
	ServiceStatus<Object> updateLastLogin(@RequestParam("userName") String userName,@RequestParam("lastLogin")String lastLogin) {
		return userService.updateLastUserLogin(userName, lastLogin);
	}
	@PutMapping("/user/statusForRegistrationUser")
	ServiceStatus<Object> updateStatusForRegistrationUser(@RequestParam("id") String userId) {
		return userService.updateStatusForRegUser(userId);
	}
	@PostMapping(value = "/user/veryfyRegistrationlink")
	ServiceStatus<Object> veryfyRegistrationlink(@RequestBody UserDTO registrationUser) {
		return userService.verifyRegLink(registrationUser);
	}
}
