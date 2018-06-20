package com.dmi.dao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the vin database table.
 * 
 */
@Entity
@NamedQuery(name="Vin.findAll", query="SELECT v FROM Vin v")
public class Vin implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	@Column(name="`KEY`")
	private String key;

	private String status;

	//bi-directional many-to-one association to Device
	@OneToOne(mappedBy="vinBean")
	@JsonIgnore
	private Device device;
	
	//bi-directional many-to-one association to DeviceConfig
	@OneToOne(mappedBy="vinBean")
	@JsonIgnore
	private DeviceConfig deviceConfig;

	public Vin() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Device getDevice() {
		return this.device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public DeviceConfig getDeviceConfig() {
		return deviceConfig;
	}

	public void setDeviceConfig(DeviceConfig deviceConfig) {
		this.deviceConfig = deviceConfig;
	}

	

}