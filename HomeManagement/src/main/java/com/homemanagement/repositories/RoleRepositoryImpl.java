package com.homemanagement.repositories;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.homemanagement.domain.Privileges;
import com.homemanagement.domain.Roles;

@Component
public class RoleRepositoryImpl implements RoleRepositoryBase {

    private final MongoTemplate mongoTemplate;

    public RoleRepositoryImpl(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

	@Override
	public void addRole(Roles role) {
		try {
			
			this.mongoTemplate.save(role);
		
		}catch(Exception e){
			
		}
	}

	@Override
	public void updateRole(Roles role) {
		final Query query = Query.query(Criteria.where("id").is(role.getId()));
		//Roles rolesUpdate = mongoTemplate.findOne(query, Roles.class);
		
		Update update = new Update();
		update.set("name", role.getName());
		update.set("description", role.getDescription());
		
         this.mongoTemplate.updateFirst(query, update, Roles.class);
		
	}

	@Override
	public List<Roles> getAllRoleList() {
		return mongoTemplate.findAll(Roles.class);
	}

	@Override
	public List<Roles> getByRoleId(String roleId) {
		 final Query query = Query.query(Criteria.where("id").is(roleId));
	        return mongoTemplate.find(query, Roles.class);
	}

	@Override
	public List<Privileges> getPrivilegesByRoleID(String role_id) {
		final Query query = Query.query(Criteria.where("role_id").is(role_id));
        return mongoTemplate.find(query, Privileges.class);
	}
	@Override
	public Roles checkRoleExsists(String role_name) {
		final Query query = Query.query(Criteria.where("name").is(role_name));
        return mongoTemplate.findOne(query, Roles.class);
	}
}
