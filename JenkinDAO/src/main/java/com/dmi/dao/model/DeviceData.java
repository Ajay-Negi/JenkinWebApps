package com.dmi.dao.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the device_data database table.
 * 
 */
@Entity
@Table(name="device_data")
@NamedQuery(name="DeviceData.findAll", query="SELECT d FROM DeviceData d")
public class DeviceData implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Lob
	private byte[] data;

	//bi-directional many-to-one association to Device
	@ManyToOne
	private Device device;

	public DeviceData() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getData() {
		return this.data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public Device getDevice() {
		return this.device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

}