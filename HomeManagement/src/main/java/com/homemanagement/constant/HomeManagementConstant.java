package com.homemanagement.constant;

public enum HomeManagementConstant{

	TO_USER_NAME("TO_USER_NAME"),FROM_USER_NAME("FROM_USER_NAME"),ACCEPT_INVITATION_LINK("ACCEPT_INVITATION_LINK"),
	CONFIRM_REGISTRATION_LINK("CONFIRM_REGISTRATION_LINK"),RESET_MY_PASSWORD("RESET_MY_PASSWORD"),ACTIVE("Active"),
	ACTIVE_PENDING("Activation Pending"); 

	private String constantKey; 

	private HomeManagementConstant(String constantKey) { 
		this.constantKey = constantKey; 
	} 

	public String getKey(){ 
		return constantKey; 
	} 


	/*
	 * TO_USER_NAME{
	 * 
	 * @Override public String asUpperCase() { return
	 * TO_USER_NAME.toString().toUpperCase(); } }, FROM_USER_NAME{
	 * 
	 * @Override public String asUpperCase() { return
	 * FROM_USER_NAME.toString().toUpperCase(); } }, ACCEPT_INVITATION_LINK{
	 * 
	 * @Override public String asUpperCase() { return
	 * ACCEPT_INVITATION_LINK.toString().toUpperCase(); } },
	 * 
	 * CONFIRM_REGISTRATION_LINK{
	 * 
	 * @Override public String asUpperCase() { return
	 * CONFIRM_REGISTRATION_LINK.toString().toUpperCase(); } },
	 * 
	 * RESET_MY_PASSWORD{
	 * 
	 * @Override public String asUpperCase() { return
	 * RESET_MY_PASSWORD.toString().toUpperCase(); } };
	 * 
	 * 
	 * 
	 * public abstract String asUpperCase();
	 */
}
