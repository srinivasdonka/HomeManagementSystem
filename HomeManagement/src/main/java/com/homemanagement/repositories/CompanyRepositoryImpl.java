package com.homemanagement.repositories;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import com.homemanagement.domain.CompanyMaster;
import com.homemanagement.domain.Roles;
import com.homemanagement.domain.User;

@Component
public class CompanyRepositoryImpl implements CompanyRepositoryBase {

    private final MongoTemplate mongoTemplate;

    public CompanyRepositoryImpl(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

	@Override
	public void addCompany(CompanyMaster companyMaster) {
		this.mongoTemplate.save(companyMaster);
	}

	@Override
	public void editCompany(CompanyMaster companyMaster) {
		final Query query = Query.query(Criteria.where("id").is(companyMaster.getId()));
		
        Update update = new Update();
 			update.set("name", companyMaster.getName());
 			update.set("no_of_active_users", companyMaster.getNo_of_active_users());
 			update.set("is_SI", companyMaster.getIs_SI());
 			update.set("address", companyMaster.getAddress());
 			update.set("no_of_users", companyMaster.getNo_of_users());
         this.mongoTemplate.updateFirst(query, update, CompanyMaster.class);
	}

	@Override
	public List<CompanyMaster> getAllCompanyMasterList() {
		 
		return mongoTemplate.findAll(CompanyMaster.class);
	}

	@Override
	public List<CompanyMaster> getByCompanyId(String companyId) {
		 final Query query = Query.query(Criteria.where("id").is(companyId));
	        return mongoTemplate.find(query, CompanyMaster.class);
	}

	@Override
	public List<User> getUsersByComapnyId(String companyId) {
		final Query query = Query.query(Criteria.where("company_name").is(companyId));
        return mongoTemplate.find(query, User.class);
	}
	@Override
	public Page<User> getUsersByComapnyIdPageable(String companyId, String status , Pageable pageable) {
		final Query query = Query.query(Criteria.where("company_name").is(companyId).andOperator(where("status").is(status)));
		query.with(pageable);
		List<User> list = mongoTemplate.find(query, User.class);
       
		return PageableExecutionUtils.getPage(list, pageable, () -> mongoTemplate.count(query, User.class));
	}
	@Override
	public Page<CompanyMaster> getCompaniesByStatusAndSI(String status,String is_SI ,Pageable pageable)
	{
		Integer is_active=0;			
		if(status!=null){
			is_active = Integer.valueOf(status);
			
		}
		
		Integer is_SILocal = 0;
		if(is_SI!=null){
			is_SILocal = Integer.valueOf(is_SI);
		
			}
		
				
	final Query query = Query.query(Criteria.where("is_active").is(is_active).andOperator(where("is_SI").is(is_SILocal)));
	
		query.with(pageable);
		List<CompanyMaster> list = mongoTemplate.find(query, CompanyMaster.class);
 
		return PageableExecutionUtils.getPage(list, pageable, () -> mongoTemplate.count(query, CompanyMaster.class));
		
	}
	@Override
	public void updateCompanyRoleToSI(String companyId)
	{
		// SI 0 :Company is not Service Integrator ,SI 1 :Company is Service Integrator
	Integer SI=1;
	final Query query = Query.query(Criteria.where("id").is(companyId));

	Update updateCompany = new Update();
		
	updateCompany.set("is_SI", SI);

	this.mongoTemplate.updateFirst(query, updateCompany, CompanyMaster.class);
	String roleName="SI";
	final Query roleQuery = Query.query(Criteria.where("name").is(roleName));
    Roles role =  mongoTemplate.findOne(roleQuery, Roles.class);
    if(role!=null){
	
	final Query userQuery = Query.query(Criteria.where("company_name").is(companyId));
	Update updateUser = new Update();
	
	updateUser.set("role_id", role.getId());
	

	this.mongoTemplate.updateMulti(userQuery , updateUser, User.class);
    }
	}
}
