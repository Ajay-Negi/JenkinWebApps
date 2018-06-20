package com.dmi.dto;

import java.io.Serializable;

/**
 * 
 * @author Ajay Negi
 */
public class NotificationTemplateDTO implements Serializable
{
	private static final long serialVersionUID = -8593244040042347412L;
	private Long id;
	private Long channel;
	private String name;
	private String content;
	private Long subService;
	private Long deviceTypeId;
	private Long type;
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public Long getDeviceTypeId() {
		return deviceTypeId;
	}
	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}
	public Long getType()
	{
		return type;
	}
	public void setType(Long type)
	{
		this.type = type;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public Long getSubService()
	{
		return subService;
	}
	public void setSubService(Long subService)
	{
		this.subService = subService;
	}
	public Long getChannel()
	{
		return channel;
	}
	public void setChannel(Long channelId)
	{
		this.channel = channelId;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	@Override
	public String toString()
	{
		return "NotificationTemplateDTO [id=" + id + ", type=" + type + ", name=" + name
				+ ", content=" + content + ", subService=" + subService + ", channel=" + channel
				+ "]";
	}
	
	
}
