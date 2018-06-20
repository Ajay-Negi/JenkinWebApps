package com.dmi.processor;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.constant.Constants;
import com.dmi.dao.IDeviceTypeDAO;
import com.dmi.dao.IMessageModelDAO;
import com.dmi.dao.IOemDAO;
import com.dmi.dao.model.DeviceType;
import com.dmi.dao.model.MessageModel;
import com.dmi.dao.model.Oem;
import com.dmi.exception.ProcessorException;


/**
 * 
 * @author Ajay Negi
 */

@Service
public class DeviceTypeProcessor
{
	
	private static final Logger LOG = Logger.getLogger(DeviceTypeProcessor.class);
	
	@Autowired
	IDeviceTypeDAO deviceTypeDAO;
	@Autowired
	IOemDAO oemDAO;
	@Autowired
	IMessageModelDAO messageModelDAO;

	public JSONArray getDeviceTypes(Long oemId)  throws ProcessorException
	{
		JSONArray allServicesArray=null;
		try
		{
			allServicesArray=new JSONArray();
			Oem oemBean=oemDAO.get(oemId);
			List<DeviceType> allDeviceTypesList = deviceTypeDAO.getAll(oemBean);
			for(DeviceType deviceType:allDeviceTypesList)
			{
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("id", deviceType.getId());
				jsonObject.put("name", deviceType.getAlias());
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
	
	public DeviceType getDeviceTypeByMessageModelId(Long messageModelId) {

		MessageModel messageModelBean = null;
		try {
			messageModelBean = messageModelDAO.getMessageModelById(messageModelId);
		} catch (DataAccessException ex) {
			LOG.error(ex.getMessage(), ex);
		}
		return messageModelBean.getDeviceTypeBean();
	}
	
	/*public DeviceType getDeviceTypeById(Long id) {

		DeviceType deviceBean = null;
		try {
			deviceBean = deviceTypeDAO.get(id);
		} catch (DataAccessException ex) {
			LOG.error(ex.getMessage(), ex);
		}
		return deviceBean;
	}*/
	
	@Transactional
	public void saveDeviceType(Long oemId, String roleCode, String deviceTypeName, String metadata, String logoUrl) throws ProcessorException
	{
		try
		{
			// check if user is admin
			if (!roleCode.equalsIgnoreCase(Constants.USER_ROLE_CODE_ADMIN))
				throw new Exception("Only Admin can create new device types.");
			
			DeviceType deviceType=new DeviceType();
			Oem oemBean=oemDAO.get(oemId);
			deviceType.setOemBean(oemBean);
			deviceType.setAlias(deviceTypeName);
			deviceType.setMetadata(metadata.getBytes());
			deviceType.setLogoUrl(logoUrl.getBytes());
			
			/*if(StringUtils.isNumeric(deviceTypeName))
				deviceType.setId(Long.parseLong(deviceTypeName));*/
			
			deviceTypeDAO.save(deviceType);
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}


	}	

	@Transactional
	public void deleteDeviceType(String roleCode, Long deviceTypeId) throws ProcessorException
	{
		try
		{
			// check if user is admin
			if (!roleCode.equalsIgnoreCase(Constants.USER_ROLE_CODE_ADMIN))
				throw new Exception("Only Admin can delete device types.");
			
			deviceTypeDAO.delete(deviceTypeId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new ProcessorException(e.getMessage());
		}

	}
	
}
