package com.dmi.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.IOemClassificationDAO;
import com.dmi.dao.model.OemClassification;

/**
 * 
 * @author Ajay Negi
 */
@Repository
public class OemClassificationDAO implements IOemClassificationDAO
{
	private static final Logger LOG = Logger.getLogger(OemClassificationDAO.class);

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public OemClassification get(Long oemClassificationId)
	{
		OemClassification oemClassification = null;
		try
		{
			String queryStr = "SELECT ss FROM OemClassification ss where ss.id = :oemClassificationId";
			TypedQuery<OemClassification> query = entityManager.createQuery(queryStr, OemClassification.class);
			query.setParameter("oemClassificationId", oemClassificationId);
			oemClassification = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.info(ex.getMessage());
		}
		return oemClassification;
	}

	@Override
	public List<OemClassification> getAll()
	{
		List<OemClassification> oemClassificationList = null;
		TypedQuery<OemClassification> query = entityManager
				.createNamedQuery("OemClassification.findAll", OemClassification.class);
		oemClassificationList = query.getResultList();
		return oemClassificationList;
	}
}
