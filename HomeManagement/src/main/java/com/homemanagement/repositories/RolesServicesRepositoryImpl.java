package com.homemanagement.repositories;


import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class RolesServicesRepositoryImpl implements RolesServicesRepositoryBase {

    public RolesServicesRepositoryImpl(final MongoTemplate mongoTemplate) {
    }
   
}
