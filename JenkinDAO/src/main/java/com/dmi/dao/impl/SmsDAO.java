package com.dmi.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.ISmsDAO;
import com.dmi.dao.model.Sms;

@Repository
public class SmsDAO implements ISmsDAO
{
	private static final Logger LOG = Logger.getLogger(SmsDAO.class);

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public Sms addSmsInfo(Sms sms)
	{
		entityManager.persist(sms);
		entityManager.flush();
		entityManager.clear();
		
		return sms;
	}

	@Override
	public Sms getSmsInfo(String mobileNumber)
	{
		Sms sms = null;
		try
		{
			String queryStr = "SELECT sms FROM Sms sms where LOWER(sms.mobileNumber) = :mobileNumber";
			TypedQuery<Sms> query = entityManager.createQuery(queryStr, Sms.class);
			query.setParameter("mobileNumber", mobileNumber.toLowerCase());
			sms = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			sms = null;
		}
		return sms;
	}

	@Override
	public void updateSmsInfo(Sms sms)
	{
		entityManager.merge(sms);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public void remove(Sms sms)
	{
		try
		{
			String queryStr = "DELETE FROM Sms sms where sms.id = :id";
			TypedQuery<Sms> query = entityManager.createQuery(queryStr, Sms.class);
			query.setParameter("id", sms.getId());
			query.executeUpdate();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
	}
}
