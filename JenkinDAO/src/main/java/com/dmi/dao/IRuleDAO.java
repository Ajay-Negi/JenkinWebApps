/**
 * 
 */
package com.dmi.dao;

import java.util.List;

import com.dmi.dao.model.MessageRule;

public interface IRuleDAO
{
	public MessageRule createRule(MessageRule messageRule);

	public MessageRule getMessageRule(Long ruleId);

	public MessageRule updateRule(MessageRule messageRule);

	public MessageRule findByRuleName(String ruleName);

	public void deleteRuleById(MessageRule messageRule);

	List<MessageRule> getAllAvailableRulesForOem(Long oemId);

	MessageRule getAllAvailableRuleForSubService(Long subServiceId);

	public List<MessageRule> getAllAvailableRulesForUser(String userName);

}
