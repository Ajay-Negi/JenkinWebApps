package com.dmi.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.IDeviceTypeDAO;
import com.dmi.dao.model.DeviceType;
import com.dmi.dao.model.Oem;
import com.dmi.dao.model.User;

@Repository
public class DeviceTypeDAO implements IDeviceTypeDAO
{

	private static final Logger LOG = Logger.getLogger(DeviceTypeDAO.class);

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public DeviceType get(Long deviceTypeId, Long oemId)
	{
		DeviceType deviceType;
		String queryStr = "SELECT dt FROM DeviceType dt where dt.id=:deviceTypeId AND dt.oemBean.id=:oemId";
		TypedQuery<DeviceType> query = entityManager.createQuery(queryStr, DeviceType.class);
		query.setParameter("deviceTypeId", deviceTypeId);
		query.setParameter("oemId", oemId);
		
		try
		{
			deviceType = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			deviceType = null;
		}
		return deviceType;
	}

	@Override
	public List<DeviceType> getAll(Oem oem)
	{
		String queryStr = "SELECT dt FROM DeviceType dt where dt.oemBean=:oem";
		TypedQuery<DeviceType> query = entityManager.createQuery(queryStr, DeviceType.class);
		query.setParameter("oem", oem);
		List<DeviceType> deviceTypeList = query.getResultList();
		return deviceTypeList;
	}

	@Override
	public void delete(Long deviceTypeId)
	{
		String queryStr = "DELETE from DeviceType dt where dt.id=:id";
		Query query = entityManager.createQuery(queryStr);
		query.setParameter("id", deviceTypeId);
		query.executeUpdate();
	}

	@Override
	public void save(DeviceType deviceType)
	{
		entityManager.persist(deviceType);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DeviceType> getAll(User user)
	{
		List<DeviceType> deviceTypeList = null;
		try
		{
			String queryStr = "select DISTINCT d.deviceTypeBean.id,d.deviceTypeBean.alias from Device d JOIN d.deviceRegistration dr where dr.userBean=:user";
			Query query = entityManager.createQuery(queryStr);
			query.setParameter("user", user);
			List<Object> resultSet = query.getResultList();

			deviceTypeList = new ArrayList<>();
			for (Object object : resultSet)
			{
				Object[] row = (Object[]) object;
				DeviceType deviceType = new DeviceType();
				deviceType.setId((Long) row[0]);
				deviceType.setAlias((String) row[1]);
				deviceTypeList.add(deviceType);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return deviceTypeList;
	}

}
