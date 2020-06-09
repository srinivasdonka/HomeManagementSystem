package com.homemanagement.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.homemanagement.domain.MongoApproval;


public interface ApprovalRepository extends MongoRepository<MongoApproval, String>, ApprovalRepositoryBase {
}
