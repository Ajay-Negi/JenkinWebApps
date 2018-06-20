package com.dmi.utils;

import java.time.Instant;

/**
 * 
 * @author Mukul Bansal
 */

public class DateUtils
{
	public static Long getEpochTimeNow()
	{
		return Instant.now().toEpochMilli();
	}

}
