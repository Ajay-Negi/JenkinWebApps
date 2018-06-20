package com.dmi.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.constant.Constants;
import com.dmi.dao.IAlertDAO;
import com.dmi.dao.IDeviceDAO;
import com.dmi.dao.IDeviceRegistrationDAO;
import com.dmi.dao.IDeviceStatusDAO;
import com.dmi.dao.IDeviceTypeDAO;
import com.dmi.dao.IOemDAO;
import com.dmi.dao.IRedisDAO;
import com.dmi.dao.IServiceSubscriptionDAO;
import com.dmi.dao.IUserDAO;
import com.dmi.dao.IVinDAO;
import com.dmi.dao.model.Device;
import com.dmi.dao.model.DeviceRegistration;
import com.dmi.dao.model.DeviceStatus;
import com.dmi.dao.model.DeviceType;
import com.dmi.dao.model.Oem;
import com.dmi.dao.model.User;
import com.dmi.dao.model.Vin;
import com.dmi.dto.DeviceDTO;
import com.dmi.exception.ProcessorException;

/**
 * 
 * @author Ajay Negi
 *
 */

@Service
public class DeviceOnboardingProcessor
{

	private static final Logger LOG = Logger.getLogger(DeviceOnboardingProcessor.class);
	
	@Autowired
	IDeviceRegistrationDAO deviceRegistartionDAO;
	@Autowired
	IDeviceDAO deviceDAO;
	@Autowired
	IDeviceStatusDAO deviceStatusDAO;
	@Autowired
	IUserDAO userDAO;
	@Autowired
	IDeviceTypeDAO deviceTypeDAO;
	@Autowired
	IAlertDAO alertDAO;
	@Autowired
	IServiceSubscriptionDAO serviceSubscriptionDAO;
	@Autowired
	IVinDAO vinDAO;
	@Autowired
	IOemDAO oemDAO;
	@Autowired
	IRedisDAO redisDAO;

	public JSONObject getRegisteredDeviceById(String deviceId) throws ProcessorException
	{
		Device device = deviceDAO.get(deviceId);
		if (device == null)
			throw new ProcessorException("No device found with given deviceId.");

		DeviceRegistration deviceRegistration = deviceRegistartionDAO.getDeviceRegistrationByDevice(device);

		JSONObject deviceDetails = new JSONObject();

		JSONObject deviceOwner = new JSONObject();
		deviceOwner.put("id", deviceRegistration.getUserBean().getId());
		deviceOwner.put("name",
				deviceRegistration.getUserBean().getFirstName() + " " + deviceRegistration.getUserBean().getLastName());
		
		/**Get number of device alerts for the device Id **/
		long numberOfAlerts = alertDAO.getAlertsCount(deviceId);

		deviceDetails.put("deviceId", deviceRegistration.getDeviceBean().getId());
		deviceDetails.put("deviceOwner", deviceOwner);
		deviceDetails.put("oemId", deviceRegistration.getDeviceBean().getOemBean().getId());
		deviceDetails.put("oemName", deviceRegistration.getDeviceBean().getOemBean().getName());
		deviceDetails.put("deviceTypeId", deviceRegistration.getDeviceBean().getDeviceTypeBean().getId());
		deviceDetails.put("deviceTypeAlias", deviceRegistration.getDeviceBean().getDeviceTypeBean().getAlias());
		deviceDetails.put("deviceMetadata",new JSONObject(deviceRegistration.getDeviceBean().getDeviceTypeBean().getMetadata()));
		deviceDetails.put("numberOfAlerts", numberOfAlerts);

		return deviceDetails;
	}

