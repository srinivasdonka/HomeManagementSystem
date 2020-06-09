package com.homemanagement.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.homemanagement.domain.DeviceMaster;


public interface DeviceMasterRepository extends MongoRepository<DeviceMaster, String>, DeviceMasterRepositoryBase {

}
