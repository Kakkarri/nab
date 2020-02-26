package com.nab.core.models;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.nab.core.DigitalCurrency;

@Model(adaptables = Resource.class)
public class DigitalCurrencyModel {

	private static final Logger LOG = LoggerFactory.getLogger(DigitalCurrencyModel.class);
	
	@Inject
	@Default(values = "BTC")
	private String selectedCurrency;

	@SlingObject
	ResourceResolver resourceResolver;

	private DigitalCurrency currencyDetails;
	
	private static final String CURRENCY_DETAIL_PATH = "/var/nab/digitalCurrency/details";
	// fetch currency details	
	@PostConstruct
	void init() {
		Optional<Node> node = Optional.ofNullable(resourceResolver.getResource(CURRENCY_DETAIL_PATH))
										.map(resource -> resource.adaptTo(Node.class));								
										
		node.ifPresent(n -> {
			Gson gson = new Gson();			
			try {
				currencyDetails = gson.fromJson(n.getProperty(selectedCurrency).getString(), DigitalCurrency.class);
				} catch (JsonSyntaxException | RepositoryException e) {
				LOG.error("ERROR :: " + e);
			}
		});
	}

	/**
	 * @return
	 */
	public DigitalCurrency getCurrencyDetail() {
		return currencyDetails;
	}

}