	public JSONArray getRegisteredDevicesForUser(String username)
	{
		User user = userDAO.getUser(username);
		List<DeviceRegistration> registeredDevices = deviceRegistartionDAO.getRegisteredDevicesByUser(user);

		JSONArray registeredDevicesArray = new JSONArray();
		for (DeviceRegistration deviceRegistration : registeredDevices)
		{
			JSONObject jsonObject = new JSONObject();

			/**Get number of device alerts for the device Id **/
			long numberOfAlerts = alertDAO.getAlertsCount(deviceRegistration.getDeviceBean().getId());
			
			jsonObject.put("registrationId", deviceRegistration.getId());
			jsonObject.put("deviceId", deviceRegistration.getDeviceBean().getId());
			jsonObject.put("alias", deviceRegistration.getDeviceBean().getAlias());
			jsonObject.put("deviceStatus", deviceRegistration.getDeviceStatusBean().getAlias());
			jsonObject.put("deviceTypeId", deviceRegistration.getDeviceBean().getDeviceTypeBean().getId());
			jsonObject.put("deviceTypeName", deviceRegistration.getDeviceBean().getDeviceTypeBean().getAlias());
			jsonObject.put("deviceMetadata", new JSONObject(deviceRegistration.getDeviceBean().getDeviceTypeBean().getMetadata()));
			jsonObject.put("numberOfAlerts", numberOfAlerts);
			jsonObject.put("deviceLogoUrl", new String(deviceRegistration.getDeviceBean().getLogoUrl()));
			
			registeredDevicesArray.put(jsonObject);
		}

		return registeredDevicesArray;
	}
	
	
	public JSONArray getRegisteredDevicesForOem(Long oemId)
	{
		Oem oem = oemDAO.get(oemId);
		List<DeviceRegistration> registeredDevices = deviceRegistartionDAO.getRegisteredDevices(oem);

		JSONArray registeredDevicesArray = new JSONArray();
		for (DeviceRegistration deviceRegistration : registeredDevices)
		{
			JSONObject jsonObject = new JSONObject();

			/**Get number of device alerts for the device Id **/
			long numberOfAlerts = alertDAO.getAlertsCount(deviceRegistration.getDeviceBean().getId());
			
			jsonObject.put("registrationId", deviceRegistration.getId());
			jsonObject.put("deviceId", deviceRegistration.getDeviceBean().getId());
			jsonObject.put("vin", deviceRegistration.getDeviceBean().getVinBean().getId());
			jsonObject.put("alias", deviceRegistration.getDeviceBean().getAlias());
			jsonObject.put("deviceStatus", deviceRegistration.getDeviceStatusBean().getAlias());
			jsonObject.put("deviceTypeId", deviceRegistration.getDeviceBean().getDeviceTypeBean().getId());
			jsonObject.put("deviceTypeName", deviceRegistration.getDeviceBean().getDeviceTypeBean().getAlias());
			jsonObject.put("deviceMetadata", new JSONObject(deviceRegistration.getDeviceBean().getDeviceTypeBean().getMetadata()));
			jsonObject.put("numberOfAlerts", numberOfAlerts);
			jsonObject.put("deviceLogoUrl", new String(deviceRegistration.getDeviceBean().getLogoUrl()));
			
			registeredDevicesArray.put(jsonObject);
		}

		return registeredDevicesArray;
	}
	
	public JSONArray getRegisteredDevicesForOemByDeviceTypeId(Long oemId, Long deviceTypeId)
	{
		List<DeviceRegistration> registeredDevices = deviceRegistartionDAO.getRegisteredDevicesByDeviceType(oemId, deviceTypeId);

		JSONArray registeredDevicesArray = new JSONArray();
		for (DeviceRegistration deviceRegistration : registeredDevices)
		{
			JSONObject jsonObject = new JSONObject();

			/**Get number of device alerts for the device Id **/
			long numberOfAlerts = alertDAO.getAlertsCount(deviceRegistration.getDeviceBean().getId());
			
			jsonObject.put("registrationId", deviceRegistration.getId());
			jsonObject.put("deviceId", deviceRegistration.getDeviceBean().getId());
			jsonObject.put("vin", deviceRegistration.getDeviceBean().getVinBean().getId());
			jsonObject.put("alias", deviceRegistration.getDeviceBean().getAlias());
			jsonObject.put("deviceStatus", deviceRegistration.getDeviceStatusBean().getAlias());
			jsonObject.put("deviceTypeId", deviceRegistration.getDeviceBean().getDeviceTypeBean().getId());
			jsonObject.put("deviceTypeName", deviceRegistration.getDeviceBean().getDeviceTypeBean().getAlias());
			jsonObject.put("deviceMetadata", new JSONObject(deviceRegistration.getDeviceBean().getDeviceTypeBean().getMetadata()));
			jsonObject.put("numberOfAlerts", numberOfAlerts);
			jsonObject.put("deviceLogoUrl", new String(deviceRegistration.getDeviceBean().getLogoUrl()));
			
			registeredDevicesArray.put(jsonObject);
		}

		return registeredDevicesArray;
	}
	
	public JSONArray getAllRegisteredDevicesForAllUsers()
	{
		List<DeviceRegistration> registeredDevices = deviceRegistartionDAO.getRegisteredDevicesForAllUsers();

		JSONArray registeredDevicesArray = new JSONArray();
		
		for (DeviceRegistration deviceRegistration : registeredDevices)
		{
			JSONObject jsonObject = new JSONObject();

			
			jsonObject.put("deviceId", deviceRegistration.getDeviceBean().getId());
			jsonObject.put("deviceAlias", deviceRegistration.getDeviceBean().getAlias());
			jsonObject.put("userId", deviceRegistration.getUserBean().getId());
		
		
			registeredDevicesArray.put(jsonObject);
		}

		return registeredDevicesArray;
	}
	
