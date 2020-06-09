package com.homemanagement.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.homemanagement.domain.CompanyMaster;
import com.homemanagement.domain.User;
public interface CompanyRepositoryBase {

	void addCompany(CompanyMaster companyMaster);

	void editCompany(CompanyMaster companyMaster);
	
	List<CompanyMaster> getAllCompanyMasterList();
	
	List<CompanyMaster> getByCompanyId(String companyId);
	
	List<User> getUsersByComapnyId(String companyId);
	Page<User> getUsersByComapnyIdPageable(String companyId, String status , Pageable pageable);
	Page<CompanyMaster> getCompaniesByStatusAndSI(String status,String is_SI ,Pageable pageable);
	void updateCompanyRoleToSI(String companyId);

}
