package com.homemanagement.repositories;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.homemanagement.domain.Privileges;
import com.homemanagement.domain.PrivilegesMapping;

@Component
public class PrivilegeRepositoryImpl implements PrivilegeRepositoryBase {

	private final MongoTemplate mongoTemplate;

	public PrivilegeRepositoryImpl(final MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public void addPrivilege(PrivilegesMapping privilage) {
		try {
			this.mongoTemplate.save(privilage);
		}catch(Exception e){
		}

	}

	@Override
	public void updatePrivilege(PrivilegesMapping privilage) {
		final Query query = Query.query(Criteria.where("id").is(privilage.getId()));

		Update update = new Update();
		update.set("name", privilage.getName());
		update.set("role_id", privilage.getRole_id());
		update.set("value", privilage.getValue());
		this.mongoTemplate.updateFirst(query, update, PrivilegesMapping.class);		
	}

	@Override
	public List<Privileges> getAllPrivilegeList() {
		return mongoTemplate.findAll(Privileges.class);
	}

	@Override
	public List<Privileges> getByPrivilegeId(String privilegeId) {
		final Query query = Query.query(Criteria.where("id").is(privilegeId));
		return mongoTemplate.find(query, Privileges.class);
	}

	@Override
	public List<PrivilegesMapping> getByPrivilegeByRoleAndUserId(String user_id) {
		final Query query = Query.query(Criteria.where("user_id").is(user_id));
		return mongoTemplate.find(query, PrivilegesMapping.class);

	}

	@Override
	public PrivilegesMapping getIndividualRoleAndUser(String privilegeId, String user_id) {
		final Query query = Query.query(Criteria.where("privilegeId").is(privilegeId).andOperator(where("user_id").is(user_id)));
		return mongoTemplate.findOne(query, PrivilegesMapping.class);
	}


	@Override
	public void deletePrivilegesByUserId(String user_id) {
		final Query query = Query.query(Criteria.where("user_id").is(user_id));
		this.mongoTemplate.remove(query, PrivilegesMapping.class);
	}
}
