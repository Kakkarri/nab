package com.nab.core;

public class DigitalCurrencyProfit {
	
	private String currency;
	
	private float minPrice;
	
	private float maxPrice;
	
	private String minTime;
	
	private String maxTime;
	
	private String profit;
	
	/**
	 * @return
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return
	 */
	public float getMinPrice() {
		return minPrice;
	}

	/**
	 * @return
	 */
	public float getMaxPrice() {
		return maxPrice;
	}

	/**
	 * @return
	 */
	public String getMinTime() {
		return minTime;
	}

	/**
	 * @return
	 */
	public String getMaxTime() {
		return maxTime;
	}

	/**
	 * @return
	 */
	public String getProfit() {
		return profit;
	}

	/**
	 * @param minPrice
	 */
	public void setMinPrice(float minPrice) {
		this.minPrice = minPrice;
	}

	/**
	 * @param maxPrice
	 */
	public void setMaxPrice(float maxPrice) {
		this.maxPrice = maxPrice;
	}

	/**
	 * @param minTime
	 */
	public void setMinTime(String minTime) {
		this.minTime = minTime;
	}

	/**
	 * @param maxTime
	 */
	public void setMaxTime(String maxTime) {
		this.maxTime = maxTime;
	}

	/**
	 * @param profit
	 */
	public void setProfit(String profit) {
		this.profit = profit;
	}
	
	
	

}
