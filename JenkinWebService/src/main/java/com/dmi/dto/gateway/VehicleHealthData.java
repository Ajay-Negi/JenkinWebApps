package com.dmi.dto.gateway;

public class VehicleHealthData {
	
	private Long dataCollectionInterval;
	private Long dataUploadInterval;
	
	public Long getDataCollectionInterval() {
		return dataCollectionInterval;
	}
	public void setDataCollectionInterval(Long dataCollectionInterval) {
		this.dataCollectionInterval = dataCollectionInterval;
	}
	public Long getDataUploadInterval() {
		return dataUploadInterval;
	}
	public void setDataUploadInterval(Long dataUploadInterval) {
		this.dataUploadInterval = dataUploadInterval;
	}
}
