package com.homemanagement.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.homemanagement.domain.MongoClientDetails;

public interface ClientDetailsRepository extends MongoRepository<MongoClientDetails, String>, ClientDetailsRepositoryBase {

}
