package com.dmi.utils;

import java.util.Random;

/**
 * 
 * @author Mukul Bansal
 */
public class RandomNumberUtils
{
	public static Float getFloat(Float min, Float max)
	{
		Random random = new Random();
		return random.nextFloat() * (max - min) + min;
	}
}
