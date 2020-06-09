package com.homemanagement.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.homemanagement.domain.CompanyMaster;

public interface CompanyRepository extends MongoRepository<CompanyMaster, String>, CompanyRepositoryBase {

}
