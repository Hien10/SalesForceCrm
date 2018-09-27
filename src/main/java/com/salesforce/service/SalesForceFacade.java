package com.salesforce.service;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesforce.common.SObjects;
import com.salesforce.model.SObjectDescription;
import com.salesforce.model.SObjectField;
import com.salesforce.model.SalesForceResponse;
import com.salesforce.model.authentication.BaseRequest;
import com.salesforce.model.filter.SalesForceCrmFilter;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author longphan
 *
 */
@Service
@Slf4j
public class SalesForceFacade {
	
	@Autowired
	private SalesForceService service;
	
	/**
	 * @author longphan
	 * @param request
	 * @return
	 * @throws InterruptedException 
	 */
	public SalesForceResponse getRecords(BaseRequest request, SObjects sObject) {
		SObjectDescription fields = service.getObjectDescription(request, sObject.getValue());
		SalesForceResponse response = service.getRecords(
				request, sObject.buildSelectQuery(createDateModifiedFilter(request), fields.getFields()));
		response.setType(sObject.getValue());
		response.setObjectSchema(fields.getFields().parallelStream().collect(
				Collectors.toMap(
						SObjectField::getName, SObjectField::getType,
						(v1, v2) -> v1,
						LinkedHashMap::new)
				));
		log.info("Total {} Records: {} with user {}.", sObject.getValue(), response.getRecords().size(), request.getUsername());
		return response;
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	private SalesForceCrmFilter createDateModifiedFilter(BaseRequest request) {
		SalesForceCrmFilter filter = new SalesForceCrmFilter();
		if ((request.getStartDate() == null) && (request.getEndDate() == null))
			filter.setLastModifiedDate(null);
		else {
			String startDate = (request.getStartDate() != null) ? request.getStartDate().toString() : "";
			String endDate = (request.getEndDate() != null) ? request.getEndDate().toString()
					: Instant.now().toString();
			String[] dateRange = new String[2];
			dateRange[0] = startDate;
			dateRange[1] = endDate;
			filter.setLastModifiedDate(dateRange);
		}
		return filter;
	}

}
