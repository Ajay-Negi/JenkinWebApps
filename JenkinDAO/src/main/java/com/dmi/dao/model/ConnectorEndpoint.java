package com.dmi.dao.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


/**
 * The persistent class for the notification_endpoint database table.
 * 
 */
@Entity
@Table(name="connector_endpoint")
@NamedQuery(name="ConnectorEndpoint.findAll", query="SELECT n FROM ConnectorEndpoint n")
public class ConnectorEndpoint implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="JMS_QNAME")
	private String jmsQName;

	@Column(name="JMS_URL")
	private String jmsUrl;

	@Column(name="NOTIFICATION_TYPE")
	private String notificationType;

	@Column(name="RESTSERVICE_NAME")
	private String restServiceName;

	@Column(name="RESTSERVICE_URL")
	private String restServiceUrl;

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
	
	@Column(name="USER_ID")
	private long userId;

	public ConnectorEndpoint() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getJmsQName() {
		return jmsQName;
	}

	public void setJmsQName(String jmsQName) {
		this.jmsQName = jmsQName;
	}

	public String getJmsUrl() {
		return jmsUrl;
	}

	public void setJmsUrl(String jmsUrl) {
		this.jmsUrl = jmsUrl;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public String getRestServiceName() {
		return restServiceName;
	}

	public void setRestServiceName(String restServiceName) {
		this.restServiceName = restServiceName;
	}

	public String getRestServiceUrl() {
		return restServiceUrl;
	}

	public void setRestServiceUrl(String restServiceUrl) {
		this.restServiceUrl = restServiceUrl;
	}

	public String getCreatedById() {
		return createdById;
	}

	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	public String getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}

	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	
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

	

}