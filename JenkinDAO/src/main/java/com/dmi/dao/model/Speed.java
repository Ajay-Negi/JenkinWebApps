package com.dmi.dao.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the speed database table.
 * 
 */
@Entity
@NamedQuery(name="Speed.findAll", query="SELECT s FROM Speed s")
public class Speed implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name="SPEED_LIMIT")
	private int speedLimit;

	//bi-directional many-to-one association to Device
	@ManyToOne
	@JoinColumn(name="DEVICE")
	private Device deviceBean;

	public Speed() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getSpeedLimit() {
		return this.speedLimit;
	}

	public void setSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
	}

	public Device getDeviceBean() {
		return this.deviceBean;
	}

	public void setDeviceBean(Device deviceBean) {
		this.deviceBean = deviceBean;
	}

}