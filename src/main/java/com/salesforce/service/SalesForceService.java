package com.salesforce.service;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.salesforce.common.Headers;
import com.salesforce.common.SalesForceConstant;
import com.salesforce.model.SObjectDescription;
import com.salesforce.model.SObjectResponse;
import com.salesforce.model.SalesForceResponse;
import com.salesforce.model.authentication.BaseRequest;
import com.salesforce.model.authentication.TokenResponse;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author longphan
 *
 */

@Getter
@Setter
@Service
@Slf4j
public class SalesForceService {

	@Value("${com.accent.ees.salesforce.grant.type}")
	private String grantType;
	@Value("${com.accent.ees.salesforce.oauth2.url}")
	private String salesforceOauth2Url;
	@Value("${com.accent.ees.salesforce.services.data.version}")
	private String servicesVersion;
    
	@Autowired
	private RestTemplate restTemplate;
	
	@Getter
	@Setter
	private String token;
	
	@Getter
	@Setter
	private String instanceUrl;
	
	private HttpHeaders headers;
	
	private ExecutorService executorService;
	/**
	 *  Fetch all data from SalesForceCRM
	 * @param type
	 * @param request {@link BaseRequest}
	 * @return
	 * @throws InterruptedException 
	 */
	public SalesForceResponse getRecords(BaseRequest request, String sObject) {
		HttpEntity<SObjectResponse> httpEntity = new HttpEntity<>(this.headers);
		String uri = buildURI(this.servicesVersion, sObject);
		ResponseEntity<SObjectResponse> postResponse = restTemplate.exchange(uri, HttpMethod.GET, httpEntity,
				new ParameterizedTypeReference<SObjectResponse>() {});
		if(!postResponse.getBody().isDone()) {
			String nextUrl = postResponse.getBody().getNextRecordsUrl();
			try {
				int totalSize = postResponse.getBody().getTotalSize();
				int limit = Integer.valueOf(nextUrl.split("-")[1]);
				int totalRequest = (totalSize % limit) == 0 ? (totalSize/ limit) - 1 : totalSize / limit;
				executorService = Executors.newFixedThreadPool(totalRequest);
				for (int i = 1; i <= totalRequest; i++) {
					String url = nextUrl.replaceAll(SalesForceConstant.SF_LIMIT_PATTERN, "-" + limit * i);
					executorService.submit(() -> getNextRecords(request, postResponse.getBody(), url));
				}
				executorService.shutdown();
				executorService.awaitTermination(1000L, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
			}
		}
		return toConnectorResponse(postResponse.getBody());
	}
	
	private void getNextRecords(BaseRequest request, SObjectResponse response, String url) {
		HttpEntity<SObjectResponse> httpEntity = new HttpEntity<>(this.headers);
		String uri = buildURI(url);
		ResponseEntity<SObjectResponse> postResponse = restTemplate.exchange(uri, HttpMethod.GET, httpEntity,
				new ParameterizedTypeReference<SObjectResponse>() {
				});
		response.setDone(postResponse.getBody().isDone());
		response.getRecords().addAll(postResponse.getBody().getRecords());
		response.setNextRecordsUrl(postResponse.getBody().getNextRecordsUrl());
	}
	/**
	 * 
	 * @param response
	 * @return SalesForceResponse
	 */
	private SalesForceResponse toConnectorResponse(SObjectResponse response) {
		SalesForceResponse resp = new SalesForceResponse();
		if(response.getRecords() != null && !response.getRecords().isEmpty()) {
			response.getRecords().stream().forEach(item -> item.remove("attributes"));
			resp.setRecords(response.getRecords());
		}
		return resp;
	}

	/**
	 *  Fetch all Object's field from SalesForceCRM
	 * @param type
	 * @param request {@link BaseRequest}
	 * @return
	 */
	public SObjectDescription getObjectDescription(BaseRequest request, String sObject) {
		initRequest(request);
		initRequestHeader();
		HttpEntity<String> httpEntity = new HttpEntity<>(this.headers);
		String uri = buildURI(this.servicesVersion, "/sobjects/" + sObject) + "/describe";
		ResponseEntity<SObjectDescription> postResponse = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, SObjectDescription.class);
		return postResponse.getBody();
	}
	/**
	 * 
	 * @param sObject
	 * @return
	 */
	private String buildURI(String...sObject) {
		return new StringBuilder(this.instanceUrl).append(Arrays.stream(sObject).collect(Collectors.joining("")))
				.toString();
	}
	/**
	 * 
	 * @param request {@link BaseRequest}
	 * @return
	 */
	private TokenResponse getToken(BaseRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> httpEntity = initHttpEntity(initTokenHeaders(request), headers);
		return restTemplate.postForEntity(salesforceOauth2Url, httpEntity, TokenResponse.class).getBody();
	}
	
	/**
	 * @author longphan
	 * @param request
	 */
	private void initRequest(BaseRequest request) {
		TokenResponse res = getToken(request);
		this.setToken(res.getAccessToken());
		this.setInstanceUrl(res.getInstanceUrl());
	}
	
	/**
	 * 
	 * @return
	 */
	private void initRequestHeader() {
		this.headers = new HttpHeaders();
		this.headers.setContentType(MediaType.APPLICATION_JSON);
		this.headers.set(Headers.AUTHORIZATION.getValue(), Headers.BEARER.getValue() + " " + this.token);
	}
	/**
	 * @author longphan
	 * @param request
	 * @return
	 */
	private MultiValueMap<String, String> initTokenHeaders(BaseRequest request) {
		MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
		params.add(Headers.USERNAME.getValue(), request.getUsername());
		params.add(Headers.PASSWORD.getValue(), request.getPassword());
		params.add(Headers.CLIENT_SECRET.getValue(), request.getClientSecret());
		params.add(Headers.CLIENT_ID.getValue(), request.getClientId());
		params.add(Headers.GRANT_TYPE.getValue(), grantType);
		return params;
	}
	
	/**
	 * 
	 * @param params
	 * @param headers
	 * @return
	 */
	private HttpEntity<MultiValueMap<String, String>> initHttpEntity(MultiValueMap<String, String> params, HttpHeaders headers){
		return new HttpEntity<MultiValueMap<String, String>>(params, headers); 
	}
}
