package com.dmi.dao.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the message_model database table.
 * 
 */
@Entity
@Table(name="message_model")
@NamedQuery(name="MessageModel.findAll", query="SELECT m FROM MessageModel m")
public class MessageModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name="CREATED_BY_ID")
	private String createdById;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_TIMESTAMP")
	private Date createdTimestamp;

	@Lob
	@Column(name="MESSAGE_FORMAT")
	private String messageFormat;
	
	@Column(name="MODEL_NAME")
	private String modelName;

	@Column(name="UPDATED_BY_ID")
	private String updatedById;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_TIMESTAMP")
	private Date updatedTimestamp;

	//bi-directional many-to-one association to DeviceType
	@ManyToOne
	@JoinColumn(name="DEVICE_TYPE")
	private DeviceType deviceTypeBean;

	//bi-directional many-to-one association to MessageRule
	@OneToMany(mappedBy="messageModel")
	@JsonIgnore
	private List<MessageRule> messageRules;

	public MessageModel() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreatedById() {
		return this.createdById;
	}

	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	public Date getCreatedTimestamp() {
		return this.createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public String getMessageFormat() {
		return this.messageFormat;
	}

	public void setMessageFormat(String messageFormat) {
		this.messageFormat = messageFormat;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getUpdatedById() {
		return this.updatedById;
	}

	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}

	public Date getUpdatedTimestamp() {
		return this.updatedTimestamp;
	}

	public void setUpdatedTimestamp(Date updatedTimestamp) {
		this.updatedTimestamp = updatedTimestamp;
	}

	public DeviceType getDeviceTypeBean() {
		return this.deviceTypeBean;
	}

	public void setDeviceTypeBean(DeviceType deviceTypeBean) {
		this.deviceTypeBean = deviceTypeBean;
	}

	public List<MessageRule> getMessageRules() {
		return this.messageRules;
	}

	public void setMessageRules(List<MessageRule> messageRules) {
		this.messageRules = messageRules;
	}

	public MessageRule addMessageRule(MessageRule messageRule) {
		getMessageRules().add(messageRule);
		messageRule.setMessageModel(this);

		return messageRule;
	}

	public MessageRule removeMessageRule(MessageRule messageRule) {
		getMessageRules().remove(messageRule);
		messageRule.setMessageModel(null);

		return messageRule;
	}

}