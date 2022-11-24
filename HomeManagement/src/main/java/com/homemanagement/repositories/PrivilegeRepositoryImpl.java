package com.homemanagement.repositories;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;

import com.homemanagement.dto.PrivilegesMappingDTO;
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
	public void updatePrivilege(PrivilegesMapping privilageMap) {
		final Query query = Query.query(Criteria.where("id").is(privilageMap.getId()));

		Update update = new Update();
		update.set("name", privilageMap.getName());
		update.set("role_id", privilageMap.getRole_id());
		update.set("value", privilageMap.getValue());
		this.mongoTemplate.updateFirst(query, update, PrivilegesMapping.class);		
	}

	@Override
	public List<PrivilegesMapping> getAllPrivilegeList() {
		return mongoTemplate.findAll(PrivilegesMapping.class);
	}

	@Override
	public List<Privileges> getByPrivilegeId(String privilegeId) {
		final Query query = Query.query(Criteria.where("id").is(privilegeId));
		return mongoTemplate.find(query, Privileges.class);
	}
	@Override
	public List<PrivilegesMapping> getByPrivilegeByRoleAndUserId(String userId) {
		final Query query = Query.query(Criteria.where("user_id").is(userId));
		return mongoTemplate.find(query, PrivilegesMapping.class);

	}
	@Override
	public PrivilegesMapping getIndividualRoleAndUser(String privilegeId, String userId) {
		final Query query = Query.query(Criteria.where("privilegeId").is(privilegeId).andOperator(where("user_id").is(userId)));
		return mongoTemplate.findOne(query, PrivilegesMapping.class);
	}


	@Override
	public void deletePrivilegesByUserId(String userId) {
		final Query query = Query.query(Criteria.where("user_id").is(userId));
		this.mongoTemplate.remove(query, PrivilegesMapping.class);
	}
}
