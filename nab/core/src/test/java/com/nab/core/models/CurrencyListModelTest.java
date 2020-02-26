package com.nab.core.models;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.factory.ModelFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import com.google.common.collect.ImmutableMap;

import io.wcm.testing.mock.aem.junit.AemContext;

public class CurrencyListModelTest {

	@Rule
	public AemContext context = new AemContext();

	CurrencyListModel currencyListModel;

	@Mock
	ModelFactory modelFactory;

	@Before
	public void setUp() {
		context.addModelsForClasses(CurrencyListModel.class);
	}

	@Test
	public void test() {

		Map<String, String> item0 = new ImmutableMap.Builder<String, String>().put("desc", "LTC Details")
				.put("img", "/content/dam/nab/Litecoin Background.png").put("title", "LTC")
				.put("url", "/content/nab/url").put("URLTitle", "See More").build();

		Map<String, String> item1 = new ImmutableMap.Builder<String, String>().put("desc", "ETH Details")
				.put("img", "/content/dam/nab/Ethereum Background.jpg").put("title", "ETH")
				.put("url", "/content/nab/ethUrl").put("URLTitle", "See More").build();
		context.build().resource("/content/nab/home/jcr:content/nabCurrencyList").hierarchyMode()
				.resource("currencyList").siblingsMode().resource("item0", item0).resource("item1", item1);
		Resource res2 = context.currentResource("/content/nab/home/jcr:content/nabCurrencyList");
		currencyListModel = res2.adaptTo(CurrencyListModel.class);
		String imgPath = currencyListModel.getCurrencyList().getChild("item1").getValueMap().get("img", String.class);
		assertEquals(imgPath, "/content/dam/nab/Ethereum Background.jpg");

	}

}
