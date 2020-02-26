package com.nab.core;

import java.util.Map;

public class DigitalCurrency {
	
	private String currency;
	
    private String date;
	
	private Map<Integer,Float> pricePerTimeList;
    
    private transient float price;
    
    private transient int time;
    
    private String maxProfit;
    
    public DigitalCurrency() {
    	
    }
    /**
     * @param currency
     * @param date
     * @param time
     * @param price
     */
    public DigitalCurrency(String currency,String date,int time,float price) {
		this.currency = currency;
		this.date = date;
		this.price = price;
		this.time = time;
	}
	
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
	public String getDate() {
		return date;
	}

	/**
	 * @param date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return
	 */
	public float getPrice() {
		return price;
	}

	/**
	 * @param price
	 */
	public void setPrice(float price) {
		this.price = price;
	}

	/**
	 * @return
	 */
	public int getTime() {
		return time;
	}

	/**
	 * @param time
	 */
	public void setTime(int time) {
		this.time = time;
	}
	/**
	 * @return
	 */
	public Map<Integer,Float> getPricePerTimeList() {
		return pricePerTimeList;
	}
	/**
	 * @param pricePerTimeList
	 */
	public void setPricePerTimeList(Map<Integer,Float> pricePerTimeList) {
		this.pricePerTimeList = pricePerTimeList;
	}
	
	/**
	 * @return
	 */
	public String getMaxProfit() {
		return maxProfit;
	}
	/**
	 * @param maxProfit
	 */
	public void setMaxProfit(String maxProfit) {
		this.maxProfit = maxProfit;
	}
	
	
}
