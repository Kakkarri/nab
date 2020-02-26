package com.nab.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.nab.core.DigitalCurrencyProfit;

@Model(adaptables = Resource.class)
public class CurrencyProfitModel {
	private static final Logger LOG = LoggerFactory.getLogger(CurrencyProfitModel.class);

	@SlingObject
	ResourceResolver resourceResolver;

	private List<DigitalCurrencyProfit> profit;

	private static final String ETH_PROFIT = "ETHProfit";

	private static final String LTC_PROFIT = "LTCProfit";

	private static final String BTC_PROFIT = "BTCProfit";
	
	private static final String CURRENCY_DETAIL_PATH = "/var/nab/digitalCurrency/details";

	@PostConstruct
	void init() {
		Optional<Node> node = Optional.ofNullable(resourceResolver.getResource(CURRENCY_DETAIL_PATH))
				.map(resource -> resource.adaptTo(Node.class));	
		// adding profit detail for each currency
		
		node.ifPresent(n -> {
			Gson gson = new Gson();
			profit = new ArrayList<DigitalCurrencyProfit>();
			try {
				profit.add(gson.fromJson(n.getProperty(BTC_PROFIT).getString(), DigitalCurrencyProfit.class));
				profit.add(gson.fromJson(n.getProperty(LTC_PROFIT).getString(), DigitalCurrencyProfit.class));
				profit.add(gson.fromJson(n.getProperty(ETH_PROFIT).getString(), DigitalCurrencyProfit.class));
			} catch (JsonSyntaxException | RepositoryException e) {
			LOG.error("ERROR :: " + e);
			}
		});
	}
	
	/**
	 * @return
	 */
	public List<DigitalCurrencyProfit> getProfit() {
		return profit;
	}
}	
