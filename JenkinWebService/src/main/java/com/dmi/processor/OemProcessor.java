package com.dmi.processor;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.constant.Constants;
import com.dmi.dao.IOemClassificationDAO;
import com.dmi.dao.IOemDAO;
import com.dmi.dao.model.Oem;
import com.dmi.dao.model.OemClassification;
import com.dmi.exception.ProcessorException;

@Service
public class OemProcessor
{
	@Autowired
	IOemDAO oemDAO;
	@Autowired
	IOemClassificationDAO oemClassificationDAO;
	
	private static final Logger LOG = Logger.getLogger(OemProcessor.class);

	public void checkOemAvailbility(Long oemId) throws ProcessorException
	{
		if (!oemDAO.checkOemAvailability(oemId))
			throw new ProcessorException("No such OEM found!!");
	}

	public JSONArray getAllOem()
	{
		List<Oem> oemList = oemDAO.getAll();
		JSONArray oemArray = new JSONArray();
		for (Oem oem : oemList)
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", oem.getId());
			jsonObject.put("name", oem.getName());
			oemArray.put(jsonObject);
		}
		return oemArray;
	}
	
	public JSONObject getOemByName(String oemName)
	{
		Oem oem = oemDAO.getByName(oemName);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", oem.getId());
		jsonObject.put("name", oem.getName());
	
		return jsonObject;

	}

	@Transactional
	public JSONArray saveOem(String logoUrl, String oemName,
			Long oemClassificationId,String locationServicesStatus, String backgroundImageURL) throws ProcessorException
	{
		try
		{

			Oem checkBean = oemDAO.getByName(oemName);
			
			if(checkBean!=null)
				throw new ProcessorException("oem name already exist");
				
			Oem oemBean = new Oem();
			OemClassification oemClassificationBean = oemClassificationDAO.get(oemClassificationId);
			
			if (logoUrl != null)
				oemBean.setLogoUrl(logoUrl.getBytes());
			
			if (backgroundImageURL != null)
				oemBean.setBackGroundImageUrl(backgroundImageURL.getBytes());
			
			oemBean.setName(oemName);
			oemBean.setOemClassificationBean(oemClassificationBean);
			
			
			if( ("enabled".equalsIgnoreCase(locationServicesStatus)) || ("disabled".equalsIgnoreCase(locationServicesStatus) ))
				oemBean.setLocationServicesStatus(locationServicesStatus);
			else 
				oemBean.setLocationServicesStatus("disabled");
			
			oemDAO.save(oemBean);
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
		return null;
	}

	@Transactional
	public void deleteOem(String roleCode, Long oemId) throws ProcessorException
	{
		try
		{
			// check if user is admin
			if (!roleCode.equalsIgnoreCase(Constants.USER_ROLE_CODE_SUPERADMIN))
				throw new ProcessorException("Only Admin can delete Services.");

			Oem oem = oemDAO.get(oemId);
			if(oem == null)
				throw new ProcessorException("No oem exist for this id");
			
			oemDAO.delete(oemId);
		}
		catch (ProcessorException pe)
		{
			LOG.error(pe.getMessage(), pe);
			throw new ProcessorException(pe.getMessage());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
	}

	/*@Transactional
	public void toggleLocationServices(String status, long oemId) throws ProcessorException{
			
		try
		{
			
			oemDAO.toggleLocationServices(status, oemId);
					
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new ProcessorException(e.getMessage());
		}
		
	}*/
}
