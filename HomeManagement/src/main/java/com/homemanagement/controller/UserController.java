
package com.homemanagement.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.mail.internet.MimeMessage;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.homemanagement.constant.HomeManagementConstant;
import com.homemanagement.domain.CompanyMaster;
import com.homemanagement.domain.Privileges;
import com.homemanagement.domain.PrivilegesMapping;
import com.homemanagement.domain.Roles;
import com.homemanagement.domain.User;
import com.homemanagement.domain.UserDTO;
import com.homemanagement.dto.EmailVo;
import com.homemanagement.dto.ServiceStatus;
import com.homemanagement.repositories.CompanyRepository;
import com.homemanagement.repositories.PrivilegeRepository;
import com.homemanagement.repositories.RoleRepository;
import com.homemanagement.repositories.UserRepository;
import com.homemanagement.security.MongoUserDetailsManager;
import com.homemanagement.utils.HomeManagementUtil;

/**
 * {@link UserController} class provides the user management and check session
 * services end-points .
 *
 * @author caprusit
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private MongoUserDetailsManager userService;

	@Autowired
	Environment environment;

	@Autowired
	PrivilegeRepository privilegeRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	RoleRepository roleRepository;

	/*
	 * @Autowired EmailService emailService;
	 */

	@Autowired
	private JavaMailSender sender;

	/** The Constant logger is used to specify the . */
	static final Logger logger = Logger.getLogger(UserController.class);

	/**
	 * This method is use to Creates the user.
	 *
	 * @param user specify the user registration info
	 * @return the service status class object with response status and payload .
	 */
	@RequestMapping(value = "/createUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> createUser(@RequestBody UserDTO userDTO) {

		ServiceStatus<Object> serviceStatus = new ServiceStatus<Object>();

		if (userDTO != null && !HomeManagementUtil.isEmptyString(userDTO.getUsername()) && !HomeManagementUtil.isEmptyString(userDTO.getPassword())) {

			try {

				Optional<User> findByUsername = userRepository.findByUsername(userDTO.getUsername());
				Boolean userExists = findByUsername.isPresent();

				if (userExists) {

					logger.info("User Already Exists" + userDTO.getUsername());

					serviceStatus.setStatus("failure");
					serviceStatus.setMessage("User Already Exists");
					return serviceStatus;
				} else if (HomeManagementUtil.isEmptyString(userDTO.getCompanyId())) {
					CompanyMaster companyMaster = new CompanyMaster();

					companyMaster.setName(userDTO.getCompanyName());
					companyMaster.setAddress(userDTO.getCompanyAddress());
					companyMaster.setNo_of_active_users(Integer.valueOf(userDTO.getIsActive()));
					companyMaster.setIs_SI(Integer.valueOf(userDTO.getIsSI()));

					LocalDateTime localDateTime = HomeManagementUtil.convertTolocalDateTimeFrom(new Date());
					companyMaster.setCreated_date(localDateTime.toString());

					companyMaster.setLast_updated(localDateTime.toString());
					companyMaster.setNo_of_users(userDTO.getNoOfUsers());
					String companyId = UUID.randomUUID().toString();

					companyMaster.setId(companyId);

					// companyMaster.setId(companyId);

					CompanyMaster companyMasterResult = companyRepository.save(companyMaster);

					if (companyMasterResult != null) {

						User user = new User();
						user.setCompanyName(companyMasterResult.getId());

						user.setUserName(userDTO.getUsername());
						user.setFirstName(userDTO.getFirstName());
						user.setLastName(userDTO.getLastName());

						user.setDesignation(userDTO.getDesignation());
						user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
						LocalDateTime localDateTimes = HomeManagementUtil.convertTolocalDateTimeFrom(new Date());
						user.setCreaterTimestamp(localDateTimes);
						if(userDTO.getRoleId() == null || HomeManagementUtil.isEmptyString(userDTO.getRoleId())) {
							Roles byRoleId = roleRepository.checkRoleExsists(user.getDesignation());
							userDTO.setRoleId(byRoleId.getId());
						}

						user.setRole_id(userDTO.getRoleId());
						user.setPhone(userDTO.getPhone());
						user.setStatus("Activation Pending");

						userRepository.save(user);

						Optional<User> singalUser=userRepository.findByUsername(userDTO.getUsername());
						serviceStatus.setResult(singalUser);
					}
					serviceStatus.setStatus("success");
					serviceStatus.setMessage("successfully registered");
					return serviceStatus;
				} else if (!HomeManagementUtil.isEmptyString(userDTO.getCompanyId())) {
					User user = new User();
					user.setCompanyName(userDTO.getCompanyId());

					user.setUserName(userDTO.getUsername());
					user.setFirstName(userDTO.getFirstName());
					user.setLastName(userDTO.getLastName());
					user.setDesignation(userDTO.getDesignation());
					user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
					user.setId(userDTO.getUserId());
					user.setPhone(userDTO.getPhone());
					user.setStatus("Activation Pending");
					user.setRole_id(userDTO.getRoleId());
					LocalDateTime localDateTime = HomeManagementUtil.convertTolocalDateTimeFrom(new Date());
					user.setCreaterTimestamp(localDateTime);
					userRepository.save(user);
					User fetchUser = (User) userService.loadUserByUsername(userDTO.getUsername());
					if (userDTO.getPrivileges() != null && userDTO.getPrivileges().size() > 0) {
						for (int i = 0; i < userDTO.getPrivileges().size(); i++) {
							userDTO.getPrivileges().get(i).setUser_id(fetchUser.getId());
							privilegeRepository.addPrivilege(userDTO.getPrivilegesMapping().get(i));
						}
					}
				}

				serviceStatus.setStatus("success");
				serviceStatus.setMessage("successfully registered");

			} catch (Exception e) {
				e.printStackTrace();

				logger.info("Exception In Create User" + e.getMessage());
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
			}

		} else {
			logger.info("invalid user payload" + userDTO.toString());
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("invalid user payload");
		}

		return serviceStatus;
	}

	@GetMapping("/get")
	ServiceStatus<User> getUser(@RequestParam("userName") String userName) {

		ServiceStatus<User> serviceStatus = new ServiceStatus<User>();

		if (userName != null) {
			try {

				User user = (User) userService.loadUserByUsername(userName);

				// User user= (User)userRepository.findByUsername(userName);

				serviceStatus.setStatus("success");
				serviceStatus.setMessage("successfully retrieved");
				serviceStatus.setResult(user);

			} catch (Exception e) {
				e.printStackTrace();
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
			}

		} else {
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("invalid user payload");
		}

		return serviceStatus;
	}

	@RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Page<User>> getUser(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {

		ServiceStatus<Page<User>> serviceStatus = new ServiceStatus<Page<User>>();

		if (page != null && size != null && size > 0) {

			try {

				Pageable pageable = PageRequest.of(page, size);

				Page<User> users = userRepository.findAll(pageable);
				serviceStatus.setStatus("success");
				serviceStatus.setMessage("successfully fetched");
				serviceStatus.setResult(users);
			} catch (Exception e) {
				e.printStackTrace();
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
			}
		} else {
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("invalid pagination values ");
		}

		return serviceStatus;
	}

	@RequestMapping(value = "/getAllCompanies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<List<CompanyMaster>> getAllCompanies() {

		ServiceStatus<List<CompanyMaster>> serviceStatus = new ServiceStatus<List<CompanyMaster>>();

		try {

			List<CompanyMaster> companyList = companyRepository.findAll();

			if ((companyList != null) && (companyList.size() > 0)) {
				serviceStatus.setStatus("success");
				serviceStatus.setMessage("successfully feched Company List");
				serviceStatus.setResult(companyList);
			} else {
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				serviceStatus.setResult(companyList);
			}

		} catch (Exception e) {
			e.printStackTrace();
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("failure");
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
			}
		}

		return serviceStatus;
	}

	@RequestMapping(value = "/getCompanyUsersById", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<List<User>> getCompanyUsersById(@RequestParam("Id") String Id) {

		ServiceStatus<List<User>> serviceStatus = new ServiceStatus<List<User>>();

		if (Id != null) {

			try {
				// Map<String, Object> users =userService.getAll(page,size);
				logger.info("getCompanyUsersById" + Id);
				List<User> users = companyRepository.getUsersByComapnyId(Id);

				// Map<String, Object> users =userService.
				serviceStatus.setStatus("success");
				serviceStatus.setMessage("successfully registered");
				serviceStatus.setResult(users);

			} catch (Exception e) {
				e.printStackTrace();
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
			}
		} else {
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("invalid pagination values ");
		}

		return serviceStatus;
	}

	@RequestMapping(value = "/getCompanyUsersByIdPageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Page<User>> getCompanyUsersByIdPageable(@RequestParam("Id") String Id,
			@RequestParam("status") String status, @RequestParam("page") Integer page,
			@RequestParam("size") Integer size) {

		ServiceStatus<Page<User>> serviceStatus = new ServiceStatus<Page<User>>();

		if (Id != null) {

			try {
				logger.info("getCompanyUsersById" + Id);
				Pageable pageable = PageRequest.of(page, size);
				Page<User> users = companyRepository.getUsersByComapnyIdPageable(Id, status, pageable);

				serviceStatus.setStatus("success");
				serviceStatus.setMessage("successfully registered");
				serviceStatus.setResult(users);

			} catch (Exception e) {
				e.printStackTrace();
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
			}
		} else {
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("invalid pagination values ");
		}

		return serviceStatus;
	}

	@RequestMapping(value = "/getCompanyByUserName", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<List<CompanyMaster>> getCompanyByUserName(@RequestParam("username") String username) {

		ServiceStatus<List<CompanyMaster>> serviceStatus = new ServiceStatus<List<CompanyMaster>>();

		if (username != null) {

			try {

				Optional<User> user = userRepository.findByUsername(username);
				if (user.isPresent()) {
					User userInfo = user.get();

					logger.info("getCompanyByUserName" + username);
					logger.info("userInfo.getCompanyName()" + userInfo.getCompanyName());
					List<CompanyMaster> company = companyRepository.getByCompanyId(userInfo.getCompanyName());
					if (company != null && company.size() > 0) {
						serviceStatus.setStatus("success");
						serviceStatus.setMessage("successfully fetched");
						serviceStatus.setResult(company);
					} else {
						serviceStatus.setStatus("failure");
						serviceStatus.setMessage("failure");

					}
				} else {
					serviceStatus.setStatus("failure");
					serviceStatus.setMessage(username + " not exist ");
				}

			} catch (Exception e) {
				e.printStackTrace();
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
			}
		} else {
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("username parameter is mandatory");
		}

		return serviceStatus;
	}

	@RequestMapping(value = "/updateCompany", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> updateCompany(@RequestBody CompanyMaster companyMaster) {

		ServiceStatus<Object> serviceStatus = new ServiceStatus<Object>();

		if (companyMaster != null && companyMaster.getName() != null && companyMaster.getAddress() != null) {

			try {

				companyRepository.editCompany(companyMaster);

				serviceStatus.setStatus("success");
				serviceStatus.setMessage("successfully updated Company details");

			} catch (Exception e) {
				e.printStackTrace();
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
			}
		} else {
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("failure");
		}

		return serviceStatus;
	}

	@RequestMapping(value = "/checkEmail", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> checkEmail(@RequestParam(required = true, name = "email") String email) {

		ServiceStatus<Object> serviceStatus = new ServiceStatus<Object>();

		if (!HomeManagementUtil.isEmptyString(email)) {

			try {
				Boolean isUserExists = userService.userExists(email);
				logger.info("isUserExists " + isUserExists);
				// System.out.println("isUserExists"+isUserExists);
				if (isUserExists) {
					serviceStatus.setStatus("success");
					;
					serviceStatus.setMessage("successfully retrieved");
					// serviceStatus.setResult(isMail);
				} else {
					logger.info("User Name not Exists " + email);
					serviceStatus.setStatus("failure");
					serviceStatus.setMessage("User Name not Exists");
					// serviceStatus.setResult(isMail);
				}

			} catch (Exception e) {
				e.printStackTrace();
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
			}

		} else {
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("invalid user payload");
		}

		return serviceStatus;
	}

	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/getCompaniesByStatusAndSI", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Page<CompanyMaster>> getCompaniesByStatusAndSI(@RequestParam("status") String status,
			@RequestParam("SI") String SI, @RequestParam("page") Integer page, @RequestParam("size") Integer size,
			@RequestParam("sort") String sort) {

		ServiceStatus<Page<CompanyMaster>> serviceStatus = new ServiceStatus<Page<CompanyMaster>>();

		try {
			if (!HomeManagementUtil.isEmptyString(status) || !HomeManagementUtil.isEmptyString(SI)) {
				Sort sortCriteria = null;
				if (!HomeManagementUtil.isEmptyString(sort)) {
					logger.info("sort" + sort);
					sortCriteria = new Sort(new Sort.Order(Direction.ASC, sort));
				} else {
					sortCriteria = new Sort(new Sort.Order(Direction.ASC, "name"));

				}
				Pageable pageable = PageRequest.of(page, size, sortCriteria);

				Page<CompanyMaster> companyList = companyRepository.getCompaniesByStatusAndSI(status, SI, pageable);

				if (companyList != null) {
					serviceStatus.setStatus("success");
					serviceStatus.setMessage("successfully feched Company List");
					serviceStatus.setResult(companyList);
				} else {
					serviceStatus.setStatus("failure");
					serviceStatus.setMessage("failure");
					serviceStatus.setResult(companyList);
				}
			} else {
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("Invalid payload");

			}

		} catch (Exception e) {
			e.printStackTrace();
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("failure");

		}

		return serviceStatus;
	}

	@RequestMapping(value = "/updateCompanyToSI", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> updateCompanyToSI(@RequestBody CompanyMaster companyMaster) {

		ServiceStatus<Object> serviceStatus = new ServiceStatus<Object>();

		if (companyMaster != null && companyMaster.getId() != null) {

			try {

				companyRepository.updateCompanyRoleToSI(companyMaster.getId());

				serviceStatus.setStatus("success");
				serviceStatus.setMessage("successfully updated Company details");

			} catch (Exception e) {
				e.printStackTrace();
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
			}
		} else {
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("failure");
		}

		return serviceStatus;
	}

	@RequestMapping(value = "/updateUser", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> editUser(@RequestBody UserDTO userDTO) {

		ServiceStatus<Object> serviceStatus = new ServiceStatus<Object>();
		if (userDTO != null && !HomeManagementUtil.isEmptyString(userDTO.getFirstName())
				&& !HomeManagementUtil.isEmptyString(userDTO.getUsername()) && !HomeManagementUtil.isEmptyString(userDTO.getPassword())) {

			try {

				Boolean userExists = userRepository.findByUsername(userDTO.getUsername()).isPresent();

				if (userExists) {

					User user = new User();
					user.setUserName(userDTO.getUsername());
					user.setFirstName(userDTO.getFirstName());
					user.setLastName(userDTO.getLastName());
					user.setDesignation(userDTO.getDesignation());
					user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
					user.setId(userDTO.getUserId());
					user.setPhone(userDTO.getPhone());
					user.setStatus(userDTO.getStatus());
					if(userDTO.getRoleId() == null && !HomeManagementUtil.isEmptyString(userDTO.getUserId())) {
						Optional<User> rollId = userRepository.findById(user.getId());
						userDTO.setRoleId(rollId.get().getRole_id());
					}
					user.setRole_id(userDTO.getRoleId());
					LocalDateTime localDateTime = HomeManagementUtil.convertTolocalDateTimeFrom(new Date());
					user.setUpdatedTimestamp(localDateTime);

					userRepository.updateUser(user);

					serviceStatus.setStatus("success");
					serviceStatus.setMessage("successfully update user");
					return serviceStatus;
				}

				else {
					serviceStatus.setStatus("failure");
					serviceStatus.setMessage("User Already Exists");

				}

			} catch (Exception e) {
				e.printStackTrace();

				logger.info("Exception In Create User" + e.getMessage());
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
			}

		} else {
			logger.info("invalid user payload" + userDTO.toString());
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("invalid user payload");
		}

		return serviceStatus;

	}

	@RequestMapping(value = "/getUserBYID", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Optional<User>> getUserBYID(@RequestParam("id") String id) {

		ServiceStatus<Optional<User>> serviceStatus = new ServiceStatus<Optional<User>>();
		if (id != null && id.length() > 0) {

			try {

				Optional<User> user = userRepository.findById(id);

				if (user != null) {

					serviceStatus.setStatus("success");
					serviceStatus.setMessage("successfully update user");
					serviceStatus.setResult(user);
					return serviceStatus;
				}

				else {
					serviceStatus.setStatus("failure");
					serviceStatus.setMessage("User Already Exists");

				}

			} catch (Exception e) {
				e.printStackTrace();

				logger.info("Exception In Create User" + e.getMessage());
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
			}

		} else {

			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("invalid user id");
		}

		return serviceStatus;

	}

	/**
	 * This method is use to Creates the user.
	 *
	 * @param user specify the user registration info
	 * @return the service status class object with response status and payload .
	 */
	@RequestMapping(value = "/createMultipleUsers", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> createMultipleUsers(@RequestBody List<UserDTO> userDTOs) {

		ServiceStatus<Object> serviceStatus = new ServiceStatus<Object>();

		if (userDTOs != null) {

			try {
				for (UserDTO userDTO : userDTOs) {
					Boolean userExists = userRepository.findByUsername(userDTO.getUsername()).isPresent();

					if (userExists) {

						logger.info("User Already Exists" + userDTO.getUsername());

					} else if (HomeManagementUtil.isEmptyString(userDTO.getCompanyId())) {
						CompanyMaster companyMaster = new CompanyMaster();

						companyMaster.setName(userDTO.getCompanyName());
						companyMaster.setAddress(userDTO.getCompanyAddress());
						companyMaster.setNo_of_active_users(Integer.valueOf(userDTO.getIsActive()));
						companyMaster.setIs_SI(Integer.valueOf(userDTO.getIsSI()));

						LocalDateTime localDateTime = HomeManagementUtil.convertTolocalDateTimeFrom(new Date());
						companyMaster.setCreated_date(localDateTime.toString());

						companyMaster.setLast_updated(localDateTime.toString());
						companyMaster.setNo_of_users(userDTO.getNoOfUsers());
						String companyId = UUID.randomUUID().toString();

						companyMaster.setId(companyId);

						// companyMaster.setId(companyId);

						CompanyMaster companyMasterResult = companyRepository.save(companyMaster);
						if (companyMasterResult != null) {

							User user = new User();
							user.setCompanyName(companyMasterResult.getId());

							user.setUserName(userDTO.getUsername());
							user.setFirstName(userDTO.getFirstName());
							user.setLastName(userDTO.getLastName());

							user.setDesignation(userDTO.getDesignation());
							user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

							user.setRole_id(userDTO.getRoleId());
							user.setPhone(userDTO.getPhone());
							user.setStatus("Active");

							userRepository.save(user);
						}


						serviceStatus.setStatus("success");
						serviceStatus.setMessage("successfully registered");
						return serviceStatus;
					} else if (!HomeManagementUtil.isEmptyString(userDTO.getCompanyId())) {
						User user = new User();
						user.setCompanyName(userDTO.getCompanyId());

						user.setUserName(userDTO.getUsername());
						user.setFirstName(userDTO.getFirstName());
						user.setLastName(userDTO.getLastName());
						user.setDesignation(userDTO.getDesignation());
						user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
						user.setId(userDTO.getUserId());
						user.setPhone(userDTO.getPhone());
						user.setStatus("Activation Pending");
						user.setRole_id(userDTO.getRoleId());
						String emailId = UUID.randomUUID().toString();
						user.setEmail_Token(emailId);
						LocalDateTime localDateTime = HomeManagementUtil.convertTolocalDateTimeFrom(new Date());
						user.setCreaterTimestamp(localDateTime);

						User userResult = userRepository.save(user);
						User fetchUser = (User) userService.loadUserByUsername(userDTO.getUsername());

						if (userDTO.getPrivilegesMapping() != null && userDTO.getPrivilegesMapping().size() > 0) {
							for (int i = 0; i < userDTO.getPrivilegesMapping().size(); i++) {
								userDTO.getPrivilegesMapping().get(i).setUser_id(fetchUser.getId());

								privilegeRepository.addPrivilege(userDTO.getPrivilegesMapping().get(i));

							}
						}
						serviceStatus.setResult(userResult);

					}

					serviceStatus.setStatus("success");
					serviceStatus.setMessage("successfully registered");
				}

			} catch (Exception e) {
				e.printStackTrace();

				logger.info("Exception In Create User" + e.getMessage());
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
			}

		} else {

			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("invalid user payload");
		}

		return serviceStatus;
	}

	@RequestMapping(value = "/updateMutlipleUser", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> updateMutlipleUser(@RequestBody List<UserDTO> userDTOs) {

		ServiceStatus<Object> serviceStatus = new ServiceStatus<Object>();

		if (userDTOs != null) {

			try {
				for (UserDTO userDTO : userDTOs) {

					Boolean userExists = userRepository.findByUsername(userDTO.getUsername()).isPresent();

					if (userExists) {

						User user = new User();

						user.setCompanyName(userDTO.getCompanyId());
						user.setUserName(userDTO.getUsername());
						user.setFirstName(userDTO.getFirstName());
						user.setLastName(userDTO.getLastName());
						user.setDesignation(userDTO.getDesignation());
						user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
						user.setId(userDTO.getUserId());
						user.setPhone(userDTO.getPhone());
						user.setStatus(userDTO.getStatus());
						if(userDTO.getRoleId() == null && !HomeManagementUtil.isEmptyString(userDTO.getUserId())) {
							Optional<User> rollId = userRepository.findById(user.getId());
							userDTO.setRoleId(rollId.get().getRole_id());
						}
						user.setRole_id(userDTO.getRoleId());
						LocalDateTime localDateTime = HomeManagementUtil.convertTolocalDateTimeFrom(new Date());
						user.setUpdatedTimestamp(localDateTime);

						userRepository.updateUser(user);

						/*
						 * User fetchUser = (User)
						 * userService.loadUserByUsername(userDTO.getUsername()); if
						 * (userDTO.getPrivileges() != null && userDTO.getPrivileges().size() > 0) { for
						 * (int i = 0; i < userDTO.getPrivileges().size(); i++) {
						 * userDTO.getPrivileges().get(i).setUser_id(fetchUser.getId());
						 * privilegeRepository.updatePrivilege(userDTO.getPrivilegesMapping().get(i));
						 * 
						 * } }
						 */

						List<PrivilegesMapping> privileges = userDTO.getPrivilegesMapping();
						if(privileges != null && privileges.size() > 0) {

							privilegeRepository.deletePrivilegesByUserId(privileges.get(0).getUser_id()) ;
							for (PrivilegesMapping privilege : privileges) {
								privilegeRepository.addPrivilege(privilege);
								serviceStatus.setStatus("success");
								serviceStatus.setMessage("Privileges added successfully");
							}
						}
						serviceStatus.setStatus("success");
						serviceStatus.setMessage("successfully update user");
					}
					else {
						serviceStatus.setStatus("failure");
						serviceStatus.setMessage("User Already Exists");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();

				logger.info("Exception In Create User" + e.getMessage());
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
			}

		} else {
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("invalid user payload");
		}

		return serviceStatus;

	}

	/*
	 * @RequestMapping(value="/sendEmail",method=RequestMethod.POST,consumes=
	 * MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	 * ServiceStatus<Object> sendEmail( @RequestBody EmailDto emailDto){
	 * 
	 * ServiceStatus<Object> serviceStatus=new ServiceStatus<Object>();
	 * 
	 * if(emailDto!=null ){ try {
	 * if(emailDto.getDelimiter()!=null&&emailDto.getEmail_string()!=null&&emailDto.
	 * getParams()!=null) {
	 * 
	 * 
	 * 
	 * emailService.sendMailFromCmdLine(emailDto.getEmail_string());
	 * 
	 * 
	 * serviceStatus.setStatus("success");
	 * serviceStatus.setMessage("successfully email sent");
	 * 
	 * }
	 * 
	 * else{ serviceStatus.setStatus("Failure");
	 * serviceStatus.setMessage("Failed To Send");
	 * 
	 * }
	 * 
	 * } catch (Exception e) { e.printStackTrace();
	 * serviceStatus.setStatus("failure"); serviceStatus.setMessage("failure"); if(e
	 * instanceof org.springframework.dao.DataIntegrityViolationException) {
	 * serviceStatus.setMessage("DATAINTGRTY_VIOLATION"); } }
	 * 
	 * }else { serviceStatus.setStatus("failure");
	 * serviceStatus.setMessage("invalid email payload"); }
	 * 
	 * return serviceStatus;
	 * 
	 * }
	 */

	@RequestMapping(value = "/sentMail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceStatus<Object> sendEmail(@RequestBody EmailVo emailVo) throws Exception {

		ServiceStatus<Object> serviceStatus = new ServiceStatus<Object>();

		if (emailVo != null) {
			try {
				MimeMessage message = sender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true);
				helper.setTo(emailVo.getToUser());
				String parseHtmlTemplate = HomeManagementUtil.parseHtmlTemplate(emailVo);
				helper.setText(parseHtmlTemplate, true);
				helper.setSubject(emailVo.getEmail_subject());
				sender.send(message);
				serviceStatus.setStatus("success");
				serviceStatus.setMessage("successfully email sent");  

			} catch (Exception eMag) {
				eMag.printStackTrace();
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if (eMag instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
			}
		} else {
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("invalid email payload");
		}
		return serviceStatus;
	}

	@RequestMapping(value = "/getEmailToken", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Optional<User>> getEmailToken(@RequestParam("id") String emailId) {

		ServiceStatus<Optional<User>> serviceStatus = new ServiceStatus<Optional<User>>();
		if (emailId != null && emailId.length() > 0) {

			try {
				Optional<User> mailingUser = userRepository.findById(emailId);
				if (mailingUser != null) {
					serviceStatus.setStatus("success");
					serviceStatus.setMessage("successfully  user");
					serviceStatus.setResult(mailingUser);
					return serviceStatus;
				}

			} catch (Exception e) {
				e.printStackTrace();

				logger.info("Exception occurs getting email token" + e.getMessage());
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
			}

		} else {

			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("invalid email token");
		}

		return serviceStatus;
	}

	@RequestMapping(value = "/lastLogin",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<User> updateLastLogin(@RequestParam("userName") String userName,@RequestParam("lastLogin")String lastLogin) {

		ServiceStatus<User> serviceStatus = new ServiceStatus<User>();

		if (userName != null && lastLogin != null) {
			try {

				userRepository.updateLastLogin(userName,lastLogin);
				serviceStatus.setStatus("success");
				serviceStatus.setMessage("Last login updated successfully");
				logger.info(serviceStatus);

			} catch (Exception e) {
				e.printStackTrace();
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
				logger.error(serviceStatus);
			}

		} else {
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("invalid user payload");
			logger.debug(serviceStatus);
		}

		return serviceStatus;
	}


	@PutMapping("/statusForRegistrationUser")
	ServiceStatus<User> updateStatusForRegistrationUser(@RequestParam("id") String userId) {

		ServiceStatus<User> serviceStatus = new ServiceStatus<User>();

		if (userId != null && !HomeManagementUtil.isEmptyString(userId)) {
			try {

				userRepository.updateStatusForRegistrationUser(userId);

				List<Privileges> privileges = privilegeRepository.getAllPrivilegeList();

				Map<String, Privileges> cleanMap = new LinkedHashMap<String, Privileges>();
				for (int i = 0; i < privileges.size(); i++) {
					cleanMap.put(privileges.get(i).getName(), privileges.get(i)); }
				List<Privileges> privilegeList = new ArrayList<Privileges>(cleanMap.values());

				if (privilegeList != null && privilegeList.size() > 0) {
					for (int i = 0; i < privilegeList.size(); i++) {
						privilegeList.get(i).setUser_id(userId);
						PrivilegesMapping pList = new PrivilegesMapping(null, privilegeList.get(i).getName(), privilegeList.get(i).getValue(), null, userId, privilegeList.get(i).getId());
						privilegeRepository.addPrivilege(pList);
					}
				}

				serviceStatus.setStatus("success");
				serviceStatus.setMessage("Status updated successfully");
				logger.info(serviceStatus);

			} catch (Exception e) {
				e.printStackTrace();
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
				if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
				}
				logger.error(serviceStatus);
			}

		} else {
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("invalid user payload");
			logger.debug(serviceStatus);
		}

		return serviceStatus;
	}



	@RequestMapping(value = "/veryfyRegistrationlink", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	ServiceStatus<Object> veryfyRegistrationlink(@RequestBody User registrationUser) {

		ServiceStatus<Object> serviceStatus = new ServiceStatus<Object>();

		if (registrationUser != null &&
				!HomeManagementUtil.isEmptyString(registrationUser.getUsername())) {

			try {

				Optional<User> findByUsername =	userRepository.findByUsername(registrationUser.getUsername()); 
				String status = findByUsername.get().getStatus();

				if (HomeManagementConstant.ACTIVE.getKey().equals(status)) {

					logger.info("User is Active" + registrationUser.getUsername());
					serviceStatus.setStatus("success");
					serviceStatus.setMessage("User is Active"); 
					return serviceStatus;

				} else {

					if (HomeManagementConstant.ACTIVE_PENDING.getKey().equals(status) && !HomeManagementUtil.isEmptyString(registrationUser.getUsername())) {
						for (int i = 0; i <= 1000; i++) {
							System.out.println("Execution in Main Thread...." + i);
							Thread.sleep(2000);
							if (i == 60) {
								userRepository.deleteUserByStatus(registrationUser.getUsername(), status);
								Thread.currentThread().interrupt();
							}
						}

						serviceStatus.setStatus("failure");
						serviceStatus.setMessage("Link expaired");
					}
				}


			} catch (Exception e) {
				e.printStackTrace();

				logger.info("Exception In registered User" + e.getMessage());
				serviceStatus.setStatus("failure"); serviceStatus.setMessage("failure"); 
				if
				(e instanceof org.springframework.dao.DataIntegrityViolationException) {
					serviceStatus.setMessage("DATAINTGRTY_VIOLATION"); } }

		} else { logger.info("invalid user payload" + registrationUser.toString());
		serviceStatus.setStatus("failure");
		serviceStatus.setMessage("invalid user payload"); }

		return serviceStatus;
	}


}
