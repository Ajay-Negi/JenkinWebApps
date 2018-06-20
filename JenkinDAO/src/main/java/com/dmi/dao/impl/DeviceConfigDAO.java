package com.dmi.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.IDeviceConfigDAO;
import com.dmi.dao.model.DeviceConfig;

@Repository
public class DeviceConfigDAO implements IDeviceConfigDAO {

	private static final Logger LOG = Logger.getLogger(DeviceConfigDAO.class);
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public DeviceConfig getByVinId(String vinId)
	{
		DeviceConfig deviceConfig;
		
		String queryStr = "SELECT dc FROM DeviceConfig dc WHERE LOWER(dc.vinBean.id) = :vinId";
		TypedQuery<DeviceConfig> query = entityManager.createQuery(queryStr, DeviceConfig.class);
		query.setParameter("vinId", vinId.toLowerCase());
		try
		{
			deviceConfig = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			deviceConfig = null;
		}
		return deviceConfig;
	}
}
