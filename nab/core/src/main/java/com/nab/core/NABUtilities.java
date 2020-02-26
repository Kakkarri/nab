package com.nab.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nab.workflow.DigitalCurrencyWorkFlowModel;

public class NABUtilities {

	private static final Logger LOG = LoggerFactory.getLogger(DigitalCurrencyWorkFlowModel.class);
	/* formatTime Method used to format the time to the HH:mm aa format */
	/**
	 * @param time
	 * @return
	 */
	public static String formatTime(int time) {
		String formattedTime = StringUtils.EMPTY;
		String t = String.valueOf(time);
		if (t.length() == 3)
			t = '0' + t;
		SimpleDateFormat rawformat = new SimpleDateFormat("HHmm");
		SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm aa");
		try {
			Date date = rawformat.parse(t);
			formattedTime = timeformat.format(date);

		} catch (ParseException e) {
			LOG.error("Date Parsing error" + e);
		}
		return formattedTime;
	}
	/* formatDate Method used to format the date as dd MMMM YYYY */
	/**
	 * @param date
	 * @return
	 */
	public static String formatDate(String date) {	
		String formattedDate = StringUtils.EMPTY;
		try {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat csvFormat = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat showDateFormat = new SimpleDateFormat("dd MMMM YYYY");
			cal.setTime(csvFormat.parse(date));
			formattedDate = showDateFormat.format(cal.getTime());
		} catch (ParseException e) {
			LOG.error("Parse Exception :" + e);
		}
		return formattedDate;
	}
}
