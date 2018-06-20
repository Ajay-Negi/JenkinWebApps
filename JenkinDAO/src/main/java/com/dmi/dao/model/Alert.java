package com.dmi.dao.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the alert database table.
 * 
 */
@Entity
@NamedQuery(name="Alert.findAll", query="SELECT a FROM Alert a")
public class Alert implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name="ALERT_TEXT")
	private String alertText;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="INSERT_TIME")
	private Date insertTime;

	//bi-directional many-to-one association to Device
	@ManyToOne
	@JoinColumn(name="DEVICE")
	@JsonIgnore
	private Device deviceBean;

	public Alert() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAlertText() {
		return this.alertText;
	}

	public void setAlertText(String alertText) {
		this.alertText = alertText;
	}

	public Date getInsertTime() {
		return this.insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	public Device getDeviceBean() {
		return this.deviceBean;
	}

	public void setDeviceBean(Device deviceBean) {
		this.deviceBean = deviceBean;
	}

}