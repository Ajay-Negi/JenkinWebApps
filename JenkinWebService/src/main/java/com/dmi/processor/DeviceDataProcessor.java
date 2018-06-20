package com.dmi.processor;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dmi.dao.IDeviceDAO;
import com.dmi.dao.IDeviceDataDAO;
import com.dmi.dao.IDeviceRegistrationDAO;
import com.dmi.dao.IUserDAO;
import com.dmi.dao.model.DeviceData;
import com.dmi.dao.model.DeviceRegistration;
import com.dmi.dao.model.User;
import com.dmi.exception.ProcessorException;

/**
 * 
 * @author ANegi
 *
 */
@Service
public class DeviceDataProcessor
{
	@Autowired
	IUserDAO userDAO;
	@Autowired
	IDeviceDAO deviceDAO;
	@Autowired 
	IDeviceDataDAO deviceDataDAO;
	@Autowired
	IDeviceRegistrationDAO deviceRegistrationDAO;
	
	private static final Logger LOG = Logger.getLogger(DeviceDataProcessor.class);

	public ResponseEntity<String> fetchLatestData(Long customerId, String deviceAlias, String payload)
			throws ProcessorException
	{
		try
		{
			User user = userDAO.getUserById(customerId);
			List<DeviceRegistration> deviceRegistrationList = deviceRegistrationDAO.getRegisteredDevicesByUser(user);

			DeviceRegistration deviceRegistration = null;
			for (DeviceRegistration registration : deviceRegistrationList)
			{
				if (registration.getDeviceBean().getAlias().equalsIgnoreCase(deviceAlias))
				{
					deviceRegistration = registration;
					break;
				}
			}

			if (deviceRegistration == null)
				throw new ProcessorException("No Such Device Found!");

			Long oemId = deviceRegistration.getUserBean().getOemBean().getId();
			Long deviceTypeId = deviceRegistration.getDeviceBean().getDeviceTypeBean().getId();
			String deviceId = deviceRegistration.getDeviceBean().getId();

			String requestUrl = null;
			ResponseEntity<String> response = null;
			RestTemplate restTemplate = new RestTemplate();

			if (payload == null)
				requestUrl = String.format(
						"http://129.152.184.195:8889/DeviceDataAPI/data/latest?oemId=%s&deviceTypeId=%s&customerId=%s&deviceId=%s",
						oemId, deviceTypeId, customerId, deviceId);
			else
				requestUrl = String.format(
						"http://129.152.184.195:8889/DeviceDataAPI/data/latest?oemId=%s&deviceTypeId=%s&customerId=%s&deviceId=%s&payload=%s",
						oemId, deviceTypeId, customerId, deviceId, payload);

			response = restTemplate.getForEntity(requestUrl, String.class);

			return response;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new ProcessorException(e.getMessage());
		}

	}

	public JSONObject getDataFromDB(String deviceId) {

		DeviceData deviceData = deviceDataDAO.getByDeviceId(deviceId);
		
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("deviceId", deviceData.getId());
		jsonObj.put("data", new JSONArray(new String(deviceData.getData())));
	
		
		return jsonObj;
		
	}

}
