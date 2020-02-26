package com.nab.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class DigitalCurrencyTest {
	
	private DigitalCurrency digitalCurrency;
	
	private Map<Integer,Float> pricePerTimeList = new HashMap<Integer,Float>();
	
	@Before
	public void setUp() {
		digitalCurrency = new DigitalCurrency();
		digitalCurrency.setCurrency("BTC");
		digitalCurrency.setDate("07062019");
		digitalCurrency.setMaxProfit("35.77");
		digitalCurrency.setPrice(12.00f);
		digitalCurrency.setTime(1400);
		pricePerTimeList.put(1400,23.16f);
		pricePerTimeList.put(1600,27.50f);
		digitalCurrency.setPricePerTimeList(pricePerTimeList);	
	}
	
	@Test
	public void testDigitalCurrency() {
		assertEquals("BTC", digitalCurrency.getCurrency());
		assertEquals(1400,digitalCurrency.getTime());
		assertEquals("07062019", digitalCurrency.getDate());
		assertEquals(2,digitalCurrency.getPricePerTimeList().size());
		assertEquals("35.77",digitalCurrency.getMaxProfit());
	}

}
