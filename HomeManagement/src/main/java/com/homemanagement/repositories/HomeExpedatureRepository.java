package com.homemanagement.repositories;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.homemanagement.domain.HomeExpendature;;

public interface HomeExpedatureRepository extends MongoRepository<HomeExpendature, String>, HomeExpedatureRepositoryBase {

}
