package com.homemanagement.service.impl;

import com.homemanagement.constant.HomeManagementConstant;
import com.homemanagement.constant.HomeManagementKeyConstant;
import com.homemanagement.domain.*;
import com.homemanagement.dto.UserDTO;
import com.homemanagement.dto.EmailVo;
import com.homemanagement.dto.ServiceStatus;
import com.homemanagement.repositories.CompanyRepository;
import com.homemanagement.repositories.PrivilegeRepository;
import com.homemanagement.repositories.RoleRepository;
import com.homemanagement.repositories.UserRepository;
import com.homemanagement.security.MongoUserDetailsManager;
import com.homemanagement.service.UserService;
import com.homemanagement.utils.HomeManagementUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MongoUserDetailsManager mongoUserDetailsManager;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JavaMailSender sender;
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
    private ServiceStatus<Object> serviceStatus;

    @Override
    public ServiceStatus<Object> getCreateUserService(UserDTO userDTO) {
    	serviceStatus = new ServiceStatus<Object>();
        if (!StringUtils.isEmpty(userDTO.getUsername()) && !StringUtils.isEmpty(userDTO.getPassword())) {
            try {
                Optional<User> findByUsername = userRepository.findByUsername(userDTO.getUsername());
                if (findByUsername.isPresent()) {
                    logger.info(HomeManagementKeyConstant.USER_ALREADY_EXIST + userDTO.getUsername());
                    serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                    serviceStatus.setMessage(HomeManagementKeyConstant.USER_ALREADY_EXIST);
                    return serviceStatus;
                } else if (StringUtils.isEmpty(userDTO.getCompanyId())) {
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
                    CompanyMaster companyMasterResult = companyRepository.save(companyMaster);

                    User user = new User();
                    user.setCompanyName(companyMasterResult.getId());
                    user.setUserName(userDTO.getUsername());
                    user.setFirstName(userDTO.getFirstName());
                    user.setLastName(userDTO.getLastName());
                    user.setDesignation(userDTO.getDesignation());
                    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                    LocalDateTime localDateTimes = HomeManagementUtil.convertTolocalDateTimeFrom(new Date());
                    user.setCreaterTimestamp(localDateTimes);
                    if (userDTO.getRoleId() == null || HomeManagementUtil.isEmptyString(userDTO.getRoleId())) {
                        Roles byRoleId = roleRepository.checkRoleExsists(user.getDesignation());
                        userDTO.setRoleId(byRoleId.getId());
                    }
                    user.setRole_id(userDTO.getRoleId());
                    user.setPhone(userDTO.getPhone());
                    user.setStatus(HomeManagementKeyConstant.ACTIVATE_PENDING);
                    userRepository.save(user);
                    Optional<User> singalUser = userRepository.findByUsername(userDTO.getUsername());
                    serviceStatus.setResult(singalUser);
                    serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                    serviceStatus.setMessage(HomeManagementKeyConstant.REGISTERED_SUCCESSFULLY);
                    return serviceStatus;
                } else if (!StringUtils.isEmpty(userDTO.getCompanyId())) {
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
                    User fetchUser = (User) mongoUserDetailsManager.loadUserByUsername(userDTO.getUsername());
                    if (!userDTO.getPrivileges().isEmpty()) {
                        for (int i = 0; i < userDTO.getPrivileges().size(); i++) {
                            userDTO.getPrivileges().get(i).setUser_id(fetchUser.getId());
                            privilegeRepository.addPrivilege(userDTO.getPrivilegesMapping().get(i));
                        }
                    }
                }
                serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                serviceStatus.setMessage(HomeManagementKeyConstant.REGISTERED_SUCCESSFULLY);
            } catch (Exception e) {
                logger.info("Exception occured while Creating User" + e.getMessage());
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
                }
            }
        } else {
            logger.info(HomeManagementKeyConstant.INVALID_PAYLOAD);
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage(HomeManagementKeyConstant.INVALID_PAYLOAD);
        }

        return serviceStatus;
    }

    public ServiceStatus<Object> getSingleUser(String userName) {
    	serviceStatus = new ServiceStatus<Object>();
        if (userName != null) {
            try {
                User user = (User) mongoUserDetailsManager.loadUserByUsername(userName);
                serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                serviceStatus.setMessage("successfully retrieved");
                serviceStatus.setResult(user);
            } catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
                }
            }
        } else {
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage(HomeManagementKeyConstant.INVALID_PAYLOAD);
        }
        return serviceStatus;
    }

    public ServiceStatus<Object> getUserDetailsInPage(Integer page, Integer size) {
    	serviceStatus = new ServiceStatus<Object>();
        if (page != null && size != null && size > 0) {
            try {
                Pageable pageable = PageRequest.of(page, size);

                Page<User> users = userRepository.findAll(pageable);
                serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                serviceStatus.setMessage("successfully fetched");
                serviceStatus.setResult(users);
            } catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
                }
            }
        } else {
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage(HomeManagementKeyConstant.INVALID_PAGINATION);
        }
        return serviceStatus;
    }

    public ServiceStatus<Object> getCompanies() {
    	serviceStatus = new ServiceStatus<Object>();
        try {
            List<CompanyMaster> companyList = companyRepository.findAll();
            if (!companyList.isEmpty()) {
                serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                serviceStatus.setMessage("successfully feched Company List");
                serviceStatus.setResult(companyList);
            } else {
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setResult(companyList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
            if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
            }
        }
        return serviceStatus;
    }

    public ServiceStatus<Object> getCompanyById(String id) {
    	serviceStatus = new ServiceStatus<Object>();
        if (id != null) {
            try {
                logger.info("getCompanyUsersById" + id);
                List<User> users = companyRepository.getUsersByComapnyId(id);
                serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                serviceStatus.setMessage(HomeManagementKeyConstant.REGISTERED_SUCCESSFULLY);
                serviceStatus.setResult(users);
            } catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
                }
            }
        } else {
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage(HomeManagementKeyConstant.INVALID_PAGINATION);
        }
        return serviceStatus;
    }

    public ServiceStatus<Object> getPageForCompanyById(String id, String status, Integer page, Integer size) {
    	serviceStatus = new ServiceStatus<Object>();
    	if (id != null) {
            try {
                logger.info("getCompanyUsersById" + id);
                Pageable pageable = PageRequest.of(page, size);
                Page<User> users = companyRepository.getUsersByComapnyIdPageable(id, status, pageable);
                serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                serviceStatus.setMessage(HomeManagementKeyConstant.REGISTERED_SUCCESSFULLY);
                serviceStatus.setResult(users);
            } catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
                }
            }
        } else {
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage(HomeManagementKeyConstant.INVALID_PAGINATION);
        }
        return serviceStatus;
    }

    public ServiceStatus<Object> getCompanyByUser(String username) {
    	serviceStatus = new ServiceStatus<Object>();
        if (username != null) {
            try {
                Optional<User> user = userRepository.findByUsername(username);
                if (user.isPresent()) {
                    User userInfo = user.get();
                    logger.info("getCompanyByUserName" + username);
                    logger.info("userInfo.getCompanyName()" + userInfo.getCompanyName());
                    List<CompanyMaster> company = companyRepository.getByCompanyId(userInfo.getCompanyName());
                    if (!company.isEmpty()) {
                        serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                        serviceStatus.setMessage("successfully fetched");
                        serviceStatus.setResult(company);
                    } else {
                        serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                        serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                    }
                } else {
                    serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                    serviceStatus.setMessage(username + " not exist ");
                }
            } catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
                }
            }
        } else {
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage("username parameter is mandatory");
        }
        return serviceStatus;
    }

    public ServiceStatus<Object> updateCompany(CompanyMaster companyMaster) {
    	serviceStatus = new ServiceStatus<Object>();
    	if (companyMaster != null && companyMaster.getName() != null && companyMaster.getAddress() != null) {
            try {
                companyRepository.editCompany(companyMaster);
                serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                serviceStatus.setMessage("successfully updated Company details");
            } catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
                }
            }
        } else {
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
        }
        return serviceStatus;
    }

    public ServiceStatus<Object> getMail(String email) {
    	serviceStatus = new ServiceStatus<Object>();
    	if (!StringUtils.isEmpty(email)) {
            try {
                boolean isUserExists = mongoUserDetailsManager.userExists(email);
                logger.info("isUserExists " + isUserExists);
                if (isUserExists) {
                    serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                    serviceStatus.setMessage("successfully retrieved");
                } else {
                    logger.info("User Name not Exists " + email);
                    serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                    serviceStatus.setMessage("User Name not Exists");
                }
            } catch (Exception e) {
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
                }
            }

        } else {
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage(HomeManagementKeyConstant.INVALID_PAYLOAD);
        }
        return serviceStatus;
    }

    public ServiceStatus<Object> getCompanyStatus(String status, String si, Integer page, Integer size, String sort) {
    	serviceStatus = new ServiceStatus<Object>();
    	try {
            if (!StringUtils.isEmpty(status) || !StringUtils.isEmpty(si)) {
                Sort sortCriteria = null;
                if (!StringUtils.isEmpty(sort)) {
                    logger.info("sort" + sort);
                    sortCriteria = new Sort(Sort.Direction.ASC, sort);
                } else {
                    sortCriteria = new Sort(Sort.Direction.ASC, "name");
                }
                Pageable pageable = PageRequest.of(page, size, sortCriteria);
                Page<CompanyMaster> companyList = companyRepository.getCompaniesByStatusAndSI(status, si, pageable);
                if (companyList != null) {
                    serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                    serviceStatus.setMessage("successfully feched Company List");
                    serviceStatus.setResult(companyList);
                } else {
                    serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                    serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                    serviceStatus.setResult(companyList);
                }
            } else {
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage("Invalid payload");

            }
        } catch (Exception e) {
            e.printStackTrace();
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);

        }
        return serviceStatus;
    }

    public ServiceStatus<Object> updateCompanyToOwner(CompanyMaster companyMaster) {
    	serviceStatus = new ServiceStatus<Object>();
    	if (companyMaster != null && companyMaster.getId() != null) {
            try {
                companyRepository.updateCompanyRoleToSI(companyMaster.getId());
                serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                serviceStatus.setMessage("successfully updated Company details");
            } catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
                }
            }
        } else {
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
        }
        return serviceStatus;
    }

    public ServiceStatus<Object> getUpdateUser(UserDTO userDTO) {
    	serviceStatus = new ServiceStatus<Object>();
    	if (!StringUtils.isEmpty(userDTO.getFirstName())
                && !StringUtils.isEmpty(userDTO.getUsername()) && !StringUtils.isEmpty(userDTO.getPassword())) {
            try {
                boolean userExists = userRepository.findByUsername(userDTO.getUsername()).isPresent();
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
                    if (userDTO.getRoleId() == null && !HomeManagementUtil.isEmptyString(userDTO.getUserId())) {
                        Optional<User> rollId = userRepository.findById(user.getId());
                        if (rollId.isPresent()) {
                            userDTO.setRoleId(rollId.get().getRole_id());
                        }
                    }
                    user.setRole_id(userDTO.getRoleId());
                    LocalDateTime localDateTime = HomeManagementUtil.convertTolocalDateTimeFrom(new Date());
                    user.setUpdatedTimestamp(localDateTime);
                    userRepository.updateUser(user);
                    serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                    serviceStatus.setMessage(HomeManagementKeyConstant.UPDATED_SUCCESSFULLY);
                    return serviceStatus;
                } else {
                    serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                    serviceStatus.setMessage(HomeManagementKeyConstant.USER_ALREADY_EXIST);
                }
            } catch (Exception e) {
                logger.info(HomeManagementKeyConstant.USER_EXCEPTION + e.getMessage());
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
                }
            }
        } else {
            logger.info(HomeManagementKeyConstant.INVALID_PAYLOAD);
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage(HomeManagementKeyConstant.INVALID_PAYLOAD);
        }
        return serviceStatus;
    }

    public ServiceStatus<Object> createMultipleUsersForHome(List<UserDTO> userDTOs) {
    	serviceStatus = new ServiceStatus<Object>();
    	if (userDTOs != null) {
            try {
                for (UserDTO userDTO : userDTOs) {
                    boolean userExists = userRepository.findByUsername(userDTO.getUsername()).isPresent();
                    if (userExists) {
                        logger.info(HomeManagementKeyConstant.USER_ALREADY_EXIST + userDTO.getUsername());
                    } else if (StringUtils.isEmpty(userDTO.getCompanyId())) {
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
                        serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                        serviceStatus.setMessage(HomeManagementKeyConstant.REGISTERED_SUCCESSFULLY);
                        return serviceStatus;
                    } else if (!StringUtils.isEmpty(userDTO.getCompanyId())) {
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
                        User fetchUser = (User) mongoUserDetailsManager.loadUserByUsername(userDTO.getUsername());
                        if (!userDTO.getPrivilegesMapping().isEmpty()) {
                            for (int i = 0; i < userDTO.getPrivilegesMapping().size(); i++) {
                                userDTO.getPrivilegesMapping().get(i).setUser_id(fetchUser.getId());
                                privilegeRepository.addPrivilege(userDTO.getPrivilegesMapping().get(i));
                            }
                        }
                        serviceStatus.setResult(userResult);
                    }
                    serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                    serviceStatus.setMessage(HomeManagementKeyConstant.REGISTERED_SUCCESSFULLY);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.info(HomeManagementKeyConstant.USER_EXCEPTION + e.getMessage());
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
                }
            }
        } else {
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage(HomeManagementKeyConstant.INVALID_PAYLOAD);
        }
        return serviceStatus;
    }

    public ServiceStatus<Object> updateMultipleUsersForHome(List<UserDTO> userDTOs) {
    	serviceStatus = new ServiceStatus<Object>();
    	if (userDTOs != null) {
            try {
                for (UserDTO userDTO : userDTOs) {
                    boolean userExists = userRepository.findByUsername(userDTO.getUsername()).isPresent();
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
                        if (userDTO.getRoleId() == null && !HomeManagementUtil.isEmptyString(userDTO.getUserId())) {
                            Optional<User> rollId = userRepository.findById(user.getId());
                            if (rollId.isPresent()) {
                                userDTO.setRoleId(rollId.get().getRole_id());
                            }
                        }
                        user.setRole_id(userDTO.getRoleId());
                        LocalDateTime localDateTime = HomeManagementUtil.convertTolocalDateTimeFrom(new Date());
                        user.setUpdatedTimestamp(localDateTime);
                        userRepository.updateUser(user);
                        List<PrivilegesMapping> privileges = userDTO.getPrivilegesMapping();
                        if (!privileges.isEmpty()) {
                            privilegeRepository.deletePrivilegesByUserId(privileges.get(0).getUser_id());
                            for (PrivilegesMapping privilege : privileges) {
                                privilegeRepository.addPrivilege(privilege);
                                serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                                serviceStatus.setMessage("Privileges added successfully");
                            }
                        }
                        serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                        serviceStatus.setMessage(HomeManagementKeyConstant.UPDATED_SUCCESSFULLY);
                    } else {
                        serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                        serviceStatus.setMessage(HomeManagementKeyConstant.USER_ALREADY_EXIST);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.info(HomeManagementKeyConstant.USER_EXCEPTION + e.getMessage());
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
                }
            }
        } else {
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage(HomeManagementKeyConstant.INVALID_PAYLOAD);
        }
        return serviceStatus;
    }

    public ServiceStatus<Object> getUserByUserId(String id) {
    	serviceStatus = new ServiceStatus<Object>();
    	if (id != null && id.length() > 0) {
            try {
                Optional<User> user = userRepository.findById(id);
                if (user.isPresent()) {
                    serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                    serviceStatus.setMessage(HomeManagementKeyConstant.UPDATED_SUCCESSFULLY);
                    serviceStatus.setResult(user);
                    return serviceStatus;
                } else {
                    serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                    serviceStatus.setMessage(HomeManagementKeyConstant.USER_ALREADY_EXIST);
                }
            } catch (Exception e) {
                logger.info(HomeManagementKeyConstant.USER_EXCEPTION + e.getMessage());
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
                }
            }
        } else {
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage("invalid user id");
        }
        return serviceStatus;
    }

    public ServiceStatus<Object> sendMailToCustomer(EmailVo emailVo) {
    	serviceStatus = new ServiceStatus<Object>();
    	if (emailVo != null) {
            try {
                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(emailVo.getToUser());
                String parseHtmlTemplate = HomeManagementUtil.parseHtmlTemplate(emailVo);
                helper.setText(parseHtmlTemplate, true);
                helper.setSubject(emailVo.getEmail_subject());
                sender.send(message);
                serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                serviceStatus.setMessage("successfully email sent");
            } catch (Exception eMag) {
                eMag.printStackTrace();
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                if (eMag instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
                }
            }
        } else {
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage("invalid email payload");
        }
        return serviceStatus;
    }

    public ServiceStatus<Object> getMailToken(String emailId) {
    	serviceStatus = new ServiceStatus<Object>();
    	if (emailId != null && emailId.length() > 0) {
            try {
                Optional<User> mailingUser = userRepository.findById(emailId);
                if (mailingUser.isPresent()) {
                    serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                    serviceStatus.setMessage("successfully  user");
                    serviceStatus.setResult(mailingUser);
                    return serviceStatus;
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("Exception occurs getting email token" + e.getMessage());
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
                }
            }
        } else {

            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage("invalid email token");
        }
        return serviceStatus;
    }

    public ServiceStatus<Object> updateLastUserLogin(String userName, String lastLogin) {
    	serviceStatus = new ServiceStatus<Object>();
    	if (userName != null && lastLogin != null) {
            try {
                userRepository.updateLastLogin(userName, lastLogin);
                serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                serviceStatus.setMessage("Last login updated successfully");
                logger.info(serviceStatus);
            } catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
                }
                logger.error(serviceStatus);
            }
        } else {
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage(HomeManagementKeyConstant.INVALID_PAYLOAD);
            logger.debug(serviceStatus);
        }
        return serviceStatus;
    }

    public ServiceStatus<Object> updateStatusForRegUser(String userId) {
    	serviceStatus = new ServiceStatus<Object>();
    	if (userId != null && !HomeManagementUtil.isEmptyString(userId)) {
            try {
                userRepository.updateStatusForRegistrationUser(userId);
                List<Privileges> privilegeList = AuthServiceImpl.getPrivileges(privilegeRepository);
                for (int i = 0; i < privilegeList.size(); i++) {
                    privilegeList.get(i).setUser_id(userId);
                    PrivilegesMapping pList = new PrivilegesMapping(null, privilegeList.get(i).getName(),
                            privilegeList.get(i).getValue(), null, userId, privilegeList.get(i).getId());
                    privilegeRepository.addPrivilege(pList);
                }
                serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                serviceStatus.setMessage("Status updated successfully");
                logger.info(serviceStatus);
            } catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage(HomeManagementKeyConstant.DATAINTGRTY_VIOLATION);
                }
                logger.error(serviceStatus);
            }
        } else {
            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
            serviceStatus.setMessage(HomeManagementKeyConstant.INVALID_PAYLOAD);
            logger.debug(serviceStatus);
        }
        return serviceStatus;
    }

    @Override
    public ServiceStatus<Object> verifyRegLink(UserDTO registrationUser) {
    	serviceStatus = new ServiceStatus<Object>();
    	if (!StringUtils.isEmpty(registrationUser.getUsername())) {
            try {
                Optional<User> findByUsername = userRepository.findByUsername(registrationUser.getUsername());
                if (findByUsername.isPresent()) {
                    String status = findByUsername.get().getStatus();
                    if (HomeManagementConstant.ACTIVE.getKey().equals(status)) {
                        logger.info("User is Active" + registrationUser.getUsername());
                        serviceStatus.setStatus(HomeManagementKeyConstant.SUCCESS);
                        serviceStatus.setMessage("User is Active");
                        return serviceStatus;
                    } else {
                        if (HomeManagementConstant.ACTIVE_PENDING.getKey().equals(status) && ObjectUtils.anyNotNull(registrationUser.getUsername())) {
                            for (int i = 0; i <= 1000; i++) {
                                Thread.sleep(2000);
                                if (i == 60) {
                                    userRepository.deleteUserByStatus(registrationUser.getUsername(), status);
                                    Thread.currentThread().interrupt();
                                }
                            }
                            serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                            serviceStatus.setMessage("Link expaired");
                        }
                    }
                }
            } catch (InterruptedException e) {
                logger.info("Exception In registered User" + e.getMessage());
                serviceStatus.setStatus(HomeManagementKeyConstant.FAILURE);
                serviceStatus.setMessage(HomeManagementKeyConstant.FAILURE);
                Thread.currentThread().interrupt();
            }
        }
        return serviceStatus;
    }
}

