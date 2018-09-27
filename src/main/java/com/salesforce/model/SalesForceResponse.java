package com.salesforce.model;


import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 
 * @author longphan
 * @param <T>
 *
 * @param <T>
 */
@Data
@Accessors(chain = true)
public class SalesForceResponse {
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("objectSchema")
	private LinkedHashMap<String, Object> objectSchema;
	
	@JsonProperty("records")
	private List<LinkedHashMap<String, Object>> records;
	
}
