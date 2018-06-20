package com.dmi.dto;

import java.io.Serializable;
/**
 * 
 * @author Mukul Bansal
 */
public class UomDTO implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String uom;
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public String getUom()
	{
		return uom;
	}
	public void setUom(String uom)
	{
		this.uom = uom;
	}
	@Override
	public String toString()
	{
		return "UomDTO [id=" + id + ", uom=" + uom + "]";
	}
	
}
