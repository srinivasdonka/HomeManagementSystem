package com.homemanagement.repositories;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.support.PageableExecutionUtils;

import com.homemanagement.domain.HomeExpendature;
import com.homemanagement.domain.DeviceNetwork;


public class HomeExpedatureRepositoryImpl implements HomeExpedatureRepositoryBase  {


	private final MongoTemplate mongoTemplate;

	public HomeExpedatureRepositoryImpl(final MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public void addDevice(HomeExpendature device) {
		this.mongoTemplate.save(device);
	}

	@Override
	public List<HomeExpendature> getDeviceListByCompanyIdAndActiveStatus(String companyId,boolean activeStatus)
	{
		final Query query = Query.query(Criteria.where("company_id").is(companyId).andOperator(where("is_active").is(activeStatus)));

		return mongoTemplate.find(query, HomeExpendature.class);
	}


	@Override
	public HomeExpendature getItemeByItemId(String id)
	{
		final Query query = Query.query(Criteria.where("id").is(id));
		return mongoTemplate.findOne(query, HomeExpendature.class);
	}

	@Override
	public Page<HomeExpendature> getDeviceListByCompanyIdAndActiveStatusPageable(String companyId,boolean activeStatus,Pageable pageable){

		final Query query = Query.query(Criteria.where("company_id").is(companyId).andOperator(where("is_active").is(activeStatus)));

		query.with(pageable);
		List<HomeExpendature> list = mongoTemplate.find(query, HomeExpendature.class);

		return PageableExecutionUtils.getPage(list, pageable, () -> mongoTemplate.count(query, HomeExpendature.class));

	}

	@Override
	public boolean changeDeviceCompany(String companyId, String destComapnyId, String deviceId,String deviceToken) {

		try {
			final Query query = Query.query(Criteria.where("device_id").is(deviceId));

			Update update = new Update();
			update.set("company_id", destComapnyId);
			update.set("device_token", deviceToken);

			this.mongoTemplate.updateFirst(query, update, HomeExpendature.class);
			return true;
		}
		catch(Exception e) {
			return false;
		}

	}

	@Override
	public void updateItem(HomeExpendature homeExp) {
		final Query query = Query.query(Criteria.where("id").is(homeExp.getId()));
		Update update = new Update();
		update.set("item_status", homeExp.getItem_status());
		update.set("item_name", homeExp.getItem_name());
		update.set("item_type", homeExp.getItem_type());
		update.set("item_id", homeExp.getItem_id());
		update.set("item_price", homeExp.getItem_price());
		update.set("item_purchase_date", homeExp.getItem_purchase_date());
		this.mongoTemplate.updateFirst(query, update, HomeExpendature.class);
	}

	@Override
	public void addNetwork(DeviceNetwork deviceNetwork) {
		this.mongoTemplate.save(deviceNetwork);
	}

	@Override
	public List<HomeExpendature> getDeviceByParentId(String device_parent_id)
	{
		final Query query = Query.query(Criteria.where("parent_id").is(device_parent_id));
		return mongoTemplate.find(query,HomeExpendature.class);
	}

	@Override
	public List<DeviceNetwork> getDeviceNetworsByNetworkId()
	{
		return mongoTemplate.findAll(DeviceNetwork.class);
	}

	@Override
	public boolean checkDeviceExists(String device_mac_id, String hardware_key) {
		final Query query = Query.query(Criteria.where("device_id").is(device_mac_id).andOperator(where("hardware_key").is(hardware_key)));
		return mongoTemplate.exists(query, HomeExpendature.class);
	}

	@Override
	public List<HomeExpendature> getItemByUserId(String userId) {
		final Query query = Query.query(Criteria.where("user_id").is(userId));
		return mongoTemplate.find(query,HomeExpendature.class);
	}

	@Override
	public DeviceNetwork getDeviceNetworkByNetworkId(String networkId) {
		final Query query = Query.query(Criteria.where("network_id").is(networkId));
		return mongoTemplate.findOne(query, DeviceNetwork.class);
	}
}

