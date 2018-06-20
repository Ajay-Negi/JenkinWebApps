package com.dmi.processor;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.dmi.constant.Constants;
import com.dmi.dao.IDeviceTypeDAO;
import com.dmi.dao.IServiceDAO;
import com.dmi.dao.model.DeviceType;
import com.dmi.exception.ProcessorException;

/**
 * 
 * @author ANegi
 *
 */
@Service
public class ServiceProcessor
{

	private static final Logger LOG = Logger.getLogger(ServiceProcessor.class);
	
	
	@Autowired
	IServiceDAO serviceDAO;
	@Autowired
	IDeviceTypeDAO deviceTypeDAO;
	
	public JSONArray getServices(Long deviceTypeId, Long oemId) throws ProcessorException
	{
		JSONArray allServicesArray=null;
		try
		{
			allServicesArray=new JSONArray();
			DeviceType deviceTypeBean=deviceTypeDAO.get(deviceTypeId, oemId);
			
			List<com.dmi.dao.model.Service> allServicesList = serviceDAO.get(deviceTypeBean, oemId);
			for(com.dmi.dao.model.Service service:allServicesList)
			{
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("id", service.getId());
				jsonObject.put("name", service.getName());
				allServicesArray.put(jsonObject);
			}
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
		return allServicesArray;
	}
	
	public JSONArray getServicesExceptGeolocation(Long deviceTypeId, Long oemId) throws ProcessorException
	{
		JSONArray allServicesArray=null;
		try
		{
			allServicesArray=new JSONArray();
			DeviceType deviceTypeBean=deviceTypeDAO.get(deviceTypeId, oemId);
			List<com.dmi.dao.model.Service> allServicesList = serviceDAO.get(deviceTypeBean, oemId);
			for(com.dmi.dao.model.Service service:allServicesList)
			{
				
				if(!service.getName().equalsIgnoreCase(Constants.LOCATION_SERVICE_NAME))
				{
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("id", service.getId());
					jsonObject.put("name", service.getName());
					allServicesArray.put(jsonObject);
				}
				
			}
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
		return allServicesArray;
	}


	@Transactional
	public void saveService(Long deviceTypeId, String roleCode, String serviceName, Long oemId)
			throws ProcessorException
	{
		try
		{
			// check if user is admin
			if (!roleCode.equalsIgnoreCase(Constants.USER_ROLE_CODE_ADMIN))
				throw new ProcessorException("Only Admin can create new Services.");
			
			com.dmi.dao.model.Service service=new com.dmi.dao.model.Service();
			DeviceType deviceTypeBean=deviceTypeDAO.get(deviceTypeId, oemId);
			
			com.dmi.dao.model.Service serviceBean = serviceDAO.getByServiceNameAndDeviceType(serviceName, deviceTypeBean, oemId);
			
			if(serviceBean != null)
			{
				throw new ProcessorException("Service already exist for this name");
			}
			else
			{
				
				service.setName(serviceName);
				service.setDeviceTypeBean(deviceTypeBean);
				serviceDAO.save(service);
			}
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}

	}

	@Transactional
	public void deleteService(String roleCode, Long serviceId) throws ProcessorException
	{
		try
		{
			// check if user is admin
			if (!roleCode.equalsIgnoreCase(Constants.USER_ROLE_CODE_ADMIN))
				throw new ProcessorException("Only Admin can delete Services.");
			
			serviceDAO.delete(serviceId);
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
	}

}
