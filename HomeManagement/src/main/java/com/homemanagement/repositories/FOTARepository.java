package com.homemanagement.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.homemanagement.domain.FOTA;

public interface FOTARepository extends MongoRepository<FOTA, String>, FOTARepositoryBase {

    

}
