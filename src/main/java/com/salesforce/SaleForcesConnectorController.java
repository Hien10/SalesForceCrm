package com.salesforce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.salesforce.common.SObjects;
import com.salesforce.model.SalesForceResponse;
import com.salesforce.model.authentication.BaseRequest;
import com.salesforce.service.SalesForceFacade;

/**
 * 
 * @author longphan
 *
 */

@RestController
@RequestMapping("/v1")
public class SaleForcesConnectorController {
	
	@Autowired
	private SalesForceFacade sfFacade;
	
	@RequestMapping(value = "/accounts", method = RequestMethod.POST)
	public SalesForceResponse getAccounts(@RequestBody BaseRequest request) {
		return sfFacade.getRecords(request, SObjects.ACCOUNT);
	}
	
	@RequestMapping(value = "/contacts", method = RequestMethod.POST)
	public SalesForceResponse getContacts(@RequestBody BaseRequest request) {
		return sfFacade.getRecords(request, SObjects.CONTACT);
	}
	
	@RequestMapping(value = "/events", method = RequestMethod.POST)
	public SalesForceResponse getEvents(@RequestBody BaseRequest request) {
		return sfFacade.getRecords(request, SObjects.EVENT);
	}
	
	@RequestMapping(value = "/leads", method = RequestMethod.POST)
	public SalesForceResponse getLeads(@RequestBody BaseRequest request) {
		return sfFacade.getRecords(request, SObjects.LEAD);
	}
	
	@RequestMapping(value = "/opportunities", method = RequestMethod.POST)
	public SalesForceResponse getOpportunities(@RequestBody BaseRequest request) {
		return sfFacade.getRecords(request, SObjects.OPPORTUNITY);
	}
	
	@RequestMapping(value = "/tasks", method = RequestMethod.POST)
	public SalesForceResponse getTasks(@RequestBody BaseRequest request) {
		return sfFacade.getRecords(request, SObjects.TASK);
	}
	
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public SalesForceResponse getUsers(@RequestBody BaseRequest request) {
		return sfFacade.getRecords(request, SObjects.USER);
	}
	
	@RequestMapping(value = "/opportunityContacts", method = RequestMethod.POST)
	public SalesForceResponse getOpportunityContact(@RequestBody BaseRequest request) {
		return sfFacade.getRecords(request, SObjects.OPPORTUNITY_CONTACT_ROLE);
	}
}
