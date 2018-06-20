package com.dmi.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dmi.exception.ValidationException;

/**
 * @author Ajay Negi
 */
public class ValidationHelper
{

	public static final String EMAIL = "email";
	public static final String NAME = "name";
	public static final String PASSWORD = "password";
	
	public static void validate(String property, String type)
			throws ValidationException
	{
		Pattern r = null;
		Matcher m = null;
		switch (type)
		{
		case EMAIL:
			String emailPattern = "^[\\w!#$%&�*+/=?`{|}~^-]+(?:\\.[\\w!#$%&�*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
			r = Pattern.compile(emailPattern);
			m = r.matcher(property);
			if (!m.find())
			{
				throw new ValidationException("[ " + property
						+ " ] validation failed for " + type + ".");
			}
			break;
		case NAME:
			String namePattern = "^[A-z0-9_-]{3,30}$";
			r = Pattern.compile(namePattern);
			m = r.matcher(property);
			if (!m.find())
			{
				throw new ValidationException(
						"UserName should be 3-15 characters in length and can only contain alphanumerics, hyphen(-) and underscore(_).");
			}
			break;
		case PASSWORD:
			String passwordPattern = "^(?=.*[A-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
			r = Pattern.compile(passwordPattern);
			m = r.matcher(property);
			if (!m.find())
			{
				throw new ValidationException(
						"Password must be atleast 8 characters long with atleast 1 alphabet, 1 numerical and 1 special character.");
			}
			break;
		default:
			break;
		}
	}
	/*
	 * public static void main(String[] args){ try{validate("123@G",PASSWORD);
	 * System.out.println("Success");} catch(Exception e){
	 * System.out.println("Failure"); e.printStackTrace(); } }
	 */

}
