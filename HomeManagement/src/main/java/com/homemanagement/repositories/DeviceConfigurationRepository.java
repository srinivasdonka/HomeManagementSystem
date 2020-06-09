package com.homemanagement.repositories;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.homemanagement.domain.DeviceConfiguration;;

public interface DeviceConfigurationRepository extends MongoRepository<DeviceConfiguration, String>, DeviceConfigurationRepositoryBase { 

}
