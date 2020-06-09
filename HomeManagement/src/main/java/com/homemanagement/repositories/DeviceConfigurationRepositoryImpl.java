package com.homemanagement.repositories;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.homemanagement.domain.DeviceConfiguration;

@Component
public class DeviceConfigurationRepositoryImpl implements DeviceConfigurationRepositoryBase {
	
	
	private final MongoTemplate mongoTemplate;

    public DeviceConfigurationRepositoryImpl(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


	
    @Override
	public void addDeviceConfiguration(DeviceConfiguration deviceconfiguration) {
		this.mongoTemplate.save(deviceconfiguration);
	}

    @Override
	public void updateDeviceConfiguration(DeviceConfiguration deviceconfiguration) {
		
		final Query query = Query.query(Criteria.where("id").is(deviceconfiguration.getId()).andOperator(where("device_id").is(deviceconfiguration.getDevice_id())));
		
		Update update = new Update();
		update.set("config_property_name", deviceconfiguration.getConfig_property_name());
		update.set("config_property_value", deviceconfiguration.getConfig_property_value());
		update.set("last_updated", deviceconfiguration.getLast_updated());
		
         this.mongoTemplate.updateFirst(query, update, DeviceConfiguration.class);
		
	}
    
    

    @Override
    public List<DeviceConfiguration> getDeviceConfigurationListByDeviceId(String device_id)
	{
	final Query query = Query.query(Criteria.where("device_id").is(device_id));
	
	return mongoTemplate.find(query, DeviceConfiguration.class);


	}
    
    @Override
    public DeviceConfiguration getDeviceConfigurationByDeviceIdAndConfigPropName(String device_id,String config_property_name )
    {
    	final Query query = Query.query(Criteria.where("device_id").is(device_id).andOperator(where("config_property_name").is(config_property_name)));
    	
    	return mongoTemplate.findOne(query, DeviceConfiguration.class);
    }
	
    @Override
    public void updateDeviceSyncConfiguration(DeviceConfiguration deviceconfiguration) 
    {
    	final Query query = Query.query(Criteria.where("id").is(deviceconfiguration.getId()).andOperator(where("device_id").is(deviceconfiguration.getDevice_id())));
		
		Update update = new Update();
		update.set("is_sync", deviceconfiguration.getIs_sync());		
		update.set("last_updated", deviceconfiguration.getLast_updated());
		
         this.mongoTemplate.updateFirst(query, update, DeviceConfiguration.class);
    	
    }
    
    public  void addDeviceConfigurationList(List<DeviceConfiguration> deviceconfiguration){
    	
 	   this.mongoTemplate.insert(deviceconfiguration,DeviceConfiguration.class);
     }
    
 	public void updateDeviceConfigurationList(List<DeviceConfiguration> deviceconfiguration)
 	{
 		 Query query=null;
 		 Update update=null;
 		for(DeviceConfiguration deviceconfig:deviceconfiguration){
 			query = Query.query(Criteria.where("id").is(deviceconfig.getId()).andOperator(where("device_id").is(deviceconfig.getDevice_id()).andOperator(where("config_property_name").is(deviceconfig.getConfig_property_name()).andOperator(where("config_property_type").is(deviceconfig.getConfig_property_type())))));
 		
 			update = new Update();
 			update.set("config_property_value", deviceconfig.getConfig_property_value());
 			update.set("last_updated", deviceconfig.getLast_updated());
 		
           this.mongoTemplate.updateFirst(query, update, DeviceConfiguration.class);
 		}
 	}
 	
 	@Override
     public List<DeviceConfiguration> getDeviceConfigurationByDeviceIdAndConfigPropType(String device_id,String config_property_type){
 		
     	final Query query = Query.query(Criteria.where("device_id").is(device_id).andOperator(where("config_property_type").is(config_property_type)));
     	
     	return mongoTemplate.find(query, DeviceConfiguration.class);
     }
 	
 	@Override
    public List<DeviceConfiguration> getDeviceConfigurationbySync(String device_id){
 		
    	final Query query = Query.query(Criteria.where("device_id").is(device_id).andOperator(where("is_sync").is("1")));
    	return mongoTemplate.find(query, DeviceConfiguration.class);
    	
    }
}
