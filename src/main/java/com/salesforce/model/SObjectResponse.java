package com.salesforce.model;


import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 
 * @author longphan
 * @param <T>
 *
 * @param <T>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SObjectResponse {
	
	@JsonProperty
	private int totalSize;
	
	@JsonProperty("done")
	private boolean done;
	
	@JsonProperty("nextRecordsUrl")
	private String nextRecordsUrl;
	
	@JsonProperty("records")
	private List<LinkedHashMap<String, Object>> records;
	
}
