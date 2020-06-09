package com.homemanagement.repositories;



import static org.springframework.data.mongodb.core.query.Criteria.where;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.homemanagement.domain.FOTA;


@Component
public class FOTARepositoryImpl implements FOTARepositoryBase {

    private final MongoTemplate mongoTemplate;

    public FOTARepositoryImpl(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


	@Override
	public void addFotaUploadedPath(FOTA fota) {
		try {
			
			this.mongoTemplate.save(fota);
		
		}catch(Exception e){
			
		}
		
	}

	@Override
	public void updateFota(FOTA fota) {
		final Query query = Query.query(Criteria.where("device_id").is(fota.getDevice_id()));
		//Roles rolesUpdate = mongoTemplate.findOne(query, Roles.class);
		
		Update update = new Update();
		update.set("version", fota.getVersion());
		update.set("description", fota.getDescription());
		update.set("path", fota.getFile_path());
		
         this.mongoTemplate.updateFirst(query, update, FOTA.class);
		
	}

	@Override
	public FOTA getFOTAPath(String device_id, String version) {
		final Query query = Query.query(Criteria.where("device_id").is(device_id).andOperator(where("version").is(version)));
		return mongoTemplate.findOne(query, FOTA.class);
	}
}
