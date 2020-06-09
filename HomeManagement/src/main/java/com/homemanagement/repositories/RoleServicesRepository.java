package com.homemanagement.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.homemanagement.domain.RolesServiceMapping;

public interface RoleServicesRepository extends MongoRepository<RolesServiceMapping, String>, RolesServicesRepositoryBase {

  

}