	public JSONArray getDeviceUserMapping(String oemName)
	{
		
		Oem oemBean = oemDAO.getByName(oemName);
		
		List<User> userList = userDAO.getAll(oemBean);
		
		List<DeviceRegistration> registeredDevices = deviceRegistartionDAO.getRegisteredDevices(oemBean);

		JSONArray deviceUserArray = new JSONArray();
		List<String> adminDeviceList = new ArrayList<>();
		String adminUserName = null;
		
		//getting all devices in the admin device list
		for(DeviceRegistration deviceRegistration : registeredDevices)
		{
			
			adminDeviceList.add(deviceRegistration.getDeviceBean().getId());
		}
		
		//getting device as per registered against a normal user
		for(User user : userList)
		{
			JSONObject jsonObject = new JSONObject();
			List<String> deviceList = new ArrayList<>();
			
			for(DeviceRegistration deviceRegistration : registeredDevices)
			{
				
				if(user.getId() == deviceRegistration.getUserBean().getId())
				{
					deviceList.add(deviceRegistration.getDeviceBean().getId());
					//adminDeviceList.add(deviceRegistration.getDeviceBean().getId());
				}
			}
			
			if(!user.getRoleBean().getAlias().equalsIgnoreCase("admin"))
			{
				jsonObject.put(user.getUsername(), deviceList);
				deviceUserArray.put(jsonObject);
			}else if(user.getRoleBean().getAlias().equalsIgnoreCase("admin"))
			{
				adminUserName = user.getUsername();
				deviceUserArray.put(new JSONObject().put(adminUserName, adminDeviceList));
			}
				
		}
		
		
		
		return deviceUserArray;
	}

	@Transactional(rollbackFor = Exception.class)
	public void registerDevice(String username, String deviceAlias, Long deviceTypeId, Long oemId, String vin,
			String imei, String deviceLogoUrl) throws ProcessorException
	{

		// CHECK IF VIN IS UNIQUE
		if (!vinDAO.checkVinAvailability(vin))
			throw new ProcessorException("VIN already exists.");

		// CHECK IF DEVICE ALIAS IS UNIQUE FOR THE LOGGED IN USER
		List<DeviceRegistration> deviceRegistrationList = deviceRegistartionDAO.getRegisteredDevicesByUser(userDAO.getUser(username));
		for (DeviceRegistration registration : deviceRegistrationList)
		{
			if (registration.getDeviceBean().getAlias().equalsIgnoreCase(deviceAlias))
			{
				throw new ProcessorException("Provided Friendly Name already Exists for one of your devices.");
			}
		}

		// SAVE DEVICE INFO
		Device device = new Device();
		
		String deviceId = imei;
		
		device.setId(deviceId);
		device.setAlias(deviceAlias);		
		
		if (!vin.isEmpty()){
			Vin vinBean = new Vin();
			vinBean.setId(vin); //vinDAO.getByVinId(vin);
			device.setVinBean(vinBean); //implement as per discussion with Agarwal
		}
		else{
			throw new ProcessorException("vin cannot be null");
		}
			
		
		DeviceType deviceTypeBean = deviceTypeDAO.get(deviceTypeId, oemId);
		device.setDeviceTypeBean(deviceTypeBean);
		
		if(deviceLogoUrl != null)
		{
			device.setLogoUrl(deviceLogoUrl.getBytes());
		}
		else{
			device.setLogoUrl(deviceTypeBean.getLogoUrl());
		}

		Oem oemBean = oemDAO.get(oemId);
		device.setOemBean(oemBean);

		try
		{
			deviceDAO.saveDevice(device);
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			throw new ProcessorException("Error Saving Device....Please try Again....");
		}

		// REGISTER DEVICE
		DeviceRegistration deviceRegistration = new DeviceRegistration();

		User userBean = userDAO.getUser(username);
		deviceRegistration.setUserBean(userBean);

		DeviceStatus deviceStatusBean = deviceStatusDAO.get(Constants.DEVICE_STATUS_ALIAS_REGISTERED);
		deviceRegistration.setDeviceStatusBean(deviceStatusBean);

		deviceRegistration.setDeviceBean(deviceDAO.get(deviceId));

		deviceRegistartionDAO.registerDevice(deviceRegistration);
		
		//save the  deviceId key mapping with customerId_vin value
		Map<String, String> hmap = new HashMap<String, String>();
		hmap.put(deviceRegistration.getDeviceBean().getId(), userBean.getId() + "_" + vin);
		redisDAO.save(Constants.REDIS_IOT_DMI_CUSTOMERID, hmap);
		
		
		
		
	}

