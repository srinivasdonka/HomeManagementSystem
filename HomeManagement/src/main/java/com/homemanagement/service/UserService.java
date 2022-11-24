package com.homemanagement.service;

import com.homemanagement.domain.CompanyMaster;
import com.homemanagement.domain.UserDTO;
import com.homemanagement.dto.EmailVo;
import com.homemanagement.dto.ServiceStatus;
import java.util.List;

public interface UserService {
    ServiceStatus<Object> getCreateUserService(UserDTO userDTO);
    ServiceStatus<Object> getSingleUser(String userName);
    ServiceStatus<Object> getUserDetailsInPage(Integer page, Integer size);
    ServiceStatus<Object> getCompanies();
    ServiceStatus<Object> getCompanyById(String id);
    ServiceStatus<Object> getPageForCompanyById(String id, String status, Integer page, Integer size);
    ServiceStatus<Object> getCompanyByUser(String username);
    ServiceStatus<Object> updateCompany(CompanyMaster companyMaster);
    ServiceStatus<Object> getMail(String email);
    ServiceStatus<Object> getCompanyStatus(String status, String si, Integer page, Integer size, String sort);
    ServiceStatus<Object> updateCompanyToOwner(CompanyMaster companyMaster);
    ServiceStatus<Object> getUpdateUser(UserDTO userDTO);
    ServiceStatus<Object> createMultipleUsersForHome(List<UserDTO> userDTOs);
    ServiceStatus<Object> updateMultipleUsersForHome(List<UserDTO> userDTOs);
    ServiceStatus<Object> getUserByUserId(String id);
    ServiceStatus<Object> sendMailToCustomer(EmailVo emailVo);
    ServiceStatus<Object> getMailToken(String emailId);
    ServiceStatus<Object> updateLastUserLogin(String userName, String lastLogin);
    ServiceStatus<Object> updateStatusForRegUser(String userId);
    ServiceStatus<Object> verifyRegLink(UserDTO registrationUser);
}
