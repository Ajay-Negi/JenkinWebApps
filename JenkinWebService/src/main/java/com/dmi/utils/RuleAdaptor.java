package com.dmi.utils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.dmi.dao.IMessageModelDAO;
import com.dmi.dao.INotificationTemplateDAO;
import com.dmi.dao.ISubServiceDAO;
import com.dmi.dao.model.MessageRule;
import com.dmi.dto.MessageRuleDTO;


public class RuleAdaptor
{

	private static final Logger LOG = Logger.getLogger(RuleAdaptor.class);
	
	@Autowired
	private ISubServiceDAO subServiceDAO;

	@Autowired
	private IMessageModelDAO messageModelDAO;

	@Autowired
	private INotificationTemplateDAO notificationTemplateDAO;

	
	public static MessageRule createMessageRulePOJO(MessageRuleDTO messageRuleDTO)
	{
		MessageRule messageRule = new MessageRule();
		
		messageRule.setRuleName(messageRuleDTO.getRuleName());
		messageRule.setRuleMessage(messageRuleDTO.getRuleText());
		messageRule.setCreatedTimestamp(messageRuleDTO.getCreatedTimeStamp());
		messageRule.setUpdatedTimestamp(messageRuleDTO.getUpdatedTimeStamp());
		messageRule.setCreatedById(messageRuleDTO.getCreatedById());

		return messageRule;
	}

	public static MessageRuleDTO createMessageRuleDTO(MessageRule messageRulePOJO)
	{
		MessageRuleDTO messageRuleDTO = new MessageRuleDTO();

		messageRuleDTO.setId(messageRulePOJO.getId());
		messageRuleDTO.setRuleName(messageRulePOJO.getRuleName());
		messageRuleDTO.setRuleText(messageRulePOJO.getRuleMessage().trim());
		messageRuleDTO.setEmailTemplateId(messageRulePOJO.getNotificationEmailTemplate().getId());
		messageRuleDTO.setSmsTemplateId(messageRulePOJO.getNotificationSmsTemplate().getId());
		//messageRuleDTO.setSubServiceId(messageRulePOJO.getSubService().getId());
		//messageRuleDTO.setMessageModelId(messageRulePOJO.getMessageModel().getId());
		messageRuleDTO.setUpdatedTimeStamp(messageRulePOJO.getUpdatedTimestamp());
		messageRuleDTO.setCreatedTimeStamp(messageRulePOJO.getCreatedTimestamp());
		
		return messageRuleDTO;
	}
}
