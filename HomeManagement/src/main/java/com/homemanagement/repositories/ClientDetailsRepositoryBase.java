package com.homemanagement.repositories;

import com.homemanagement.domain.MongoClientDetails;

public interface ClientDetailsRepositoryBase {
    boolean deleteByClientId(String clientId);

    boolean update(MongoClientDetails mongoClientDetails);

    boolean updateClientSecret(String clientId, String newSecret);

    MongoClientDetails findByClientId(String clientId);
}
