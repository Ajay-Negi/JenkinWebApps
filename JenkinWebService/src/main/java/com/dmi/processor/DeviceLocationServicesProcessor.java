package com.dmi.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.hadoop.util.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.constant.Constants;
import com.dmi.dao.IDeviceDAO;
import com.dmi.dao.IDeviceRegistrationDAO;
import com.dmi.dao.IGeofenceActionDAO;
import com.dmi.dao.IGeolocationDAO;
import com.dmi.dao.INotificationTemplateDAO;
import com.dmi.dao.IRedisDAO;
import com.dmi.dao.IServiceDAO;
import com.dmi.dao.IServiceSubscriptionDAO;
import com.dmi.dao.ISpeedDAO;
import com.dmi.dao.ISubServiceDAO;
import com.dmi.dao.ISubServiceSubscriptionDAO;
import com.dmi.dao.IUserDAO;
import com.dmi.dao.enums.NotificationChannelEnum;
import com.dmi.dao.model.Device;
import com.dmi.dao.model.DeviceRegistration;
import com.dmi.dao.model.GeofenceAction;
import com.dmi.dao.model.Geolocation;
import com.dmi.dao.model.NotificationTemplate;
import com.dmi.dao.model.ServiceSubscription;
import com.dmi.dao.model.Speed;
import com.dmi.dao.model.SubServiceSubscription;
import com.dmi.dao.model.User;
import com.dmi.dto.geospatial.GeospatialCollectionDTO;
import com.dmi.dto.location.DeviceLocationDTO;
import com.dmi.dto.location.Fence;
import com.dmi.dto.location.Geometry;
import com.dmi.dto.location.Location;
import com.dmi.dto.location.Properties;
import com.dmi.exception.ProcessorException;
import com.dmi.utils.DateUtils;
import com.dmi.utils.RandomNumberUtils;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * 
 * @author ANegi
 *
 */
@Service
public class DeviceLocationServicesProcessor
{
	private static final Logger LOG = Logger.getLogger(DeviceLocationServicesProcessor.class);
	
	@Autowired
	IUserDAO userDAO;
	@Autowired
	IDeviceDAO deviceDAO;
	@Autowired
	IRedisDAO redisDAO;
	@Autowired
	IDeviceRegistrationDAO deviceRegistrationDAO;
	@Autowired
	IGeofenceActionDAO geofenceActionDAO;
	@Autowired
	IGeolocationDAO geolocationDAO;
	@Autowired
	ISpeedDAO speedLimitDAO;
	@Autowired
	IServiceDAO serviceDAO;
	@Autowired
	ISubServiceDAO subServiceDAO;
	@Autowired
	IServiceSubscriptionDAO serviceSubscriptionDAO;
	@Autowired
	ISubServiceSubscriptionDAO subServiceSubscriptionDAO;
	@Autowired
	INotificationTemplateDAO notificationTemplateDAO;

