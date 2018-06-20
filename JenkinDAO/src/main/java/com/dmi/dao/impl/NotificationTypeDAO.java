package com.dmi.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.INotificationTypeDAO;
import com.dmi.dao.model.NotificationType;

/**
 * 
 * @author ANegi
 *
 */
@Repository
public class NotificationTypeDAO implements INotificationTypeDAO
{
	private static final Logger LOG = Logger.getLogger(NotificationTypeDAO.class);

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public NotificationType get(Long notificationTypeId)
	{
		NotificationType notificationType = null;
		try
		{
			String queryStr = "SELECT nt FROM NotificationType nt where nt.id=:id";
			TypedQuery<NotificationType> query = entityManager.createQuery(queryStr, NotificationType.class);
			query.setParameter("id", notificationTypeId);
			notificationType = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
		return notificationType;
	}

	@Override
	public NotificationType get(String alias)
	{
		NotificationType notificationType = null;
		try
		{
			String queryStr = "SELECT nt FROM NotificationType nt where nt.alias=:alias";
			TypedQuery<NotificationType> query = entityManager.createQuery(queryStr, NotificationType.class);
			query.setParameter("alias", alias);
			notificationType = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
		return notificationType;
	}
	
	@Override
	public List<NotificationType> getAll()
	{
		List<NotificationType> notificationTypeList = null;
		try
		{
			String queryStr = "SELECT nt FROM NotificationType nt";
			TypedQuery<NotificationType> query = entityManager.createQuery(queryStr, NotificationType.class);
			notificationTypeList = query.getResultList();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
		return notificationTypeList;
	}
}
