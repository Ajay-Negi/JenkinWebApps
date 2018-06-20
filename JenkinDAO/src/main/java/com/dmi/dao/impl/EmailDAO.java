package com.dmi.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.IEmailDAO;
import com.dmi.dao.model.Email;

@Repository
public class EmailDAO implements IEmailDAO
{
	private static final Logger LOG = Logger.getLogger(EmailDAO.class);

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public boolean checkEmailAvailability(String primaryEmail)
	{
		try
		{
			String queryStr = "SELECT email FROM Email email where LOWER(email.primaryEmail) = :primaryEmail";
			TypedQuery<Email> query = entityManager.createQuery(queryStr, Email.class);
			query.setParameter("primaryEmail", primaryEmail.toLowerCase());
			query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			return false;
		}
		return true;
	}

	@Override
	public void addEmailInfo(Email email)
	{
		entityManager.persist(email);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public Email getEmailInfo(String primaryEmail)
	{
		Email email = null;
		try
		{
			String queryStr = "SELECT email FROM Email email where LOWER(email.primaryEmail)=:primaryEmail";
			TypedQuery<Email> query = entityManager.createQuery(queryStr, Email.class);
			query.setParameter("primaryEmail", primaryEmail.toLowerCase());
			email = (Email) query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			email = null;
		}
		return email;
	}

	@Override
	public void updateEmailInfo(Email email)
	{
		entityManager.merge(email);
		entityManager.flush();
		entityManager.clear();
	}
}
