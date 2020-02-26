package com.nab.workflow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.dam.api.Asset;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nab.core.DigitalCurrency;
import com.nab.core.DigitalCurrencyProfit;
import com.nab.core.NABUtilities;

/**
 * @author 444749
 *
 */
@Component(service = WorkflowProcess.class, property = { "process.label= NAB Digital Currency WF Model" })

public class DigitalCurrencyWorkFlowModel implements WorkflowProcess {

	private static final Logger LOG = LoggerFactory.getLogger(DigitalCurrencyWorkFlowModel.class);

	@Reference
	ResourceResolverFactory resolverFactory;

	private static final String DATE = "Date";

	private static final String TIME = "Time";

	private static final String PRICE = "Price";

	private static final String CURRENCY = "Currency";

	private List<DigitalCurrency> currencyList = new LinkedList<DigitalCurrency>();

	Map<String, Object> param = new HashMap<String, Object>();

	private static final String CURRENCY_DETAIL_PATH = "/var/nab/digitalCurrency/details";
	
	NumberFormat formatter = new DecimalFormat("#0.00");

	private String maxCurrencyProfit;

	Gson gson;

	Node node;
	Session session;

	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
			throws WorkflowException {

		LOG.debug("workflow executed");

		param.put(ResourceResolverFactory.SUBSERVICE, "getResourceResolver");
		param.put(ResourceResolverFactory.USER, "datawriteuser");

		final WorkflowData workflowData = workItem.getWorkflowData();

		if (!workflowData.getPayloadType().equals("JCR_PATH")) {
			return;
		}

		final String digitalCurrencyCSVPath = workItem.getWorkflowData().getPayload().toString();	

		LOG.debug("Payload path :: {}", digitalCurrencyCSVPath);

		try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(param)) {
			session = resolver.adaptTo(Session.class);
			Resource resource = resolver.getResource(digitalCurrencyCSVPath);		
			Asset asset = resource.adaptTo(Asset.class);			
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(asset.getOriginal().getStream()));
			CSVParser csvParser = CSVFormat.EXCEL.withHeader().parse(bufferedReader);

			csvParser.forEach(r -> {
				currencyList.add(new DigitalCurrency(r.get(CURRENCY), NABUtilities.formatDate(r.get(DATE)),
						Integer.parseInt(r.get(TIME)), Float.parseFloat(r.get(PRICE))));

			});

			Map<String, List<DigitalCurrency>> list = currencyList.stream()
					.collect(Collectors.groupingBy(DigitalCurrency::getCurrency));

			list.forEach((s, dcList) -> {
				createCurrencyDetailsNodes(s, createJSONForDigitalCurrency(s, dcList), resolver);
			});

		} catch (LoginException e) {
			LOG.error("Login Exception" + e);
		} catch (IOException e) {
			LOG.error("IO Exception" + e);
		}catch (Exception e) {
			LOG.error("Exception" + e);
		}
	}
	/* createJSONForDigitalCurrency method creates json string for digital currency and calls maxTradeProfit Method
	 *  to calculates maxCurrencyProfit */
	/**
	 * @param currency
	 * @param digitalCurrencyList
	 * @return
	 */
	private String createJSONForDigitalCurrency(String currency, List<DigitalCurrency> digitalCurrencyList) {
		DigitalCurrency c = new DigitalCurrency();
		Map<Integer, Float> priceTimeMap = new TreeMap<Integer, Float>();

		for (DigitalCurrency dc : digitalCurrencyList) {
			c.setDate(dc.getDate());
			priceTimeMap.put(dc.getTime(), dc.getPrice());
			LOG.debug("dc - " + dc.getCurrency() + "-" + dc.getDate() + "-" + dc.getTime() + "-" + dc.getPrice());
		}

		c.setCurrency(currency);
		c.setPricePerTimeList(priceTimeMap);
		gson = new GsonBuilder().create();
		String str = gson.toJson(c);
		maxCurrencyProfit = gson.toJson(maxTradeProfit(priceTimeMap, currency));
		return str;
	}
	/* createCurrencyDetailsNodes Method : creates path and add properties to store the respective currency details */	 
	/**
	 * @param currency
	 * @param currencyDetail
	 * @param resolver
	 */
	private void createCurrencyDetailsNodes(String currency, String currencyDetail, ResourceResolver resolver) {
		try {
			if (!Optional.ofNullable(resolver.getResource(CURRENCY_DETAIL_PATH)).isPresent())
				JcrUtil.createPath(CURRENCY_DETAIL_PATH, "nt:unstructured", session);
			node = resolver.getResource(CURRENCY_DETAIL_PATH).adaptTo(Node.class);
			node.setProperty(currency, currencyDetail);
			LOG.debug("node :: profit" + maxCurrencyProfit);
			node.setProperty(currency.concat("Profit"), maxCurrencyProfit.toString());
			LOG.debug("node :: " + node.getPath());		
			session.save();
		} catch (RepositoryException e) {
			LOG.error("Repository Exception" + e);
		}

	}

	/* maxTradeProfit Method : Calculates Profit for a currency */
	/**
	 * @param priceTimeMap
	 * @param currency
	 * @return
	 */
	private DigitalCurrencyProfit maxTradeProfit(Map<Integer, Float> priceTimeMap, String currency) {
		DigitalCurrencyProfit currencyProfit = new DigitalCurrencyProfit();
		float maxPrice = Collections.max(priceTimeMap.values());
		int maxTime = priceTimeMap.keySet().stream().filter(key -> maxPrice == priceTimeMap.get(key)).findFirst().get();

		priceTimeMap.entrySet().removeIf(r -> r.getKey() > maxTime);
		float minPrice = Collections.min(priceTimeMap.values());
		int minTime = priceTimeMap.keySet().stream().filter(key -> minPrice == priceTimeMap.get(key)).findFirst().get();

		LOG.debug("Min time ::" + minTime + " ::" + minPrice);
		LOG.debug("Maxtime ::" + maxTime + " ::" + maxPrice);
		
		currencyProfit.setCurrency(currency);
		currencyProfit.setMinPrice(minPrice);
		currencyProfit.setMinTime(NABUtilities.formatTime(minTime));
		currencyProfit.setMaxPrice(maxPrice);
		currencyProfit.setMaxTime(NABUtilities.formatTime(maxTime));
		currencyProfit.setProfit(formatter.format(maxPrice - minPrice));
		
		return currencyProfit;

	}

}
