package com.homemanagement.repositories;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.homemanagement.domain.DeviceMaster;



@Component
public class DeviceMasterRepositoryImpl implements DeviceMasterRepositoryBase {


	private final MongoTemplate mongoTemplate;

	public DeviceMasterRepositoryImpl(final MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public void addDeviceMaster(DeviceMaster deviceMaster) {
		this.mongoTemplate.save(deviceMaster);
	}

	@Override
	public boolean checkDeviceExists(String device_mac_id,String device_key){
		Query query = null;
		if(device_mac_id != null) {
			query = Query.query(Criteria.where("device_mac_id").is(device_mac_id));
			if(device_key != null) {
				query = Query.query(Criteria.where("device_mac_id").is(device_mac_id).andOperator(where("hardware_key").is(device_key)));
			}
		}

		return mongoTemplate.exists(query, DeviceMaster.class);
	}

	@Override
	public void updatePhoneHomeDevice(DeviceMaster deviceMaster) {
		final Query query = Query.query(Criteria.where("device_mac_id").is(deviceMaster.getDevice_mac_id()));

		Update update = new Update();
		update.set("model_name", deviceMaster.getModel_name());
		update.set("device_type", deviceMaster.getDevice_type());
		update.set("hardware_version", deviceMaster.getHardware_version());
		update.set("serial_number", deviceMaster.getSerial_number());
		update.set("hardware_key", deviceMaster.getHardware_key());
		update.set("created_date", deviceMaster.getCreated_date());
		update.set("last_updated", deviceMaster.getLast_updated());
		update.set("miscellaneous", deviceMaster.getMiscellaneous());
		this.mongoTemplate.updateFirst(query, update, DeviceMaster.class);

	}


	@Override
	public DeviceMaster getByDeviceMacId(String device_mac_id,String device_key){
		Query query = null;
		if(device_mac_id != null) {
			query = Query.query(Criteria.where("device_mac_id").is(device_mac_id));
			if(device_key != null) {
				query = Query.query(Criteria.where("device_mac_id").is(device_mac_id).andOperator(where("hardware_key").is(device_key)));
			}
		}
		return mongoTemplate.findOne(query, DeviceMaster.class);

	}
}
