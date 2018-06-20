package com.dmi.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dmi.constant.Constants;

public class DateTimeUtils
{
	private DateTimeUtils()
	{

	}

	// Common Time Format is mentioned in
	// com.dmi.constant.Constants.DATEFORMAT
	public static String convertToCommonTimeFormat(Date date)
	{
		DateFormat dateFormat = new SimpleDateFormat(Constants.DATEFORMAT);
		return dateFormat.format(date);
	}
}
