package com.nab.core.models;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

@Model(adaptables=Resource.class)
public class CurrencyListModel {	
	
	@Inject
	@Optional
	public Resource currencyList;

	/**
	 * @return list of currency resources
	 */
	public Resource getCurrencyList() {
		return currencyList;
	}

}
