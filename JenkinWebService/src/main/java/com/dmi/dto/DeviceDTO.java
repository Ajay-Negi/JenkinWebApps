package com.dmi.dto;

import java.io.Serializable;

/**
 * 
 * @author ANegi
 *
 */
public class DeviceDTO implements Serializable
{
	private static final long serialVersionUID = -8593244040042347412L;

	private Long registrationId;
	private String deviceId;
	private String alias;
	private Long deviceStatusId;
	private String deviceStatus;
	private Long deviceTypeId;
	private String deviceType;

	public Long getRegistrationId()
	{
		return registrationId;
	}

	public void setRegistrationId(Long registrationId)
	{
		this.registrationId = registrationId;
	}

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public String getDeviceStatus()
	{
		return deviceStatus;
	}

	public void setDeviceStatus(String deviceStatus)
	{
		this.deviceStatus = deviceStatus;
	}

	public String getDeviceType()
	{
		return deviceType;
	}

	public void setDeviceType(String deviceType)
	{
		this.deviceType = deviceType;
	}

	public String getAlias()
	{
		return alias;
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
	}

	public Long getDeviceStatusId()
	{
		return deviceStatusId;
	}

	public void setDeviceStatusId(Long deviceStatusId)
	{
		this.deviceStatusId = deviceStatusId;
	}

	public Long getDeviceTypeId()
	{
		return deviceTypeId;
	}

	public void setDeviceTypeId(Long deviceTypeId)
	{
		this.deviceTypeId = deviceTypeId;
	}

	@Override
	public String toString()
	{
		return "DeviceDTO [registrationId=" + registrationId + ", deviceId=" + deviceId + ", alias=" + alias
				+ ", deviceStatusId=" + deviceStatusId + ", deviceStatus=" + deviceStatus + ", deviceTypeId="
				+ deviceTypeId + ", deviceType=" + deviceType + "]";
	}

}
