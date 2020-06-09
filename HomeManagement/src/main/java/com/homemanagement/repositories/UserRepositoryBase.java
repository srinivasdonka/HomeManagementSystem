package com.homemanagement.repositories;

import java.util.List;

import com.homemanagement.domain.User;

public interface UserRepositoryBase {

	boolean changePassword(String oldPassword, String newPassword, String username);

	void updateUser(User user);

	void addMultipleUsers(List<User> users);

	void updateMultipleUsers(List<User> users);

	void updateLastLogin(String userName,String lastLogin);
	
	void updateStatusForRegistrationUser(String userId);
	
	//void createRegistrationUser(RegistrationUser registrationUser);
	
	void deleteUserByStatus(String username, String status);

}
