package com.dmi.processor;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.constant.Constants;
import com.dmi.dao.IServiceDAO;
import com.dmi.dao.ISubServiceDAO;
import com.dmi.dao.model.SubService;
import com.dmi.exception.ProcessorException;


/**
 * 
 * @author ANegi
 *
 */
@Service
public class SubServiceProcessor
{
	
	private static final Logger LOG = Logger.getLogger(SubServiceProcessor.class);
	
	@Autowired
	ISubServiceDAO subServiceDAO;
	@Autowired
	IServiceDAO seviceDAO;

	public JSONArray getSubServices(Long serviceId) throws ProcessorException
	{
		JSONArray allSubServicesArray=null;
		try
		{
			allSubServicesArray=new JSONArray();
			com.dmi.dao.model.Service serviceBean = seviceDAO.get(serviceId);
			List<SubService> allSubServicesList = subServiceDAO.get(serviceBean);
			for(SubService subService:allSubServicesList)
			{
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("id", subService.getId());
				jsonObject.put("name", subService.getName());
				allSubServicesArray.put(jsonObject);
			}
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
		return allSubServicesArray;
	}

	@Transactional
	public void saveSubService(Long serviceId, String roleCode, String subServiceName)
			throws ProcessorException
	{
		try
		{
			// check if user is admin
			if (!roleCode.equalsIgnoreCase(Constants.USER_ROLE_CODE_ADMIN))
				throw new ProcessorException("Only Admin can create new Sub-Services.");
			
			SubService subService=new SubService();
			com.dmi.dao.model.Service serviceBean = seviceDAO.get(serviceId);
			
			SubService subServiceBean = subServiceDAO.get(serviceId, subServiceName);
			if(subServiceBean!=null)
			{
				throw new ProcessorException("Subservice already exist with this name");
				
			}
			else
			{
				
				subService.setName(subServiceName);
				subService.setServiceBean(serviceBean);
				subServiceDAO.save(subService);
			}
			
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}

	}

	@Transactional
	public void deleteSubService(String roleCode, Long subServiceId) throws ProcessorException
	{
		try
		{
			// check if user is admin
			if (!roleCode.equalsIgnoreCase(Constants.USER_ROLE_CODE_ADMIN))
				throw new Exception("Only Admin can delete Sub-Services.");

			subServiceDAO.delete(subServiceId);
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}

	}

}
