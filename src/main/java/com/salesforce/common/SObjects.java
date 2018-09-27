package com.salesforce.common;

import java.util.Arrays;
import java.util.Collection;

import com.salesforce.model.SObjectField;
import com.salesforce.model.filter.SalesForceCrmFilter;
/**
 * 
 * @author longphan
 *
 */
public enum SObjects {

	ACCOUNT("Account"){
		@Override
		public String buildSelectQuery(SalesForceCrmFilter filter, Collection<SObjectField> fields) {
			buildQuery(fields, filter);
			return this.getQuery();
		}
	},
	CONTACT("Contact") {
		@Override
		public String buildSelectQuery(SalesForceCrmFilter filter, Collection<SObjectField> fields) {
			buildQuery(fields, filter);
			return this.getQuery();
		}
	},
	EVENT("Event") {
		@Override
		public String buildSelectQuery(SalesForceCrmFilter filter, Collection<SObjectField> fields) {
			buildQuery(fields, filter);
			return this.getQuery();
		}
	},
	LEAD("Lead") {
		@Override
		public String buildSelectQuery(SalesForceCrmFilter filter, Collection<SObjectField> fields) {
			buildQuery(fields, filter);
			return this.getQuery();
		}
	},
	OPPORTUNITY("Opportunity") {
		@Override
		public String buildSelectQuery(SalesForceCrmFilter filter, Collection<SObjectField> fields) {
			buildQuery(fields, filter);
			return this.getQuery();
		}
	},
	TASK("Task") {
		@Override
		public String buildSelectQuery(SalesForceCrmFilter filter, Collection<SObjectField> fields) {
		
			buildQuery(fields, filter);
			return this.getQuery();
		}
	}, 
	USER("User") {
		@Override
		public String buildSelectQuery(SalesForceCrmFilter filter, Collection<SObjectField> fields) {
			buildQuery(fields, filter);
			return this.getQuery();
		}
	}, 
	OPPORTUNITY_CONTACT_ROLE("OpportunityContactRole") {
		@Override
		public String buildSelectQuery(SalesForceCrmFilter filter, Collection<SObjectField> fields) {
			buildQuery(fields, filter);
			return this.getQuery();
		}
	}
	;
	
	private String value;
	private StringBuilder query;
	
	public String getValue() {
		return this.value;
	}
	protected String getQuery() {
		return this.query.toString();
	}
	
	private SObjects(String value) {
		this.value = value;
	}
	
	public abstract String buildSelectQuery(SalesForceCrmFilter filter, Collection<SObjectField> fields);
	
	/**
	 *
	 * Building Native query</br>
	 * Replace spaces with “+” characters in the query string to create a valid URI
	 * @see https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/resources_query.htm
	 * @param type
	 * @author longphan
	 * @return String
	 */
	
	protected void buildSelectClause(Collection<SObjectField> fields) {
		query = new StringBuilder();
		query.append("query?q=Select+");
		fields.stream().forEach(field -> query.append(field.getName() + ","));
	}
	
	protected void buildFromClause() {
		int length = query.length();
	    query.replace(length-1, length, "+");
		query.append("+FROM+" + this.value);
	}

	protected void buildRelationShipQuery(String... args) {
		Arrays.stream(args).forEach(item -> query.append(item + ","));
	}
	
	/**
	 * @author longphan
	 * @param type
	 * @param filter
	 * @return
	 */
	protected void buildQuery(Collection<SObjectField> fields, SalesForceCrmFilter filter) {
		buildSelectClause(fields);
		buildFromClause();
		query.append("+WHERE+");
		query.append(SalesForceConstant.FIELD_LAST_MODIFIED_DATE);
		String[] lastModifiedDate = filter.getLastModifiedDate();
		query.append("+>=+");
		query.append(lastModifiedDate[0]);
		query.append("+AND+");
		query.append(SalesForceConstant.FIELD_LAST_MODIFIED_DATE);
		query.append("+<=+");
		query.append(lastModifiedDate[1]);
	}
}
