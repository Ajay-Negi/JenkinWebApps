package com.dmi.dto;

import java.io.Serializable;

/**
 * 
 * @author Mukul Bansal
 */

public class TncDTO implements Serializable
{

	private static final long serialVersionUID = 1L;

	private Long id;
	private String agreement;
	private Float version;
	private String effectuationDate;
	private Long oem;
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public String getAgreement()
	{
		return agreement;
	}
	public void setAgreement(String agreement)
	{
		this.agreement = agreement;
	}
	public Float getVersion()
	{
		return version;
	}
	public void setVersion(Float version)
	{
		this.version = version;
	}
	public String getEffectuationDate()
	{
		return effectuationDate;
	}
	public void setEffectuationDate(String effectuationDate)
	{
		this.effectuationDate = effectuationDate;
	}
	public Long getOem()
	{
		return oem;
	}
	public void setOem(Long oem)
	{
		this.oem = oem;
	}
	@Override
	public String toString()
	{
		return "TncDTO [id=" + id + ", agreement=" + agreement + ", version=" + version
				+ ", effectuationDate=" + effectuationDate + ", oem=" + oem + "]";
	}

}
