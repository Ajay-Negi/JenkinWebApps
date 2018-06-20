package com.dmi.dao.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author MBansal
 *
 */

@Entity
@Table(name = "ml_models_and_exp")
@NamedQuery(name = "MLModelsAndExp.findAll", query = "SELECT c FROM MLModelsAndExp c")
public class MLModelsAndExp implements Serializable
{
	private static final long serialVersionUID = 1L;

	public MLModelsAndExp(String modelName, MLModelsTypes mlModelType)
	{
		this.modelName = modelName;
		this.mlModelsTypes = mlModelType;
	}

	public MLModelsAndExp()
	{

	}

	@Id
	@Column(name = "MODEL_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name = "MODEL_NAME")
	private String modelName;

	@Lob
	@Column(name = "MODEL_EQUATION", length = 100000)
	private byte[] modelEquation;

	@ManyToOne
	@JoinColumn(name = "OEM")
	private Oem oem;

	@ManyToOne
	@JoinColumn(name = "DEVICE_TYPE")
	private DeviceType deviceType;

	@ManyToOne
	@JoinColumn(name = "MODEL_TYPE_ID")
	private MLModelsTypes mlModelsTypes;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_TIMESTAMP")
	private Date createdTimeStamp;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_TIMESTAMP")
	private Date updatedTimeStamp;

	@Column(name = "CREATED_BY_ID")
	private String createdById;

	@Column(name = "UPDATED_BY_ID")
	private String updatedById;

	public Date getCreatedTimeStamp()
	{
		return createdTimeStamp;
	}

	public void setCreatedTimeStamp(Date createdTimeStamp)
	{
		this.createdTimeStamp = createdTimeStamp;
	}

	public Date getUpdatedTimeStamp()
	{
		return updatedTimeStamp;
	}

	public void setUpdatedTimeStamp(Date updatedTimeStamp)
	{
		this.updatedTimeStamp = updatedTimeStamp;
	}

	public String getCreatedById()
	{
		return createdById;
	}

	public void setCreatedById(String createdById)
	{
		this.createdById = createdById;
	}

	public String getUpdatedById()
	{
		return updatedById;
	}

	public void setUpdatedById(String updatedById)
	{
		this.updatedById = updatedById;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModelName()
	{
		return modelName;
	}

	public void setModelName(String modelName)
	{
		this.modelName = modelName;
	}

	public byte[] getModelEquation()
	{
		return modelEquation;
	}

	public void setModelEquation(byte[] modelEquation)
	{
		this.modelEquation = modelEquation;
	}

	public Oem getOem() {
		return oem;
	}

	public void setOem(Oem oem) {
		this.oem = oem;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}


	public MLModelsTypes getMlModelsTypes()
	{
		return mlModelsTypes;
	}

	public void setMlModelsTypes(MLModelsTypes mlModelsTypes)
	{
		this.mlModelsTypes = mlModelsTypes;
	}

}
