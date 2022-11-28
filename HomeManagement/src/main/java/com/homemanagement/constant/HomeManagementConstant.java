package com.homemanagement.constant;

public enum HomeManagementConstant{
	TO_USER_NAME("TO_USER_NAME"),FROM_USER_NAME("FROM_USER_NAME"),ACCEPT_INVITATION_LINK("ACCEPT_INVITATION_LINK"),
	CONFIRM_REGISTRATION_LINK("CONFIRM_REGISTRATION_LINK"),RESET_MY_PASSWORD("RESET_MY_PASSWORD"),ACTIVE("Active"),
	ACTIVE_PENDING("Activation Pending"), MONGO_URL("mongodb+srv://Homemanage:Homemanage5259@cluster0-woxyj.mongodb.net"), PORTAL(""), ADMIN("Administrator"), ANONYMOUS_USER("anonymoususer"), USERID("user_id");
	private String constantKey;
	private HomeManagementConstant(String constantKey) { 
		this.constantKey = constantKey; 
	}
	public String getKey(){ 
		return constantKey; 
	}
}
