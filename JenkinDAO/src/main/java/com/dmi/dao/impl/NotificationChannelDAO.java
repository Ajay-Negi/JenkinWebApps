package com.dmi.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.INotificationChannelDAO;
import com.dmi.dao.model.NotificationChannel;

/**
 * 
 * @author ANegi
 *
 */
@Repository
public class NotificationChannelDAO implements INotificationChannelDAO
{
	private static final Logger LOG = Logger.getLogger(NotificationChannelDAO.class);

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public NotificationChannel get(Long notificationChannelId)
	{
		NotificationChannel notificationChannel = null;
		try
		{
			String queryStr = "SELECT ss FROM NotificationChannel ss where ss.id=:id";
			TypedQuery<NotificationChannel> query = entityManager.createQuery(queryStr, NotificationChannel.class);
			query.setParameter("id", notificationChannelId);
			notificationChannel = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
		return notificationChannel;
	}

	@Override
	public NotificationChannel get(String alias)
	{
		NotificationChannel notificationType = null;
		try
		{
			String queryStr = "SELECT nc FROM NotificationChannel nc where LOWER(nc.alias)=:alias";
			TypedQuery<NotificationChannel> query = entityManager.createQuery(queryStr, NotificationChannel.class);
			query.setParameter("alias", alias.toLowerCase());
			notificationType = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
		return notificationType;
	}
	
	@Override
	public List<NotificationChannel> getAll()
	{
		List<NotificationChannel> notificationChannelList = null;
		try
		{
			String queryStr = "SELECT nc FROM NotificationChannel nc";
			TypedQuery<NotificationChannel> query = entityManager.createQuery(queryStr, NotificationChannel.class);
			notificationChannelList = query.getResultList();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
		return notificationChannelList;
	}
}