	@Transactional
	public void updateDevice(String username, DeviceDTO deviceDTO) throws ProcessorException
	{
		Long registrationId = deviceDTO.getRegistrationId();
		String deviceId = deviceDTO.getDeviceId();
		String alias = deviceDTO.getAlias();
		Long deviceStatusId = deviceDTO.getDeviceStatusId();

		// UPADTE REGISTERED DEVICE
		DeviceRegistration deviceRegistration = deviceRegistartionDAO.getDeviceRegistrationById(registrationId);

		Device device = deviceDAO.get(deviceId);
		device.setAlias(alias);

		deviceRegistration.setDeviceBean(device);

		DeviceStatus deviceStatusBean = deviceStatusDAO.get(deviceStatusId);
		deviceRegistration.setDeviceStatusBean(deviceStatusBean);

		deviceRegistartionDAO.updateDevice(deviceRegistration);
		
		//save the  deviceId key mapping with customerId_vin value
		Map<String, String> hmap = new HashMap<String, String>();
		hmap.put(deviceRegistration.getDeviceBean().getId(), deviceRegistration.getUserBean().getId() + "_" + deviceRegistration.getDeviceBean().getVinBean().getId());
		redisDAO.save(Constants.REDIS_IOT_DMI_CUSTOMERID, hmap);
	}
	
	@Transactional
	public void deleteDevice(String deviceId) throws ProcessorException
	{
		
		Device device = deviceDAO.get(deviceId);
		DeviceRegistration deviceReg = deviceRegistartionDAO.getDeviceRegistrationByDevice(device);
		
		
		serviceSubscriptionDAO.remove(deviceReg);
		deviceDAO.delete(deviceId);
		
		//delete the  deviceId key mapping with customerId_vin value
		String key = deviceReg.getDeviceBean().getId();
		redisDAO.delete(Constants.REDIS_IOT_DMI_CUSTOMERID, key);
		
		/*
		Long registrationId = deviceDTO.getRegistrationId();
		String deviceId = deviceDTO.getDeviceId();
		String alias = deviceDTO.getAlias();
		Long deviceStatusId = deviceDTO.getDeviceStatusId();

		// UPADTE REGISTERED DEVICE
		DeviceRegistration deviceRegistration = deviceRegistartionDAO.getRegisteredDevice(registrationId);

		Device device = deviceDAO.get(deviceId);
		device.setAlias(alias);

		deviceRegistration.setDeviceBean(device);

		DeviceStatus deviceStatusBean = deviceStatusDAO.get(deviceStatusId);
		deviceRegistration.setDeviceStatusBean(deviceStatusBean);

		deviceRegistartionDAO.updateDevice(deviceRegistration);*/
	}

	public JSONArray getCumulativeDataForAllDevices(Long oemId) {
		
		Oem oem = oemDAO.get(oemId);
		
		List<DeviceRegistration> registeredDevices = deviceRegistartionDAO.getRegisteredDevices(oem);

		JSONArray registeredDevicesArray = new JSONArray();
		
		for (DeviceRegistration deviceRegistration : registeredDevices)
		{
			JSONObject jsonObject = new JSONObject();

			jsonObject.put("userId", deviceRegistration.getUserBean().getId());
			jsonObject.put("userName", deviceRegistration.getUserBean().getFirstName() + " " + deviceRegistration.getUserBean().getLastName());
			jsonObject.put("vinId", deviceRegistration.getDeviceBean().getVinBean().getId());
			jsonObject.put("deviceId", deviceRegistration.getDeviceBean().getId());
			jsonObject.put("status", deviceRegistration.getDeviceStatusBean().getAlias());
		
			registeredDevicesArray.put(jsonObject);
		}

		return registeredDevicesArray;
	}

	public JSONArray getAllDevicesForUser(Long userId) {
			

		User user = userDAO.getUserById(userId);
		
		List<DeviceRegistration> registeredDevices = deviceRegistartionDAO.getRegisteredDevicesByUser(user);

		JSONArray deviceJsonArray = new JSONArray();		
	
		
		for(DeviceRegistration deviceRegistration : registeredDevices)
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("deviceId", deviceRegistration.getDeviceBean().getId());		
			jsonObject.put("vin", deviceRegistration.getDeviceBean().getVinBean().getId());	
			jsonObject.put("alias", deviceRegistration.getDeviceBean().getAlias());	
			
			deviceJsonArray.put(jsonObject);
		}
	
		return deviceJsonArray;
	}
}
