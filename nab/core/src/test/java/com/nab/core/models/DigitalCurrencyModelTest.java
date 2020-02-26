package com.nab.core.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import io.wcm.testing.mock.aem.junit.AemContext;

public class DigitalCurrencyModelTest {

	@Rule
	public AemContext aemContext = new AemContext(ResourceResolverType.JCR_MOCK);

	public static final String ETH_DETAILS = "{\"currency\":\"ETH\",\"date\":\"07 May 2018\",\"pricePerTimeList\":{\"900\":1.45,\"1030\":1.87,\"1245\":2.55,\"1515\":1.01,\"1700\":3.15}}";

	Resource nabCurrencyDetail;

	DigitalCurrencyModel dcModel;

	@Before
	public void setUp() {
		aemContext.create().resource("/content/nab/home/jcr:content/par/nabcurrencydetail",
				ImmutableMap.<String, Object>builder().put("selectedCurrency", "ETH").build());

		nabCurrencyDetail = aemContext.create().resource("/var/nab/digitalCurrency/details",
				ImmutableMap.<String, Object>builder().put("ETH", ETH_DETAILS).build());

		aemContext.addModelsForClasses(DigitalCurrencyModel.class);
	}

	@Test
	public void testModel() {
		dcModel = aemContext.resourceResolver().getResource("/content/nab/home/jcr:content/par/nabcurrencydetail")
				.adaptTo(DigitalCurrencyModel.class);
		assertEquals("ETH", dcModel.getCurrencyDetail().getCurrency());		
	}

	@Test
	public void testResourceIsNotPresent() throws PersistenceException  {
		aemContext.resourceResolver().delete(aemContext.currentResource("/var/nab/digitalCurrency/details"));
		dcModel = aemContext.resourceResolver().getResource("/content/nab/home/jcr:content/par/nabcurrencydetail")
				.adaptTo(DigitalCurrencyModel.class);
		assertNull(dcModel.getCurrencyDetail());
	}

	@Test
	public void testRepositoryException() throws PersistenceException {
		aemContext.resourceResolver().delete(aemContext.currentResource("/var/nab/digitalCurrency/details"));		
		aemContext.create().resource("/var/nab/digitalCurrency/details");
		aemContext.resourceResolver().getResource("/content/nab/home/jcr:content/par/nabcurrencydetail")
				.adaptTo(DigitalCurrencyModel.class);

	}

}
