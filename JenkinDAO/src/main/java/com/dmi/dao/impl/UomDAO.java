package com.dmi.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.IUomDAO;
import com.dmi.dao.model.Uom;
/**
 * 
 * @author Ajay Negi
 */
@Repository
public class UomDAO implements IUomDAO
{

	private static final Logger LOG = Logger.getLogger(UomDAO.class);

	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public Uom get(Long uomId)
	{
		Uom uom=null;
		try
		{
			String queryStr = "SELECT uom FROM Uom uom where uom.id=:uomId";
			TypedQuery<Uom> query = entityManager.createQuery(queryStr, Uom.class);
			query.setParameter("uomId", uomId);
			uom=(Uom)query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			return uom;
		}
		return uom;
	}

	@Override
	public List<Uom> getAll()
	{
		List<Uom> uomList = new ArrayList<>();
		try
		{
			String queryStr = "SELECT uom FROM Uom uom";
			TypedQuery<Uom> query = entityManager.createQuery(queryStr, Uom.class);
			uomList = query.getResultList();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			return uomList;
		}
		return uomList;
	}

	@Override
	public Uom get(String uomName)
	{
		Uom uom=null;
		try
		{
			String queryStr = "SELECT uom FROM Uom uom where uom.alias=:alias";
			TypedQuery<Uom> query = entityManager.createQuery(queryStr, Uom.class);
			query.setParameter("alias", uomName);
			uom=query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			return uom;
		}
		return uom;
	}

}
