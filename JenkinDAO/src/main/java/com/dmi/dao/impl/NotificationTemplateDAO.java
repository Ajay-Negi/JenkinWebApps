package com.dmi.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.INotificationTemplateDAO;
import com.dmi.dao.enums.NotificationChannelEnum;
import com.dmi.dao.model.NotificationTemplate;
import com.dmi.dao.model.Oem;

/**
 * 
 * @author ANegi
 *
 */
@Repository
public class NotificationTemplateDAO implements INotificationTemplateDAO
{
	private static final Logger LOG = Logger.getLogger(NotificationTemplateDAO.class);

	@PersistenceContext
	EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<NotificationTemplate> getAll(Oem oem)
	{
		List<NotificationTemplate> notificationTemplateList = null;
		try
		{
			String queryStr = "SELECT nt FROM NotificationTemplate nt where nt.oemBean=:oem";
			Query query = entityManager.createQuery(queryStr);
			query.setParameter("oem", oem);
			notificationTemplateList = query.getResultList();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
		return notificationTemplateList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NotificationTemplate> getAll(Long oemId)
	{
		List<NotificationTemplate> notificationTemplateList = null;
		try
		{
			String queryStr = "SELECT nt FROM NotificationTemplate nt where nt.oemBean.id=:oemId";
			Query query = entityManager.createQuery(queryStr);
			query.setParameter("oemId", oemId);
			notificationTemplateList = query.getResultList();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
		return notificationTemplateList;
	}
	
	@Override
	public NotificationTemplate get(Long notificationTemplateId)
	{
		NotificationTemplate notificationTemplate = null;
		try
		{
			String queryStr = "SELECT nt FROM NotificationTemplate nt where nt.id=:id";
			Query query = entityManager.createQuery(queryStr);
			query.setParameter("id", notificationTemplateId);
			notificationTemplate =(NotificationTemplate) query.getSingleResult();
		}catch (NoResultException ex)
		{
			LOG.error("No template found for this id", ex);
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
		return notificationTemplate;
	}
	@Override
	public void save(NotificationTemplate notificationTemplate)
	{
		entityManager.persist(notificationTemplate);
		entityManager.flush();
		entityManager.clear();		
	}

	@Override
	public void update(NotificationTemplate notificationTemplate)
	{
		entityManager.merge(notificationTemplate);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public void delete(NotificationTemplate notificationTemplateId)
	{

		try
		{
		
			entityManager.remove(notificationTemplateId);
		
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
		
	}

	@Override
	public NotificationTemplate getBySubserviceAndChannel(Long subServiceId, NotificationChannelEnum channel) {
		NotificationTemplate template = null;
		
		try{
			String queryString = "SELECT nt FROM NotificationTemplate nt WHERE "
					+ "LOWER(nt.notificationChannelBean.alias) = :channel and nt.subServiceBean.id = :subServiceId";
			
			TypedQuery<NotificationTemplate> query = entityManager.createQuery(queryString, NotificationTemplate.class);
			query.setParameter("channel", channel.toString().toLowerCase());
			query.setParameter("subServiceId", subServiceId);
			
			template = query.getSingleResult();
			
		}catch(Exception ex){
			LOG.error(ex.getMessage(), ex);
		}
		return template;
	}
	
	@Override
	public NotificationTemplate getBySubserviceAndChannel(Long subServiceId, Long channelTypeId) {
		NotificationTemplate template = null;
		
		try{
			String queryString = "SELECT nt FROM NotificationTemplate nt WHERE "
					+ "nt.notificationChannelBean.id = :channelId AND nt.subServiceBean.id = :subServiceId";
			
			TypedQuery<NotificationTemplate> query = entityManager.createQuery(queryString, NotificationTemplate.class);
			query.setParameter("channelId", channelTypeId);
			query.setParameter("subServiceId", subServiceId);
			
			template = query.getSingleResult();
			
		}catch(Exception ex){
			LOG.error(ex.getMessage(), ex);
		}
		return template;
	}
	
	@Override
	public List<NotificationTemplate> getByChannel(String channel, Long oemId) {
		
		List<NotificationTemplate> templateList = null;
		
		try{
			String queryString = "SELECT nt FROM NotificationTemplate nt WHERE LOWER(nt.notificationChannelBean.alias) = :channel "
					+ "AND nt.oemBean.id = :oemId ";
			
			TypedQuery<NotificationTemplate> query = entityManager.createQuery(queryString, NotificationTemplate.class);
			query.setParameter("channel", channel.toLowerCase());
			query.setParameter("oemId", oemId);
			
			
			templateList = query.getResultList();
			
		}catch(Exception ex){
			LOG.error(ex.getMessage(), ex);
		}
		return templateList;
	}

}
