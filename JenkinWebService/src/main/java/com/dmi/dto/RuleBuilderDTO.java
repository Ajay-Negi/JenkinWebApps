package com.dmi.dto;

import java.io.Serializable;

/**
 * 
 * @author ANegi
 *
 */
public class RuleBuilderDTO implements Serializable{
	
	private static final long serialVersionUID = -8593244040042347412L;

	private Long subServiceId;
	private Long messageModelId;
	private Long emailTemplateId;
	private Long smsTemplateId;
	private String ruleContent;
	private String ruleName;
	
	public Long getSubServiceId() {
		return subServiceId;
	}
	public void setSubServiceId(Long subServiceId) {
		this.subServiceId = subServiceId;
	}
	public Long getMessageModelId() {
		return messageModelId;
	}
	public void setMessageModelId(Long messageModelId) {
		this.messageModelId = messageModelId;
	}
	public Long getEmailTemplateId() {
		return emailTemplateId;
	}
	public void setEmailTemplateId(Long emailTemplateId) {
		this.emailTemplateId = emailTemplateId;
	}
	public Long getSmsTemplateId() {
		return smsTemplateId;
	}
	public void setSmsTemplateId(Long smsTemplateId) {
		this.smsTemplateId = smsTemplateId;
	}
	public String getRuleContent() {
		return ruleContent;
	}
	public void setRuleContent(String ruleContent) {
		this.ruleContent = ruleContent;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	
	
}
