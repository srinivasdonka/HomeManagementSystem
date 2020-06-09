package com.homemanagement.domain;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Device_Network")
public class DeviceNetwork {

	private String id;
	private String network_id;	
	private String admin_username;
	private String admin_password;
	private String admin_confirmpassword;
	private String network_username;
	private String network_password;
	private String network_confirmpassword;
	private String country;
	private boolean wireless_router;
	private boolean wireless_router_ap;
	private boolean access_srm;
	private boolean privacy_policy;
	private String user_id;

	public DeviceNetwork() {
		// TODO Auto-generated constructor stub
	}

	@PersistenceConstructor
	public DeviceNetwork(String id,String network_id, String admin_username, String admin_password, String admin_confirmpassword,
			String network_username, String network_password, String network_confirmpassword, String country,
			boolean wireless_router, boolean wireless_router_ap, boolean access_srm, boolean privacy_policy, String user_id) {
		super();
		this.id=id;
		this.network_id = network_id;
		this.admin_username = admin_username;
		this.admin_password = admin_password;
		this.admin_confirmpassword = admin_confirmpassword;
		this.network_username = network_username;
		this.network_password = network_password;
		this.network_confirmpassword = network_confirmpassword;
		this.country = country;
		this.wireless_router = wireless_router;
		this.wireless_router_ap = wireless_router_ap;
		this.access_srm = access_srm;
		this.privacy_policy = privacy_policy;
		this.user_id=user_id;
	}
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNetwork_id() {
		return network_id;
	}

	public void setNetwork_id(String network_id) {
		this.network_id = network_id;
	}

	public String getAdmin_username() {
		return admin_username;
	}

	public void setAdmin_username(String admin_username) {
		this.admin_username = admin_username;
	}

	public String getAdmin_password() {
		return admin_password;
	}

	public void setAdmin_password(String admin_password) {
		this.admin_password = admin_password;
	}

	public String getAdmin_confirmpassword() {
		return admin_confirmpassword;
	}

	public void setAdmin_confirmpassword(String admin_confirmpassword) {
		this.admin_confirmpassword = admin_confirmpassword;
	}

	public String getNetwork_username() {
		return network_username;
	}

	public void setNetwork_username(String network_username) {
		this.network_username = network_username;
	}

	public String getNetwork_password() {
		return network_password;
	}

	public void setNetwork_password(String network_password) {
		this.network_password = network_password;
	}

	public String getNetwork_confirmpassword() {
		return network_confirmpassword;
	}

	public void setNetwork_confirmpassword(String network_confirmpassword) {
		this.network_confirmpassword = network_confirmpassword;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public boolean isWireless_router() {
		return wireless_router;
	}

	public void setWireless_router(boolean wireless_router) {
		this.wireless_router = wireless_router;
	}

	public boolean isWireless_router_ap() {
		return wireless_router_ap;
	}

	public void setWireless_router_ap(boolean wireless_router_ap) {
		this.wireless_router_ap = wireless_router_ap;
	}

	public boolean isAccess_srm() {
		return access_srm;
	}

	public void setAccess_srm(boolean access_srm) {
		this.access_srm = access_srm;
	}

	public boolean isPrivacy_policy() {
		return privacy_policy;
	}

	public void setPrivacy_policy(boolean privacy_policy) {
		this.privacy_policy = privacy_policy;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

}
