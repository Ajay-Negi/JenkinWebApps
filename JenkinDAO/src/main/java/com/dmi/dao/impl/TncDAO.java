package com.dmi.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.ITncDAO;
import com.dmi.dao.model.Oem;
import com.dmi.dao.model.Tnc;


/**
 * 
 * @author Mukul Bansal
 */
@Repository
public class TncDAO implements ITncDAO
{
	private static final Logger LOG = Logger.getLogger(TncDAO.class);

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public Tnc getTncFromId(Long id)
	{
		Tnc tnc = null;
		try
		{
			String queryStr = "SELECT tnc FROM Tnc tnc where tnc.id=:id";
			Query query = entityManager.createQuery(queryStr);
			query.setParameter("id", id);
			tnc = (Tnc) query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.info(ex.getMessage());
			tnc = null;
		}
		return tnc;
	}

	@Override
	public Tnc getLatestTnc(Oem oem)
	{
		Tnc tnc = null;
		try
		{
			String queryStr = "SELECT tnc FROM Tnc tnc WHERE tnc.version = (select max(tnc1.version) FROM Tnc tnc1 WHERE tnc1.oemBean = :oem) AND tnc.oemBean=:oem";
			Query query = entityManager.createQuery(queryStr);
			query.setParameter("oem", oem);
			tnc = (Tnc) query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			tnc = null;
		}
		return tnc;
	}

	@Override
	public void save(Tnc tnc)
	{
		entityManager.persist(tnc);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public boolean checkTnc(Oem oem, Float tncVersion)
	{
		try
		{
			String queryStr = "SELECT tnc FROM Tnc tnc where tnc.oemBean = :oem AND tnc.version=:version";
			Query query = entityManager.createQuery(queryStr);
			query.setParameter("oem", oem);
			query.setParameter("version", tncVersion);
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
	public List<Tnc> get(Oem oem)
	{

		List<Tnc> tncList = null;
		try
		{
			String queryStr = "SELECT tnc FROM Tnc tnc where tnc.oemBean=:oem";
			Query query = entityManager.createQuery(queryStr);
			query.setParameter("oem", oem);
			tncList = query.getResultList();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			tncList = null;
		}
		return tncList;
	}

	@Override
	public void update(Tnc tnc)
	{
		entityManager.merge(tnc);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public void delete(Long tncId)
	{
		try
		{
			String queryStr = "DELETE FROM Tnc tnc where tnc.id=:id";
			Query query = entityManager.createQuery(queryStr);
			query.setParameter("id", tncId);
			query.executeUpdate();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
	}

}
