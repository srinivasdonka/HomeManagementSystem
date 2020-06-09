package com.homemanagement.repositories;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Update.update;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.homemanagement.domain.DeviceConfiguration;
import com.homemanagement.domain.User;
import com.mongodb.client.result.UpdateResult;

@Component
public class UserRepositoryImpl implements UserRepositoryBase {

	private final MongoTemplate mongoTemplate;

	public UserRepositoryImpl(final MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public boolean changePassword(final String oldPassword,
			final String newPassword,
			final String username) {
		final Query searchUserQuery = new Query(where("username").is(username).andOperator(where("password").is(oldPassword)));
		final UpdateResult updateResult = mongoTemplate.updateFirst(searchUserQuery, update("password", newPassword), User.class);
		return updateResult.wasAcknowledged();
	}

	@Override
	public void updateUser(User user) {

		final Query query = Query.query(Criteria.where("username").is(user.getUsername()));

		Update update = new Update();
		update.set("password", user.getPassword());
		update.set("first_name", user.getFirstName());
		update.set("last_name", user.getLastName());
		update.set("designation", user.getDesignation());
		update.set("role_id", user.getRole_id());
		update.set("phone", user.getPhone());
		update.set("status",user.getStatus());
		update.set("upated_timestamp", user.getUpdatedTimestamp());

		this.mongoTemplate.updateFirst(query, update, User.class);		


	}

	@Override
	public void addMultipleUsers(List<User> users) {
		this.mongoTemplate.insert(users,DeviceConfiguration.class);
	}

	@Override
	public void updateMultipleUsers(List<User> users) {

		Query query=null;
		Update update=null;
		for(User user:users){
			query = Query.query(Criteria.where("username").is(user.getUsername()));

			update = new Update();
			update.set("password", user.getPassword());
			update.set("first_name", user.getFirstName());
			update.set("last_name", user.getLastName());
			update.set("designation", user.getDesignation());
			update.set("role_id", user.getRole_id());
			update.set("phone", user.getPhone());
			update.set("status",user.getStatus());
			update.set("upated_timestamp", user.getUpdatedTimestamp());
			this.mongoTemplate.updateFirst(query, update, DeviceConfiguration.class);
		}
	}

	@Override
	public void updateLastLogin(String userName,String lastLogin) {

		final Query query = Query.query(Criteria.where("username").is(userName));
		Update update = new Update();
		update.set("lastLogin", lastLogin);
		this.mongoTemplate.updateFirst(query, update, User.class);

	}

	@Override
	public void updateStatusForRegistrationUser(String id) {
		final Query query = Query.query(Criteria.where("id").is(id));
		Update update = new Update();
		update.set("status", "Active");
		this.mongoTemplate.updateFirst(query, update, User.class);		
	}
	
	@Override
	public void deleteUserByStatus(String username, String status) {
		final Query deleteUserQuery = new Query(where("username").is(username).andOperator(where("status").is(status)));
		this.mongoTemplate.findAndRemove(deleteUserQuery, User.class);		
	}

	/*
	 * @Override public void createRegistrationUser(RegistrationUser
	 * registrationUser) { this.mongoTemplate.save(registrationUser);
	 * 
	 * }
	 */
}
