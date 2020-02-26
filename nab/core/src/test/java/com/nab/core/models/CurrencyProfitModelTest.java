package com.nab.core.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import io.wcm.testing.mock.aem.junit.AemContext;

public class CurrencyProfitModelTest {

	@Rule
	public AemContext aemContext = new AemContext(ResourceResolverType.JCR_MOCK);

	public static final String ETHPROFIT = "{\"currency\":\"ETH\",\"minPrice\":1.01,\"maxPrice\":3.15,\"minTime\":\"15:15 PM\",\"maxTime\":\"17:00 PM\",\"profit\":2.14}";

	public static final String BTCPROFIT = "{\"currency\":\"BTC\",\"minPrice\":36.13,\"maxPrice\":39.98,\"minTime\":\"10:45 AM\",\"maxTime\":\"14:00 PM\",\"profit\":3.8499985}";

	public static final String LTCPROFIT = "{\"currency\":\"LTC\",\"minPrice\":11.87,\"maxPrice\":15.03,\"minTime\":\"11:15 AM\",\"maxTime\":\"12:45 PM\",\"profit\":3.1599998}";

	Resource nabCurrencyDetail;

	CurrencyProfitModel dcModel;
	

	@Before
	public void setUp() {
		aemContext.create().resource("/content/nab/home/jcr:content/par/nabcurrencydetail",
				ImmutableMap.<String, Object>builder().put("selectedCurrency", "ETH").build());

		nabCurrencyDetail = aemContext.create().resource("/var/nab/digitalCurrency/details",
				ImmutableMap.<String, Object>builder().put("ETHProfit", ETHPROFIT)
						.put("BTCProfit", BTCPROFIT).put("LTCProfit", LTCPROFIT).build());

		aemContext.addModelsForClasses(CurrencyProfitModel.class);
	}
	

	@Test
	public void testModel() {
		dcModel = aemContext.resourceResolver().getResource("/content/nab/home/jcr:content/par/nabcurrencydetail")
				.adaptTo(CurrencyProfitModel.class);		
		assertEquals("17:00 PM", dcModel.getProfit().get(2).getMaxTime());
		assertNotNull(dcModel.getProfit().get(0));
	}
	

	@Test
	public void testResourceIsNotPresent() throws PersistenceException {
		aemContext.resourceResolver().delete(aemContext.currentResource("/var/nab/digitalCurrency/details"));
		dcModel = aemContext.resourceResolver().getResource("/content/nab/home/jcr:content/par/nabcurrencydetail")
				.adaptTo(CurrencyProfitModel.class);
		assertNull(dcModel.getProfit());
	}

	@Test
	public void testRepositoryException() throws PersistenceException {
		aemContext.resourceResolver().delete(aemContext.currentResource("/var/nab/digitalCurrency/details"));
		aemContext.create().resource("/var/nab/digitalCurrency/details", ImmutableMap.<String, Object>builder()
				.put("ETHProfit", ETHPROFIT).put("LTCProfit", LTCPROFIT).build());
		aemContext.resourceResolver().getResource("/content/nab/home/jcr:content/par/nabcurrencydetail")
				.adaptTo(CurrencyProfitModel.class);

	}



}
