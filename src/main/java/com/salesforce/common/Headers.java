package com.salesforce.common;
/**
 * 
 * @author longphan
 *
 */
public enum Headers {
	GRANT_TYPE("grant_type"),
	CLIENT_ID("client_id"),
	CLIENT_SECRET("client_secret"),
	USERNAME("username"),
	PASSWORD("password"),
	AUTHORIZATION("Authorization"),
	BEARER("Bearer");
	private final String value;
	
	private Headers(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}