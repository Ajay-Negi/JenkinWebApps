package com.dmi.dto.oemmodel.rawmessage;

import java.io.Serializable;
import java.util.ArrayList;

public class ArithExpDTO implements Serializable
{

	private static final long serialVersionUID = 1L;

	private String expression;
	private String expName;
	private String expType;
	private ArrayList<String> featureList;

	public String getExpression()
	{
		return expression;
	}

	public void setExpression(String expression)
	{
		this.expression = expression;
	}

	public String getExpName()
	{
		return expName;
	}

	public void setExpName(String expName)
	{
		this.expName = expName;
	}

	public String getExpType()
	{
		return expType;
	}

	public void setExpType(String expType)
	{
		this.expType = expType;
	}

	public ArrayList<String> getFeatureList()
	{
		return featureList;
	}

	public void setFeatureList(ArrayList<String> featureList)
	{
		this.featureList = featureList;
	}

}
