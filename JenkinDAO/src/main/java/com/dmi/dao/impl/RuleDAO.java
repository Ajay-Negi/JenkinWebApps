package com.dmi.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.IRuleDAO;
import com.dmi.dao.model.MessageRule;

@Repository
public class RuleDAO implements IRuleDAO
{
	private static final Logger LOG = Logger.getLogger(RuleDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public MessageRule createRule(MessageRule messageRule)
	{

		entityManager.persist(messageRule);
		return messageRule;
	}

	@Override
	public List<MessageRule> getAllAvailableRulesForOem(Long oemId)
	{
		
		String queryStr = "SELECT mr FROM MessageRule mr WHERE mr.messageModel.deviceTypeBean.oemBean.id = :oemId";
		TypedQuery<MessageRule> query = entityManager.createQuery(queryStr, MessageRule.class);
		query.setParameter("oemId", oemId);
		
		List<MessageRule> messageRules = new ArrayList<>();
		
		try
		{
			messageRules = query.getResultList();
			return messageRules;
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			messageRules = null;
			return messageRules;
		}
		
	}
	
	@Override
	public List<MessageRule> getAllAvailableRulesForUser(String userName) {
		
		String queryStr = "SELECT mr FROM MessageRule mr WHERE mr.createdById = :userName";
		TypedQuery<MessageRule> query = entityManager.createQuery(queryStr, MessageRule.class);
		query.setParameter("userName", userName);
		
		List<MessageRule> messageRules = new ArrayList<>();
		
		try
		{
			messageRules = query.getResultList();
			return messageRules;
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			messageRules = null;
			return messageRules;
		}
	}


	@Override
	public MessageRule getAllAvailableRuleForSubService(Long subServiceId)
	{
		
		try
		{
			String queryStr = "SELECT mr FROM MessageRule mr WHERE mr.subService.id = :subServiceId";
			
			TypedQuery<MessageRule> query = entityManager.createQuery(queryStr, MessageRule.class);
			query.setParameter("subServiceId", subServiceId);
			
			MessageRule messageRule = query.getSingleResult();
			
			return messageRule;
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			return null;
		}
		
	}

	@Override
	public MessageRule getMessageRule(Long ruleId)
	{
		try
		{
			String queryStr = "SELECT mr FROM MessageRule mr WHERE mr.id = :ruleId";
			
			TypedQuery<MessageRule> query = entityManager.createQuery(queryStr, MessageRule.class);
			query.setParameter("ruleId", ruleId);
			
			return query.getSingleResult();
			
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			return null;
		}
	}

	@Override
	public MessageRule updateRule(MessageRule messageRule)
	{

		entityManager.merge(messageRule);
		return messageRule;
	}

	@Override
	public MessageRule findByRuleName(String ruleName)
	{

		try
		{
			String queryStr = "SELECT mr FROM MessageRule mr WHERE LOWER(mr.ruleName) = :ruleName";
			
			TypedQuery<MessageRule> query = entityManager.createQuery(queryStr, MessageRule.class);
			query.setParameter("ruleName", ruleName.toLowerCase());
			
			return query.getSingleResult();
			
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			return null;
		}

	}

	@Override
	public void deleteRuleById(MessageRule messageRule)
	{

		
		try
		{
			entityManager.remove(messageRule);
			entityManager.flush();
			
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			
		}
		

	}

	
}
