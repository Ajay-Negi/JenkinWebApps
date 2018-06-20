package com.dmi.dao.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the vhr_template database table.
 * 
 */
@Entity
@Table(name="vhr_template")
@NamedQuery(name="VhrTemplate.findAll", query="SELECT v FROM VhrTemplate v")
public class VhrTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Lob
	private byte[] content;

	private String name;

	//bi-directional many-to-one association to DeviceType
	@ManyToOne
	@JoinColumn(name="DEVICE_TYPE")
	private DeviceType deviceTypeBean;

	public VhrTemplate() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getContent() {
		return this.content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DeviceType getDeviceTypeBean() {
		return this.deviceTypeBean;
	}

	public void setDeviceTypeBean(DeviceType deviceTypeBean) {
		this.deviceTypeBean = deviceTypeBean;
	}

}