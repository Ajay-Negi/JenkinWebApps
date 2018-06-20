package com.dmi.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.IGeofenceActionDAO;
import com.dmi.dao.model.GeofenceAction;

/**
 * 
 * @author Ajay Negi
 */

@Repository
public class GeofenceActionDAO implements IGeofenceActionDAO
{
	private static final Logger LOG = Logger.getLogger(IGeofenceActionDAO.class);

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public GeofenceAction get(Long geofenceActionId)
	{
		GeofenceAction geofenceActionBean;
		String queryStr = "SELECT gfa FROM GeofenceAction gfa where gfa.id=:geofenceActionId";
		TypedQuery<GeofenceAction> query = entityManager.createQuery(queryStr, GeofenceAction.class);
		query.setParameter("geofenceActionId", geofenceActionId);
		try
		{
			geofenceActionBean= query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			geofenceActionBean = null;
		}
		return geofenceActionBean;
	}

	@Override
	public List<GeofenceAction> getAll()
	{
		List<GeofenceAction> geofenceActionList;
		String queryStr = "SELECT gfa FROM GeofenceAction gfa";
		TypedQuery<GeofenceAction> query = entityManager.createQuery(queryStr, GeofenceAction.class);
		try
		{
			geofenceActionList=query.getResultList();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			geofenceActionList = null;
		}
		return geofenceActionList;
	}
}
