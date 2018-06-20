package com.dmi.processor;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.constant.Constants;
import com.dmi.controller.TncController;
import com.dmi.dao.IOemDAO;
import com.dmi.dao.ITncDAO;
import com.dmi.dao.IUserDAO;
import com.dmi.dao.model.Oem;
import com.dmi.dao.model.Tnc;
import com.dmi.dao.model.User;
import com.dmi.dto.TncDTO;
import com.dmi.exception.ProcessorException;
import com.esotericsoftware.minlog.Log;



/**
 * 
 * @author Mukul Bansal
 */

@Service
public class TncProcessor
{

	private static final Logger LOG = Logger.getLogger(TncProcessor.class);
	
	@Autowired
	ITncDAO tncDAO;
	@Autowired
	IUserDAO userDAO;
	@Autowired
	IOemDAO oemDAO;

	public JSONObject getLatestTnc(Long oemId) throws ProcessorException
	{
		JSONObject latestTnc = new JSONObject();
		try
		{
			Tnc tnc = tncDAO.getLatestTnc(oemDAO.get(oemId));
			if (tnc == null)
				throw new ProcessorException("No Terms and Conditions found.");
			latestTnc.put("Id", tnc.getId());
			latestTnc.put("Version", tnc.getVersion());
			latestTnc.put("Agreement", new String(tnc.getAgreement()));
			latestTnc.put("Effectuation Date", tnc.getEffectuationDate());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
		return latestTnc;
	}

	@Transactional
	public void save(String username, Long oemId, Float tncVersion, String agreement)
			throws ProcessorException
	{
		try
		{
			Oem oem = oemDAO.get(oemId);
			User user = userDAO.getUser(username);
			// check if user is admin
			if (!user.getRoleBean().getAlias()
					.equalsIgnoreCase(Constants.USER_ROLE_CODE_ADMIN))
				throw new ProcessorException("Only Admin can add new Terms And Conditions Agreement.");

			// check if tnc version already exists
			if (tncDAO.checkTnc(oem, tncVersion))
				throw new ProcessorException("Tnc Version Alredy Exists.");
			Tnc tnc = new Tnc();
			tnc.setAgreement(agreement.getBytes());
			tnc.setVersion(tncVersion);
			tnc.setOemBean(oem);
			tnc.setEffectuationDate(new Date());
			tncDAO.save(tnc);
		
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
	}

	public JSONArray get(Long oemId) throws ProcessorException
	{
		JSONArray allTncs;
		try
		{
			allTncs = new JSONArray();
			Oem oem = oemDAO.get(oemId);

			List<Tnc> tncList = tncDAO.get(oem);
			for (Tnc tnc : tncList)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("agreement", new String(tnc.getAgreement()));
				jsonObject.put("id", tnc.getId());
				jsonObject.put("version", tnc.getVersion());
				jsonObject.put("effectuationDate", tnc.getEffectuationDate());

				allTncs.put(jsonObject);
			}

		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
		return allTncs;

	}

	@Transactional
	public void update(String username, Long oemId, TncDTO tncDTO) throws ProcessorException
	{
		try
		{
			// check if user is admin
			if (!userDAO.getUser(username).getRoleBean().getAlias()
					.equalsIgnoreCase(Constants.USER_ROLE_CODE_ADMIN))
				throw new ProcessorException("Only Admin can update Terms And Conditions Agreement.");
			
			Oem oem = oemDAO.get(oemId);
			
			Tnc tnc = tncDAO.getTncFromId(tncDTO.getId());
			tnc.setAgreement(tncDTO.getAgreement().getBytes());
			tnc.setOemBean(oem);
			tnc.setEffectuationDate(new Date());
			tncDAO.update(tnc);
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
	}
	
	@Transactional
	public void delete(String username, Long tncId) throws ProcessorException
	{
		try
		{
			// check if user is admin
			if (!userDAO.getUser(username).getRoleBean().getAlias()
					.equalsIgnoreCase(Constants.USER_ROLE_CODE_ADMIN))
				throw new ProcessorException("Only Admin can delete Terms And Conditions Agreement.");
			
			tncDAO.delete(tncId);
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
	}

}
