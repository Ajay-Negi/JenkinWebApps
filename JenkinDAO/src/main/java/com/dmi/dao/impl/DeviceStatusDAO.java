package com.dmi.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.IDeviceStatusDAO;
import com.dmi.dao.model.DeviceStatus;

@Repository
public class DeviceStatusDAO implements IDeviceStatusDAO
{
	private static final Logger LOG = Logger.getLogger(DeviceStatusDAO.class);

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public DeviceStatus get(String alias)
	{
		DeviceStatus deviceStatus;
		String queryStr = "SELECT ds FROM DeviceStatus ds where LOWER(ds.alias) = :alias";
		TypedQuery<DeviceStatus> query = entityManager.createQuery(queryStr, DeviceStatus.class);
		query.setParameter("alias", alias.toLowerCase());
		try
		{
			deviceStatus=query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage());
			deviceStatus = null;
		}
		return deviceStatus;
	}

	@Override
	public DeviceStatus get(Long deviceStatusId)
	{
		DeviceStatus deviceStatus;
		String queryStr = "SELECT ds FROM DeviceStatus ds where ds.id=:deviceStatusId";
		TypedQuery<DeviceStatus> query = entityManager.createQuery(queryStr, DeviceStatus.class);
		query.setParameter("deviceStatusId", deviceStatusId);
		try
		{
			deviceStatus= query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.info(ex.getMessage());
			deviceStatus = null;
		}
		return deviceStatus;
	}

	@Override
	public List<DeviceStatus> getAll()
	{
		TypedQuery<DeviceStatus> query = entityManager.createNamedQuery("DeviceStatus.findAll", DeviceStatus.class);
		List<DeviceStatus> deviceStatusList = query.getResultList();
		return deviceStatusList;
	}
}
