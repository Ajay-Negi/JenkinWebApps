package com.dmi.dto;

import java.util.Date;

public class DeviceTypeDTO {

	private Long id;
	private String deviceTypeName;
	private String oemName;
	private Date updatedTimeStamp;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDeviceTypeName() {
		return deviceTypeName;
	}
	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}
	public String getOemName() {
		return oemName;
	}
	public void setOemName(String oemName) {
		this.oemName = oemName;
	}
	public Date getUpdatedTimeStamp() {
		return updatedTimeStamp;
	}
	public void setUpdatedTimeStamp(Date updatedTimeStamp) {
		this.updatedTimeStamp = updatedTimeStamp;
	}
	
	
}
