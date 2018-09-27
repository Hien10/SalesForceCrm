package com.salesforce.model.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SalesForceCrmFilter {

	@JsonProperty("lastModifiedDate")
    private String[] lastModifiedDate;
}
