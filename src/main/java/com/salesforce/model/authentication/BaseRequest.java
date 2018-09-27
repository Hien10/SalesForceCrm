package com.salesforce.model.authentication;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BaseRequest {
	
    private String clientId;

    private String clientSecret;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;
    
    @JsonProperty("startDate")
    private Instant startDate;
	
    @JsonProperty("endDate")
    private Instant endDate;
}
