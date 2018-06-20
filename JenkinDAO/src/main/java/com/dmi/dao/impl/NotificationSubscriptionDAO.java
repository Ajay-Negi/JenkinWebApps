package com.dmi.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.INotificationSubscriptionDAO;
import com.dmi.dao.model.NotificationSubscription;
import com.dmi.dao.model.SubServiceSubscription;

/**
 * 
 * @author ANegi
 *
 */
@Repository
public class NotificationSubscriptionDAO implements INotificationSubscriptionDAO
{
	private static final Logger LOG = Logger.getLogger(NotificationSubscriptionDAO.class);

	@PersistenceContext
	EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<NotificationSubscription> getNotificationSubscriptions(SubServiceSubscription subServiceSubscriptionBean)
	{
		List<NotificationSubscription> notificationSubscriptionList = null;
		try
		{
			String queryStr = "SELECT ss FROM NotificationSubscription ss where ss.subServiceSubscriptionBean=:subServiceSubscriptionBean";
			Query query = entityManager.createQuery(queryStr);
			query.setParameter("subServiceSubscriptionBean", subServiceSubscriptionBean);
			notificationSubscriptionList = query.getResultList();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			notificationSubscriptionList = new ArrayList<>();
		}
		return notificationSubscriptionList;
	}

	@Override
	public void remove(SubServiceSubscription subServiceSubscriptionBean)
	{
		try
		{
			String queryStr = "DELETE FROM NotificationSubscription ss where ss.subServiceSubscriptionBean=:subServiceSubscriptionBean";
			Query query = entityManager.createQuery(queryStr);
			query.setParameter("subServiceSubscriptionBean", subServiceSubscriptionBean);
			query.executeUpdate();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
		
	}

	@Override
	public void save(NotificationSubscription notificationSubscription)
	{
		entityManager.persist(notificationSubscription);
		entityManager.flush();
		entityManager.clear();		
	}
}
