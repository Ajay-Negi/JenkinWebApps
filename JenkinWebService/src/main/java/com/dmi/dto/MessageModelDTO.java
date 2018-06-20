/**
 * 
 */
package com.dmi.dto;


/**
 * @author AChowdhury
 *
 */
public class MessageModelDTO
{

	private Long id;
	private Long deviceTypeId;
	private String modelName;
	private String messageFormat;
	private String createdTimeStamp;
	private String updatedTimeStamp;
	private String createdByUserId;

	public Long getId()
	{
		return id;
	}

	public MessageModelDTO setId(Long id)
	{
		this.id = id;
		return this;
	}

	public Long getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}
	

	public String getCreatedByUserId() {
		return createdByUserId;
	}

	public void setCreatedByUserId(String createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	public String getCreatedTimeStamp() {
		return createdTimeStamp;
	}

	public MessageModelDTO setCreatedTimeStamp(String createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
		return this;
	}

	public String getMessageFormat()
	{
		return messageFormat;
	}

	public MessageModelDTO setMessageFormat(String messageFormat)
	{
		this.messageFormat = messageFormat;
		return this;
	}
	
	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getUpdatedTimeStamp()
	{
		return updatedTimeStamp;
	}

	public MessageModelDTO setUpdatedTimeStamp(String updatedTimeStamp)
	{
		this.updatedTimeStamp = updatedTimeStamp;
		return this;
	}

}
