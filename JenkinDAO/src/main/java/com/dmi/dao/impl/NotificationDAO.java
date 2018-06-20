package com.dmi.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.INotificationDAO;
import com.dmi.dao.model.NotificationContact;
import com.dmi.dao.model.User;

@Repository
public class NotificationDAO implements INotificationDAO {

	private static final Logger LOG = Logger.getLogger(NotificationDAO.class);

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public void addNotificationInfo(NotificationContact notificationContact) {
		entityManager.persist(notificationContact);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public void updateNotificationInfo(NotificationContact notificationContact)
	{
		entityManager.merge(notificationContact);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public NotificationContact get(User user)
	{
		NotificationContact nc = null;
		try
		{
			String queryStr = "SELECT nc FROM NotificationContact nc where nc.userBean = :userBean";
			TypedQuery<NotificationContact> query = entityManager.createQuery(queryStr, NotificationContact.class);
			query.setParameter("userBean", user);
			nc = (NotificationContact) query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			nc = null;
		}
		return nc;
	}
}
