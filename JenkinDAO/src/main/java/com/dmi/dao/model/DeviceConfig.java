package com.dmi.dao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="device_config")
public class DeviceConfig implements Serializable  {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	//bi-directional many-to-one association to Vin
	@OneToOne
	@JoinColumn(name="VIN")
	private Vin vinBean;
		
	@Column(name="TELEMETRY_DATA_UPLOAD_INTERVAL")
	private Long telemetryDataUploadInterval;

	@Column(name="TELEMETRY_DATA_COLLECTION_INTERVAL")
	private Long telemetryDataCollectionInterval;

	@Column(name="VEHICLE_HEALTH_DATA_UPLOAD_INTERVAL")
	private Long vehicleHealthDataUploadInterval;

	@Column(name="VEHICLE_HEALTH_DATA_COLLECTION_INTERVAL")
	private Long vehicleHealthDataCollectionInterval;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTelemetryDataUploadInterval() {
		return telemetryDataUploadInterval;
	}

	public void setTelemetryDataUploadInterval(Long telemetryDataUploadInterval) {
		this.telemetryDataUploadInterval = telemetryDataUploadInterval;
	}

	public Long getTelemetryDataCollectionInterval() {
		return telemetryDataCollectionInterval;
	}

	public void setTelemetryDataCollectionInterval(Long telemetryDataCollectionInterval) {
		this.telemetryDataCollectionInterval = telemetryDataCollectionInterval;
	}

	public Long getVehicleHealthDataUploadInterval() {
		return vehicleHealthDataUploadInterval;
	}

	public void setVehicleHealthDataUploadInterval(Long vehicleHealthDataUploadInterval) {
		this.vehicleHealthDataUploadInterval = vehicleHealthDataUploadInterval;
	}

	public Long getVehicleHealthDataCollectionInterval() {
		return vehicleHealthDataCollectionInterval;
	}

	public void setVehicleHealthDataCollectionInterval(Long vehicleHealthDataCollectionInterval) {
		this.vehicleHealthDataCollectionInterval = vehicleHealthDataCollectionInterval;
	}

	public Vin getVinBean() {
		return vinBean;
	}

	public void setVinBean(Vin vinBean) {
		this.vinBean = vinBean;
	}

	
	

}
