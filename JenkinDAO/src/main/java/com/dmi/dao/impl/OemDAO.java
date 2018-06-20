package com.dmi.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.IOemDAO;
import com.dmi.dao.model.DeviceType;
import com.dmi.dao.model.Oem;


@Repository
public class OemDAO implements IOemDAO
{

	private static final Logger LOG = Logger.getLogger(OemDAO.class);

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public boolean checkOemAvailability(Long oemId)
	{
		try
		{
			String queryStr = "SELECT oem FROM Oem oem WHERE oem.id=:oemId";
			TypedQuery<Oem> query = entityManager.createQuery(queryStr, Oem.class);
			query.setParameter("oemId", oemId);
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
	public List<Oem> getAll()
	{
		List<Oem> oemList = null;
		try
		{
			String queryStr = "SELECT oem FROM Oem oem";
			TypedQuery<Oem> query = entityManager.createQuery(queryStr, Oem.class);
	
			oemList = query.getResultList();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
		return oemList;
	}

	@Override
	public Oem get(Long oemId)
	{
		Oem oem =null;
		try
		{
			String queryStr = "SELECT oem FROM Oem oem WHERE oem.id=:oemId";
			TypedQuery<Oem> query = entityManager.createQuery(queryStr, Oem.class);
			query.setParameter("oemId", oemId);
			oem = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			return oem;
		}
		return oem;
	}
	
	@Override
	public Oem getByName(String name)
	{
		Oem oem = null;
		try
		{
			String queryStr = "SELECT oem FROM Oem oem WHERE LOWER(oem.name) = :name";
			TypedQuery<Oem> query = entityManager.createQuery(queryStr, Oem.class);
			query.setParameter("name", name.toLowerCase());
			oem = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			return oem;
		}
		return oem;
	}

	@Override
	public void save(Oem oemBean)
	{

		entityManager.persist(oemBean);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public void delete(Long oemId)
	{
		String queryStr = "DELETE FROM Oem oem WHERE oem.id=:oemId";
		Query query = entityManager.createQuery(queryStr);
		query.setParameter("oemId", oemId);
		query.executeUpdate();
		
	}

	@Override
	public void update(Oem oemBean) {
		
		entityManager.merge(oemBean);
		entityManager.flush();
		entityManager.clear();
		
	}

	
	@Override
	public List<Oem> getOemsForChatBot(long chatBotId) {
		
		String queryStr = "SELECT oem FROM Oem oem WHERE oem.chatEngineBean.id = :id";
		TypedQuery<Oem> query = entityManager.createQuery(queryStr, Oem.class);
		query.setParameter("id", chatBotId);
		
		List<Oem> oems = null;
		
		try{
			
			oems = query.getResultList();
			
		}
		catch(Exception ex) {
			
			LOG.error(ex);

		}
		
		return oems;
	}

	@Override
	public List<DeviceType> getDeviceTypesByOem(Long oemId) {
		
		List<DeviceType> deviceTypeList = null;
		try
		{
			String queryStr = "SELECT dt FROM DeviceType dt WHERE dt.oemBean.id = :oemId";
			TypedQuery<DeviceType> query = entityManager.createQuery(queryStr, DeviceType.class);
			query.setParameter("oemId", oemId);
			
			deviceTypeList = query.getResultList();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
		return deviceTypeList;
	}

	/*@Override
	public void toggleLocationServices(String status, Long oemId) {
		
		Oem oem = entityManager.find(Oem.class, oemId);
		oem.setLocationServicesStatus(status);
		
		
		try{
			
			entityManager.merge(oem);
		}
		catch(Exception ex) {
			
			LOG.error(ex);

		}
		
	
	}*/

}
