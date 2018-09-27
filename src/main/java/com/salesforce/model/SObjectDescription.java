package com.salesforce.model;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 
 * @author longphan
 *
 * @param <T>
 */
@Data
public class SObjectDescription {
	@JsonProperty("fields")
	private Collection<SObjectField> fields;
}
