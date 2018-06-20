package com.dmi.dao.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the CUSTOM_RULE_ASSET database table.
 * 
 */
@Entity
@Table(name="CUSTOM_RULE_ASSET")
@NamedQuery(name="CustomRuleAsset.findAll", query="SELECT c FROM CustomRuleAsset c")
public class CustomRuleAsset implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	@Column(name="JAR_EXECUTION_PATH")
	private String jarExecutionPath;

	@Column(name="JAR_FILE_PATH")
	private String jarFilePath;

	@Lob
	@Column(name="MESSAGE_MODEL_FORMAT")
	private String messageModelFormat;

	@Column(name="MESSAGE_MODEL_NAME")
	private String messageModelName;

	//bi-directional many-to-one association to VehicleType
	@ManyToOne
	@JoinColumn(name="DEVICE_TYPE")
	private DeviceType deviceTypeBean;

	public CustomRuleAsset() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getJarExecutionPath() {
		return this.jarExecutionPath;
	}

	public void setJarExecutionPath(String jarExecutionPath) {
		this.jarExecutionPath = jarExecutionPath;
	}

	public String getJarFilePath() {
		return this.jarFilePath;
	}

	public void setJarFilePath(String jarFilePath) {
		this.jarFilePath = jarFilePath;
	}

	public String getMessageModelFormat() {
		return this.messageModelFormat;
	}

	public void setMessageModelFormat(String messageModelFormat) {
		this.messageModelFormat = messageModelFormat;
	}

	public String getMessageModelName() {
		return this.messageModelName;
	}

	public void setMessageModelName(String messageModelName) {
		this.messageModelName = messageModelName;
	}

	public DeviceType getDeviceTypeBean() {
		return this.deviceTypeBean;
	}

	public void setDeviceTypeBean(DeviceType deviceTypeBean) {
		this.deviceTypeBean = deviceTypeBean;
	}

}