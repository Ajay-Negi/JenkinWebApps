package com.dmi.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.IGeolocationDAO;
import com.dmi.dao.model.Device;
import com.dmi.dao.model.Geolocation;

/**
 * 
 * @author ANegi
 *
 */
@Repository
public class GeolocationDAO implements IGeolocationDAO
{
	private static final Logger LOG = Logger.getLogger(GeolocationDAO.class);

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public Geolocation get(Device device)
	{
		Geolocation geolocation = null;
		try
		{
			String queryStr = "SELECT geo FROM Geolocation geo where geo.deviceBean=:deviceBean";
			TypedQuery<Geolocation> query = entityManager.createQuery(queryStr, Geolocation.class);
			query.setParameter("deviceBean", device);
			geolocation = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			geolocation = null;
		}
		return geolocation;
	}

	@Override
	public void save(Geolocation geolocation)
	{
		entityManager.persist(geolocation);
		entityManager.flush();
		entityManager.clear();
	}
	
	@Override
	public Geolocation update(Geolocation geolocation)
	{
		Geolocation geolocationBean = entityManager.merge(geolocation);
		entityManager.flush();
		entityManager.clear();
		
		return geolocationBean;
	}

	@Override
	public void delete(Device device)
	{
		String queryStr = "DELETE from Geolocation geo where geo.deviceBean=:deviceBean";
		Query query = entityManager.createQuery(queryStr);
		query.setParameter("deviceBean", device);
		query.executeUpdate();
	}
}
