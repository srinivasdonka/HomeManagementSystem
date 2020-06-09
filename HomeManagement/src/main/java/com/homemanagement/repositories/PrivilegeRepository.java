package com.homemanagement.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.homemanagement.domain.Privileges;

@Repository("privilegeRepository")
public interface PrivilegeRepository extends MongoRepository<Privileges, String>, PrivilegeRepositoryBase {

    

}
