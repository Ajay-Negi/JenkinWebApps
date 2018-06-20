package com.dmi.dao.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the device_status database table.
 * 
 */
@Entity
@Table(name="device_status")
@NamedQuery(name="DeviceStatus.findAll", query="SELECT d FROM DeviceStatus d")
public class DeviceStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String alias;

	//bi-directional many-to-one association to DeviceRegistration
	@OneToMany(mappedBy="deviceStatusBean")
	@JsonIgnore
	private List<DeviceRegistration> deviceRegistrations;

	public DeviceStatus() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public List<DeviceRegistration> getDeviceRegistrations() {
		return this.deviceRegistrations;
	}

	public void setDeviceRegistrations(List<DeviceRegistration> deviceRegistrations) {
		this.deviceRegistrations = deviceRegistrations;
	}

	public DeviceRegistration addDeviceRegistration(DeviceRegistration deviceRegistration) {
		getDeviceRegistrations().add(deviceRegistration);
		deviceRegistration.setDeviceStatusBean(this);

		return deviceRegistration;
	}

	public DeviceRegistration removeDeviceRegistration(DeviceRegistration deviceRegistration) {
		getDeviceRegistrations().remove(deviceRegistration);
		deviceRegistration.setDeviceStatusBean(null);

		return deviceRegistration;
	}

}