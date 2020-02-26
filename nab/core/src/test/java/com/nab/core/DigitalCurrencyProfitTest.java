package com.nab.core;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class DigitalCurrencyProfitTest {
	
	private DigitalCurrencyProfit dcProfit;

	@Before
	public void setup() {
		dcProfit = new DigitalCurrencyProfit();
		dcProfit.setCurrency("LTC");
		dcProfit.setMaxPrice(23.44f);
		dcProfit.setMinPrice(12.33f);
		dcProfit.setMaxTime("1500");
		dcProfit.setMinTime("0905");
		dcProfit.setProfit("11.11");
		
	}
	
	@Test
	public void test() {
		assertEquals("LTC", dcProfit.getCurrency());
		assertEquals(23.44f, dcProfit.getMaxPrice(),0);
		assertEquals(12.33f, dcProfit.getMinPrice(),0);
		assertEquals("1500", dcProfit.getMaxTime());
		assertEquals("0905", dcProfit.getMinTime());
		assertEquals("11.11", dcProfit.getProfit());
	}
}
