package com.nab.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@PrepareForTest({NABUtilities.class})
public class NABUtilitiesTest {
		
	@Test
	public void testFormatDate() {
		assertEquals("07 May 2019", NABUtilities.formatDate("20190507"));		
	}

	@Test
	public void testFormatDateParseException() {
		assertEquals(StringUtils.EMPTY, NABUtilities.formatDate("2019/05/07"));	
	}
	
	@Test
	public void testFormatTime() {
		assertEquals("07:15 AM", NABUtilities.formatTime(715));
	}
	
	@Test
	public void testFormatTime1() {
		assertEquals("22:00 PM", NABUtilities.formatTime(2200));
	}
	
	@Test
	public void testFormatTimeException() {
		assertEquals(StringUtils.EMPTY, NABUtilities.formatTime(07));
	}
}
