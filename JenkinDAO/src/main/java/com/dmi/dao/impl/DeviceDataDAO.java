package com.dmi.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.IDeviceDataDAO;
import com.dmi.dao.model.DeviceData;

@Repository
public class DeviceDataDAO implements IDeviceDataDAO{

	@PersistenceContext
	EntityManager entityManager;
	
	private static final Logger LOG = Logger.getLogger(DeviceDataDAO.class);
	
	@Override
	public DeviceData getByDeviceId(String deviceId) {

		DeviceData deviceData = null;
		
		try{
			String queryStr = "SELECT dd FROM DeviceData dd WHERE LOWER(dd.device.id) = :deviceId ";
			TypedQuery<DeviceData> query = entityManager.createQuery(queryStr, DeviceData.class);
			query.setParameter("deviceId", deviceId.toLowerCase());
			
			deviceData = query.getSingleResult();
		}catch(Exception ex){
			LOG.error(ex.getMessage(), ex);
		}
		
		return deviceData;
	}

}
