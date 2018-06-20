package com.dmi.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageRuleDTO
{

	Long id;
	String ruleName;
	String ruleText;

	Long messageModelId;

	Long subServiceId;

	Date updatedTimeStamp;
	Date createdTimeStamp;
	
	String createdById;

	Long emailTemplateId;
	
	Long smsTemplateId;
	
	List<DeviceTypeDTO> deviceTypeDTOList = new ArrayList<>();
	List<KeyValuePairDTO> messageModelList = new ArrayList<>();
	List<NotificationTemplateDTO> notificationTemplateList = new ArrayList<>();
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getRuleText() {
		return ruleText;
	}
	public void setRuleText(String ruleText) {
		this.ruleText = ruleText;
	}
	
	public Date getCreatedTimeStamp() {
		return createdTimeStamp;
	}
	public void setCreatedTimeStamp(Date createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}
	
	public Date getUpdatedTimeStamp() {
		return updatedTimeStamp;
	}
	public void setUpdatedTimeStamp(Date updatedTimeStamp) {
		this.updatedTimeStamp = updatedTimeStamp;
	}
	
	public Long getMessageModelId() {
		return messageModelId;
	}
	public void setMessageModelId(Long messageModelId) {
		this.messageModelId = messageModelId;
	}
	public Long getSubServiceId() {
		return subServiceId;
	}
	public void setSubServiceId(Long subServiceId) {
		this.subServiceId = subServiceId;
	}
	public Long getEmailTemplateId() {
		return emailTemplateId;
	}
	public void setEmailTemplateId(Long emailTemplateId) {
		this.emailTemplateId = emailTemplateId;
	}
	public Long getSmsTemplateId() {
		return smsTemplateId;
	}
	public void setSmsTemplateId(Long smsTemplateId) {
		this.smsTemplateId = smsTemplateId;
	}
	public List<DeviceTypeDTO> getDeviceTypeDTOList() {
		return deviceTypeDTOList;
	}
	public void setDeviceTypeDTOList(List<DeviceTypeDTO> deviceTypeDTOList) {
		this.deviceTypeDTOList = deviceTypeDTOList;
	}
	public List<KeyValuePairDTO> getMessageModelList() {
		return messageModelList;
	}
	public void setMessageModelList(List<KeyValuePairDTO> messageModelList) {
		this.messageModelList = messageModelList;
	}
	public List<NotificationTemplateDTO> getNotificationTemplateList() {
		return notificationTemplateList;
	}
	public void setNotificationTemplateList(List<NotificationTemplateDTO> notificationTemplateList) {
		this.notificationTemplateList = notificationTemplateList;
	}
	public String getCreatedById() {
		return createdById;
	}
	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}
	
	



}
