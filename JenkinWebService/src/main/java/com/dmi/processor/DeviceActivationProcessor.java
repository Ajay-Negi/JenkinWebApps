package com.dmi.processor;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.constant.Constants;
import com.dmi.dao.IDeviceDAO;
import com.dmi.dao.IDeviceRegistrationDAO;
import com.dmi.dao.IDeviceStatusDAO;
import com.dmi.dao.model.Device;
import com.dmi.dao.model.DeviceRegistration;
import com.dmi.dao.model.DeviceStatus;
import com.dmi.exception.ProcessorException;

/**
 * 
 * @author ANegi
 *
 */
@Service
public class DeviceActivationProcessor
{
	private static final Logger LOG = Logger.getLogger(DeviceActivationProcessor.class);
	
	@Autowired
	IDeviceDAO deviceDAO;
	@Autowired
	IDeviceRegistrationDAO deviceRegistarationDAO;
	@Autowired
	IDeviceStatusDAO deviceStatusDAO;
	
	public JSONObject checkStatus(Long oemId, String deviceId) throws ProcessorException
	{
		try
		{
			Device device = deviceDAO.get(deviceId);
			if(device==null)
				throw new ProcessorException("Device not Found!");
			
			DeviceRegistration deviceRegistration = deviceRegistarationDAO.getDeviceRegistrationByDevice(device);
			if(deviceRegistration==null)
				throw new ProcessorException("Device not Registered!");

			JSONObject response = new JSONObject();
			response.put("oemId", oemId);
			response.put("deviceId", deviceId);
			response.put("status", deviceRegistration.getDeviceStatusBean().getAlias());
			return response;
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
	}
	
	@Transactional
	public JSONObject statusChangeToRegistered(Long oemId, String deviceId) throws ProcessorException
	{
		try
		{
			Device device = deviceDAO.get(deviceId);
			
			if(device==null)
				throw new ProcessorException("Device not Found!");
			
			DeviceRegistration deviceRegistration = deviceRegistarationDAO.getDeviceRegistrationByDevice(device);
			DeviceStatus deviceStatusBean = deviceStatusDAO
					.get(Constants.DEVICE_STATUS_ALIAS_REGISTERED);
			deviceRegistration.setDeviceStatusBean(deviceStatusBean);
			deviceRegistarationDAO.updateDevice(deviceRegistration);

			JSONObject response= new JSONObject();
			response.put("oemId", oemId);
			response.put("deviceId", deviceId);
			response.put("status", Constants.DEVICE_STATUS_ALIAS_REGISTERED);
			return response;
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
	}

	@Transactional
	public JSONObject statusChangeToUnderActivation(Long oemId, String deviceId) throws ProcessorException
	{
		try
		{
			Device device = deviceDAO.get(deviceId);
			
			if(device==null)
				throw new ProcessorException("Device not Found!");
			
			DeviceRegistration deviceRegistration = deviceRegistarationDAO.getDeviceRegistrationByDevice(device);
			DeviceStatus deviceStatusBean = deviceStatusDAO
					.get(Constants.DEVICE_STATUS_ALIAS_UNDER_ACTIVATION);
			deviceRegistration.setDeviceStatusBean(deviceStatusBean);
			deviceRegistarationDAO.updateDevice(deviceRegistration);

			JSONObject response= new JSONObject();
			response.put("oemId", oemId);
			response.put("deviceId", deviceId);
			response.put("status", Constants.DEVICE_STATUS_ALIAS_UNDER_ACTIVATION);
			return response;
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
	}

	@Transactional
	public JSONObject statusChangeToActivated(Long oemId, String deviceId) throws ProcessorException
	{
		try
		{
			Device device = deviceDAO.get(deviceId);
			
			if(device==null)
				throw new ProcessorException("Device not Found!");
			
			DeviceRegistration deviceRegistration = deviceRegistarationDAO.getDeviceRegistrationByDevice(device);
			DeviceStatus deviceStatusBean = deviceStatusDAO
					.get(Constants.DEVICE_STATUS_ALIAS_ACTIVATED);
			deviceRegistration.setDeviceStatusBean(deviceStatusBean);
			deviceRegistarationDAO.updateDevice(deviceRegistration);

			JSONObject response = new JSONObject();
			response.put("oemId", oemId);
			response.put("deviceId", deviceId);
			response.put("status", Constants.DEVICE_STATUS_ALIAS_ACTIVATED);
			return response;
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
	}

}
