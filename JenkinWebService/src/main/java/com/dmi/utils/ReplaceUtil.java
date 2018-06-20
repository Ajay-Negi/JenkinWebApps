package com.dmi.utils;

/**
 * 
 * @author ANegi
 *
 */
public class ReplaceUtil {
	
	public static String replaceString(String input)
	{
		  
		String replacedString = input.replace("&gt;",">");
		replacedString = replacedString.replace("&lt;","<");
		replacedString = replacedString.replace("<p>[#ftl]</p>","[#ftl]");
		replacedString = replacedString.replace("src=\"../images","src=\"http://129.191.21.218:8080/images");
		  
		System.out.println("REPLACE TEMPLATE STRING:::" + replacedString);
		
		return replacedString;
	}

}
