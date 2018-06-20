package com.dmi.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.IDeviceRegistrationDAO;
import com.dmi.dao.model.Device;
import com.dmi.dao.model.DeviceRegistration;
import com.dmi.dao.model.Oem;
import com.dmi.dao.model.User;

@Repository
public class DeviceRegistrationDAO implements IDeviceRegistrationDAO
{

	private static final Logger LOG = Logger.getLogger(DeviceRegistrationDAO.class);

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<DeviceRegistration> getRegisteredDevices(Oem oem)
	{
		List<DeviceRegistration> devices = null;
		String queryStr = "SELECT dr FROM DeviceRegistration dr WHERE dr.deviceBean.oemBean.id=:oemId";
		TypedQuery<DeviceRegistration> query = entityManager.createQuery(queryStr, DeviceRegistration.class);
		query.setParameter("oemId", oem.getId());
		try
		{
			devices = query.getResultList();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			devices = null;
		}
		return devices;
	}

	@Override
	public List<DeviceRegistration> getRegisteredDevicesByUser(User user)
	{
		List<DeviceRegistration> registeredDevices = null;
		String queryStr = "SELECT dr FROM DeviceRegistration dr where dr.userBean.id = :userId";
		TypedQuery<DeviceRegistration> query = entityManager.createQuery(queryStr, DeviceRegistration.class);
		query.setParameter("userId", user.getId());
		
		try
		{
			registeredDevices = query.getResultList();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
		
		return registeredDevices;
	}
	
	
	@Override
	public List<DeviceRegistration> getRegisteredDevicesForAllUsers()
	{
		List<DeviceRegistration> registeredDevices = null;
		String queryStr = "SELECT device FROM DeviceRegistration device";
		TypedQuery<DeviceRegistration> query = entityManager.createQuery(queryStr, DeviceRegistration.class);
		
		try
		{
			registeredDevices = query.getResultList();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
		
		return registeredDevices;
	}

	@Override
	public void registerDevice(DeviceRegistration deviceRegistration)
	{
		entityManager.persist(deviceRegistration);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public DeviceRegistration getDeviceRegistrationById(Long registrationId)
	{
		DeviceRegistration deviceRegistration;
		String queryStr = "SELECT d FROM DeviceRegistration d where d.id=:registrationId";
		TypedQuery<DeviceRegistration> query = entityManager.createQuery(queryStr, DeviceRegistration.class);
		query.setParameter("registrationId", registrationId);
		try
		{
			deviceRegistration = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			deviceRegistration = null;
		}
		return deviceRegistration;
	}

	@Override
	public DeviceRegistration getRegisteredDevice(User user, Device device)
	{

		DeviceRegistration deviceRegistration;
		String queryStr = "SELECT d FROM DeviceRegistration d where d.deviceBean = :device and d.userBean = :user";
		TypedQuery<DeviceRegistration> query = entityManager.createQuery(queryStr, DeviceRegistration.class);
		query.setParameter("device", device);
		query.setParameter("user", user);
		try
		{
			deviceRegistration = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			deviceRegistration = null;
		}
		return deviceRegistration;
	}

	@Override
	public void updateDevice(DeviceRegistration deviceRegistration)
	{
		entityManager.merge(deviceRegistration);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public DeviceRegistration getDeviceRegistrationByDevice(Device deviceBean)
	{
		DeviceRegistration deviceRegistration;
		String queryStr = "SELECT d FROM DeviceRegistration d where d.deviceBean=:deviceBean";
		TypedQuery<DeviceRegistration> query = entityManager.createQuery(queryStr, DeviceRegistration.class);
		query.setParameter("deviceBean", deviceBean);
		try
		{
			deviceRegistration = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			deviceRegistration = null;
		}
		return deviceRegistration;
	}

	@Override
	public List<DeviceRegistration> getRegisteredDevicesByDeviceType(Long oemId, Long deviceTypeId) {
		
		List<DeviceRegistration> devices = null;
		String queryStr = "SELECT dr FROM DeviceRegistration dr WHERE dr.deviceBean.oemBean.id=:oemId AND dr.deviceBean.deviceTypeBean.id = :deviceTypeId";
		TypedQuery<DeviceRegistration> query = entityManager.createQuery(queryStr, DeviceRegistration.class);
		query.setParameter("oemId", oemId);
		query.setParameter("deviceTypeId", deviceTypeId);
		
		try
		{
			devices = query.getResultList();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			devices = null;
		}
		return devices;
	}

}
