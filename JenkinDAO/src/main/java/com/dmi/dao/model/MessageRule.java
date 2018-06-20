package com.dmi.dao.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the message_rule database table.
 * 
 */
@Entity
@Table(name="message_rule")
@NamedQuery(name="MessageRule.findAll", query="SELECT m FROM MessageRule m")
public class MessageRule implements Serializable {
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
	@Column(name="RULE_MESSAGE")
	private String ruleMessage;

	@Column(name="RULE_NAME")
	private String ruleName;

	@Column(name="UPDATED_BY_ID")
	private String updatedById;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_TIMESTAMP")
	private Date updatedTimestamp;

	//bi-directional many-to-one association to MessageModel
	@ManyToOne
	@JoinColumn(name="MESSAGE_MODEL_ID")
	private MessageModel messageModel;

	//bi-directional many-to-one association to NotificationTemplate
	@ManyToOne
	@JoinColumn(name="EMAIL_TEMPLATE")
	private NotificationTemplate notificationEmailTemplate;

	//bi-directional many-to-one association to NotificationTemplate
	@ManyToOne
	@JoinColumn(name="SMS_TEMPLATE")
	private NotificationTemplate notificationSmsTemplate;

	//bi-directional many-to-one association to SubService
	@ManyToOne
	@JoinColumn(name="SUB_SERVICE_ID")
	private SubService subService;

	public MessageRule() {
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

	public String getRuleMessage() {
		return this.ruleMessage;
	}

	public void setRuleMessage(String ruleMessage) {
		this.ruleMessage = ruleMessage;
	}

	public String getRuleName() {
		return this.ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
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

	public MessageModel getMessageModel() {
		return this.messageModel;
	}

	public void setMessageModel(MessageModel messageModel) {
		this.messageModel = messageModel;
	}

	public NotificationTemplate getNotificationEmailTemplate() {
		return this.notificationEmailTemplate;
	}

	public void setNotificationEmailTemplate(NotificationTemplate notificationEmailTemplate) {
		this.notificationEmailTemplate = notificationEmailTemplate;
	}

	public NotificationTemplate getNotificationSmsTemplate() {
		return this.notificationSmsTemplate;
	}

	public void setNotificationSmsTemplate(NotificationTemplate notificationSmsTemplate) {
		this.notificationSmsTemplate = notificationSmsTemplate;
	}

	public SubService getSubService() {
		return this.subService;
	}

	public void setSubService(SubService subService) {
		this.subService = subService;
	}

}