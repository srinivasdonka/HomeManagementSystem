package com.homemanagement.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.homemanagement.domain.Roles;

public interface RoleRepository extends MongoRepository<Roles, String>, RoleRepositoryBase {

    

}
