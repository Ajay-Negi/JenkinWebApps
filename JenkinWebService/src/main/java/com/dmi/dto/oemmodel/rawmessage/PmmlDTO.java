package com.dmi.dto.oemmodel.rawmessage;

import java.io.Serializable;

import org.dmg.pmml.PMML;

public class PmmlDTO implements Serializable
{

	private static final long serialVersionUID = 1L;

	private String mlName;
	private String mlType;
	private PMML model;

	public PmmlDTO(String mlName, PMML model)
	{
		super();
		this.mlName = mlName;
		this.model = model;
	}

	public PmmlDTO()
	{

	}

	public String getMlType()
	{
		return mlType;
	}

	public void setMlType(String mlType)
	{
		this.mlType = mlType;
	}

	public String getMlName()
	{
		return mlName;
	}

	public void setMlName(String mlName)
	{
		this.mlName = mlName;
	}

	public PMML getModel()
	{
		return model;
	}

	public void setModel(PMML model)
	{
		this.model = model;
	}

}
