package com.homemanagement.domain;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Home_Expendature")
public class HomeExpendature {

	private String id;
	private String user_id;
	private String item_name;
	private String item_id;
	private String item_type;
	private String item_price;
	private String item_purchase_date;
	private String item_status;


	@PersistenceConstructor
	public HomeExpendature(String id,String user_id, String item_name, String item_id, String item_type, String item_price,
			String item_purchase_date, String item_status) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.item_name = item_name;
		this.item_id = item_id;
		this.item_type = item_type;
		this.item_price = item_price;
		this.item_purchase_date = item_purchase_date;
		this.item_status = item_status;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getUser_id() {
		return user_id;
	}


	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}


	public String getItem_name() {
		return item_name;
	}


	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}


	public String getItem_id() {
		return item_id;
	}


	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}


	public String getItem_type() {
		return item_type;
	}


	public void setItem_type(String item_type) {
		this.item_type = item_type;
	}


	public String getItem_price() {
		return item_price;
	}


	public void setItem_price(String item_price) {
		this.item_price = item_price;
	}


	public String getItem_purchase_date() {
		return item_purchase_date;
	}


	public void setItem_purchase_date(String item_purchase_date) {
		this.item_purchase_date = item_purchase_date;
	}


	public String getItem_status() {
		return item_status;
	}


	public void setItem_status(String item_status) {
		this.item_status = item_status;
	}




	
}