	public JSONObject checkEligibility(String username, String deviceId, String subServiceName)
			throws ProcessorException
	{
		JSONObject jsonObject;
		boolean eligibilityFlag = false;
		try
		{
			jsonObject = new JSONObject();
			User user = userDAO.getUser(username);
			Device device = deviceDAO.get(deviceId);

			// CHECK IF USER HAS OPTED FOR LOCATION BASED SERVICE AND
			// GEOLOCATION SUBSERVICE

			List<ServiceSubscription> serviceSubscriptions = serviceSubscriptionDAO
					.getSubscribedServices(deviceRegistrationDAO.getRegisteredDevice(user, device));
			
			for (ServiceSubscription serviceSubscrp : serviceSubscriptions)
				if (serviceSubscrp.getServiceBean().getName().equalsIgnoreCase(Constants.LOCATION_SERVICE_NAME))
				{
					List<SubServiceSubscription> subServiceSubscriptions = subServiceSubscriptionDAO.getSubscribedSubServices(serviceSubscrp);
					
					for (SubServiceSubscription subServiceSubscrp : subServiceSubscriptions)
						if (subServiceSubscrp.getSubServiceBean().getName().equalsIgnoreCase(subServiceName))
						{
							eligibilityFlag = true;
							jsonObject.put("serviceId", serviceSubscrp.getServiceBean().getId());
							jsonObject.put("subServiceId", subServiceSubscrp.getSubServiceBean().getId());
						}
				}

			jsonObject.put("eligibility", eligibilityFlag);
			return jsonObject;
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public DeviceLocationDTO locateAllDevices(String username) throws ProcessorException
	{
		DeviceLocationDTO deviceLocationDTO = new DeviceLocationDTO();
		try
		{
			// MOCKED DATA
			/*
			 * String sampleMapData = ""; ClassLoader classLoader =
			 * getClass().getClassLoader(); Scanner scanner = new Scanner(new
			 * File(classLoader.getResource("sampleMapData.txt").getFile()));
			 * while (scanner.hasNextLine()) { sampleMapData +=
			 * scanner.nextLine(); } scanner.close(); ObjectMapper mapper = new
			 * ObjectMapper(); deviceLocationDTO =
			 * mapper.readValue(sampleMapData, DeviceLocationDTO.class);
			 */

			// ACTUAL DATA
			deviceLocationDTO.setType("LocationCollection");

			List<Location> locationList = new ArrayList<>();

			List<DeviceRegistration> registrations = deviceRegistrationDAO.getRegisteredDevicesByUser(userDAO.getUser(username));
			for (DeviceRegistration registration : registrations)
			{
				Location location = new Location();

				location.setType("Location");

				Geometry geometry = new Geometry();
				Geolocation geolocation = geolocationDAO.get(registration.getDeviceBean());
				if (geolocation != null)
				{
					String geography = new String(geolocation.getGeography());
					ObjectMapper mapper = new ObjectMapper();
					GeospatialCollectionDTO geoDTO = mapper.readValue(geography, GeospatialCollectionDTO.class);
					geometry.setRadius(geoDTO.getGeography().getGeometry().getRadius());
					geometry.setCoordinates(geoDTO.getGeography().getGeometry().getCoordinates());
					// TODO Fetch Reference from Oracle IoT-CS
					List<Float> reference = new ArrayList<>();
					reference.add(geoDTO.getGeography().getGeometry().getCoordinates().get(0).getLat());
					reference.add(geoDTO.getGeography().getGeometry().getCoordinates().get(0).getLng());
					geometry.setReference(reference);
					geometry.setType(geoDTO.getGeography().getType());
					Fence fence = new Fence();
					fence.setId(geolocation.getGeofenceActionBean().getId());
					fence.setName(geolocation.getGeofenceActionBean().getAlias());
					geometry.setFence(fence);
					location.setGeometry(geometry);
				}
				else
				{
					// TODO Fetch Reference from Oracle IoT-CS
					List<Float> reference = new ArrayList<>();
					reference.add(RandomNumberUtils.getFloat(Constants.DETROIT_LAT_MIN, Constants.DETROIT_LAT_MAX));
					reference.add(RandomNumberUtils.getFloat(Constants.DETROIT_LNG_MIN, Constants.DETROIT_LNG_MAX));
					geometry.setReference(reference);
					location.setGeometry(geometry);
				}
				Properties properties = new Properties();
				properties.setDeviceAlias(registration.getDeviceBean().getAlias());
				properties.setDeviceId(registration.getDeviceBean().getId());
				properties.setDeviceTypeId(registration.getDeviceBean().getDeviceTypeBean().getId());
				properties.setPlace("");
				properties.setTime(DateUtils.getEpochTimeNow());
				location.setProperties(properties);

				locationList.add(location);
			}

			deviceLocationDTO.setLocation(locationList);

		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
		return deviceLocationDTO;
	}

	public JSONObject getGeofence(String deviceId) throws ProcessorException
	{
		JSONObject jsonObject;
		try
		{
			jsonObject = new JSONObject();

			Device device = deviceDAO.get(deviceId);
			if (device == null)
				throw new ProcessorException("No device with given deviceId found.");

			DeviceRegistration deviceRegistration = deviceRegistrationDAO.getDeviceRegistrationByDevice(device);
			if (deviceRegistrationDAO.getRegisteredDevice(deviceRegistration.getUserBean(), device) == null)
				throw new ProcessorException("No device with given deviceId found.");

			Geolocation geolocation = geolocationDAO.get(device);
			if (geolocation == null)
				throw new Exception("No geofence mapping found for your device.");

			String geoData = new String(geolocation.getGeography());
			ObjectMapper mapper = new ObjectMapper();
			GeospatialCollectionDTO geoDTO = mapper.readValue(geoData, GeospatialCollectionDTO.class);
			jsonObject.put("type", geoDTO.getGeography().getType());

			if (geoDTO.getGeography().getType().equalsIgnoreCase(Constants.GEOGRAPHY_TYPE_CIRCLE))
				jsonObject.put("radius", geoDTO.getGeography().getGeometry().getRadius());

			jsonObject.put("unit", deviceRegistration.getUserBean().getUom().getAlias());
			jsonObject.put("geoFenceCoordinates", geoDTO.getGeography().getGeometry().getCoordinates());

			JSONObject fence = new JSONObject();
			fence.put("id", geolocation.getGeofenceActionBean().getId());
			fence.put("area", geolocation.getGeofenceActionBean().getAlias());
			jsonObject.put("fence", fence);

			return jsonObject;
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
	}

	@Transactional
	public void saveUpdateGeofence(String geoDTO, String username, String deviceId, Long geoFenceActionId)
			throws ProcessorException
	{
		try
		{
			User user = userDAO.getUser(username);
			Device device = deviceDAO.get(deviceId);
			if (deviceRegistrationDAO.getRegisteredDevice(user, device) == null)
				throw new ProcessorException("No device with given deviceId found.");

			JSONObject jsonEligibilityObj = checkEligibility(username, deviceId, Constants.GEOFENCE_SUBSERVICE_NAME);
			
			if (Constants.CHECK_LOCATION_SERVICE_SUBSCRIPTION)
				if (jsonEligibilityObj.get("eligibility").toString().equalsIgnoreCase("false"))
					throw new ProcessorException(
							"You have not opted for Location Based Services. Hence you are not eligible to set a geofence for your device.");

			// VALIDATING geoDTO
			ObjectMapper mapper = new ObjectMapper();
			
			try
			{
				mapper.readValue(geoDTO, GeospatialCollectionDTO.class);
			}
			catch (Exception e)
			{
				LOG.error(e.getMessage(), e);
				throw new ProcessorException("Improper format of geoDTO.");
			}

			Geolocation geolocation = geolocationDAO.get(device);

			//get service subservice id from json 
			Long serviceId = jsonEligibilityObj.getLong("serviceId");
			Long subServiceId = jsonEligibilityObj.getLong("subServiceId");
			
			//get notificatoin template for subservice and channel types email and sms
			NotificationTemplate emailTemplate = notificationTemplateDAO.getBySubserviceAndChannel(subServiceId, NotificationChannelEnum.EMAIL);
			NotificationTemplate smsTemplate = notificationTemplateDAO.getBySubserviceAndChannel(subServiceId, NotificationChannelEnum.SMS);
			
			if (geolocation == null)
			{
				geolocation = new Geolocation();
				geolocation.setDeviceBean(deviceDAO.get(deviceId));
				GeofenceAction fenceActionBean = geofenceActionDAO.get(geoFenceActionId);
				geolocation.setGeofenceActionBean(fenceActionBean);
				geolocation.setGeography(geoDTO.getBytes());
				geolocation.setServiceId(serviceId);
				geolocation.setSubServiceId(subServiceId);
				try{
					geolocation.setEmailTemplateId(emailTemplate.getId());
					geolocation.setSmsTemplateId(smsTemplate.getId());
				}catch(NullPointerException npe){
					throw new ProcessorException("Email or SMS template not set for this webservice");
					
				}
				geolocationDAO.save(geolocation);
				
				//save data in Redis
				Map<String, String> hmap = new HashMap<String, String>();
				String key = device.getOemBean().getId()+ "_"+ device.getDeviceTypeBean().getId() + "_" + device.getId()
						+ "_" + serviceId + "_"+ subServiceId;
				
				JSONObject json = new JSONObject();
				json.put("serviceId", geolocation.getServiceId().toString());
				json.put("subServiceId", geolocation.getSubServiceId().toString());
				json.put("smsTempId", geolocation.getSmsTemplateId().toString());
				json.put("emailTempId", geolocation.getEmailTemplateId().toString());
				json.put("action", geolocation.getGeofenceActionBean().getId().toString());
				json.put("geofencedata",new JSONObject(geoDTO));
				String value = json.toString();
				hmap.put(key,value);
								
				redisDAO.save(Constants.REDIS_IOT_DMI_GEOFENCE, hmap);
			}
			else
			{
				GeofenceAction fenceActionBean = geofenceActionDAO.get(geoFenceActionId);
				geolocation.setGeofenceActionBean(fenceActionBean);
				geolocation.setGeography(geoDTO.getBytes());
				Geolocation savedGeolocationBean = geolocationDAO.update(geolocation);
				
				//save data in Redis
				Map<String, String> hmap = new HashMap<String, String>();
				String key = device.getOemBean().getId()+ "_"+ device.getDeviceTypeBean().getId() + "_" + device.getId()
						+ "_" + serviceId + "_"+ subServiceId;
				
				JSONObject json = new JSONObject();
				json.put("serviceId", savedGeolocationBean.getServiceId().toString());
				json.put("subServiceId", savedGeolocationBean.getSubServiceId().toString());
				json.put("smsTempId", savedGeolocationBean.getSmsTemplateId().toString());
				json.put("emailTempId", savedGeolocationBean.getEmailTemplateId().toString());
				json.put("action", savedGeolocationBean.getGeofenceActionBean().getId().toString());
				json.put("geofencedata", new JSONObject(geoDTO));
				String value = json.toString();
				hmap.put(key,value);
				
				redisDAO.save(Constants.REDIS_IOT_DMI_GEOFENCE, hmap);
			}
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
	}

	@Transactional
	public void deleteGeofence(String username, String deviceId) throws ProcessorException
	{
		try
		{
			Device device = deviceDAO.get(deviceId);
			Geolocation geolocation = geolocationDAO.get(device);
			geolocationDAO.delete(device);
			
			//delete data in Redis
			String key = device.getOemBean().getId()+ "_"+ device.getDeviceTypeBean().getId() + "_" + device.getId()
					+ "_" + geolocation.getServiceId() + "_"+ geolocation.getSubServiceId();
						
			redisDAO.delete(Constants.REDIS_IOT_DMI_GEOFENCE, key);
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public JSONObject getSpeedLimitDetails(String username, String deviceId) throws ProcessorException
	{
		JSONObject jsonObject;
		try
		{
			jsonObject = new JSONObject();

			User user = userDAO.getUser(username);
			Device device = deviceDAO.get(deviceId);
			if (deviceRegistrationDAO.getRegisteredDevice(user, device) == null)
				throw new Exception("No device with given deviceId found.");

			Speed speed = speedLimitDAO.get(device);
			if (speed == null)
				throw new ProcessorException("No speed limit details found for your device.");

			jsonObject.put("speedLimit", speed.getSpeedLimit());

			return jsonObject;
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
	}

	@Transactional
	public void saveUpdateSpeedLimitDetails(String username, String deviceId, int speedLimit) throws ProcessorException
	{
		try
		{
			User user = userDAO.getUser(username);
			Device device = deviceDAO.get(deviceId);
			if (deviceRegistrationDAO.getRegisteredDevice(user, device) == null)
				throw new ProcessorException("No device with given deviceId found.");

			if (Constants.CHECK_LOCATION_SERVICE_SUBSCRIPTION)
				if (checkEligibility(username, deviceId, Constants.SPEEDLIMIT_SUBSERVICE_NAME).get("eligibility")
						.toString().equalsIgnoreCase("false"))
					throw new ProcessorException(
							"You have not opted for Location Based Services. Hence you are not eligible to set a speed limit for your device.");

			Speed speed = speedLimitDAO.get(device);

			if (speed == null)
			{
				speed = new Speed();
				speed.setDeviceBean(device);
				speed.setSpeedLimit(speedLimit);
				speedLimitDAO.save(speed);
			}
			else
			{
				speed.setSpeedLimit(speedLimit);
				speedLimitDAO.update(speed);
			}
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
	}

	@Transactional
	public void deleteSpeedLimitDetails(String username, String deviceId) throws ProcessorException
	{
		try
		{
			speedLimitDAO.delete(deviceDAO.get(deviceId));
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
	}
}
