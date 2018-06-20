package com.dmi.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.IDeviceDAO;
import com.dmi.dao.model.Device;

@Repository
public class DeviceDAO implements IDeviceDAO
{
	private static final Logger LOG = Logger.getLogger(DeviceDAO.class);

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public void saveDevice(Device device)
	{
		entityManager.persist(device);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public Device get(String deviceId)
	{
		Device device;
		String queryStr = "SELECT device FROM Device device where device.id=:deviceId";
		TypedQuery<Device> query = entityManager.createQuery(queryStr, Device.class);
		query.setParameter("deviceId", deviceId);
		try
		{
			device = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			device = null;
		}
		return device;
	}
	
	@Override
	public Device getByVinId(String vinId)
	{
		Device device;
		String queryStr = "SELECT device FROM Device device where LOWER(device.vinBean.id) = :vinId";
		TypedQuery<Device> query = entityManager.createQuery(queryStr, Device.class);
		query.setParameter("vinId", vinId.toLowerCase());
		try
		{
			device = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			device = null;
		}
		return device;
	}

	@Override
	public boolean checkDeviceIdAvailability(String deviceId)
	{

		String queryStr = "SELECT device FROM Device device where device.id=:deviceId";
		TypedQuery<Device> query = entityManager.createQuery(queryStr, Device.class);
		query.setParameter("deviceId", deviceId);
		try
		{
			query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			return true;
		}
		return false;
	}

	@Override
	public boolean checkVinAvailability(String vin)
	{
		String queryStr = "SELECT device FROM Device device where device.vin=:vin";
		TypedQuery<Device> query = entityManager.createQuery(queryStr, Device.class);
		query.setParameter("vin", vin);
		try
		{
			query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			return true;
		}
		return false;
	}
	
	@Override
	public void delete(String deviceId)
	{
		Device device = entityManager.find(Device.class, deviceId);
		
		if(device != null)
			entityManager.remove(device);
		
		entityManager.flush();
		entityManager.clear();
	}
}
