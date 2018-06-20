package com.dmi.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.ISpeedDAO;
import com.dmi.dao.model.Device;
import com.dmi.dao.model.Speed;

/**
 * 
 * @author ANegi
 *
 */
@Repository
public class SpeedDAO implements ISpeedDAO
{
	private static final Logger LOG = Logger.getLogger(SpeedDAO.class);

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public Speed get(Device device)
	{
		Speed speed = null;
		try
		{
			String queryStr = "SELECT s FROM Speed s where s.deviceBean=:deviceBean";
			Query query = entityManager.createQuery(queryStr);
			query.setParameter("deviceBean", device);
			speed = (Speed) query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			speed = null;
		}
		return speed;
	}

	@Override
	public void save(Speed speed)
	{
		entityManager.persist(speed);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public void update(Speed speed)
	{
		entityManager.merge(speed);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public void delete(Device device)
	{
		String queryStr = "DELETE from Speed s where s.deviceBean=:deviceBean";
		Query query = entityManager.createQuery(queryStr);
		query.setParameter("deviceBean", device);
		query.executeUpdate();
	}
}
