package com.salesforce.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 
 * @author longphan
 *
 * @param <T>
 */
@Data
public class SObjectField {
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("type")
	private String type;
}
