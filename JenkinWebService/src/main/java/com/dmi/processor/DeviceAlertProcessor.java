package com.dmi.processor;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.dao.IDeviceAlertDAO;
import com.dmi.dao.IDeviceDAO;
import com.dmi.dao.model.Alert;
import com.dmi.dao.model.Device;
import com.dmi.exception.ProcessorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * 
 * @author ANegi
 *
 */
@Service
public class DeviceAlertProcessor {

	private static final Logger LOG = Logger.getLogger(DeviceAlertProcessor.class);

	@Autowired
	IDeviceAlertDAO deviceAlertDAO;
	
	@Autowired
	IDeviceDAO deviceDAO;

	@Transactional
	public void save(String deviceId, String alertText) throws ProcessorException{
		
		Alert alert = new Alert();
		
		Device device = deviceDAO.get(deviceId);
		
		if(device!=null){
	
			try{
				alert.setAlertText(alertText);
				/*
				 * record current date and time
				 */
				java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
				alert.setInsertTime(sqlDate);
				alert.setDeviceBean(device);
				deviceAlertDAO.save(alert);
		
			}catch(Exception ex){
				
				LOG.error(ex.getMessage(), ex);
				
			}
			
		}else
		{
			throw new ProcessorException("Device Id is not valid");
		}
		
	}
	
	@Transactional
	public void delete(String deviceId){
		
		deviceAlertDAO.delete(deviceId);
		
	}
	
	
	public JSONArray getByDeviceId(String deviceId) throws ProcessorException {
		
		List<Alert> alertList = deviceAlertDAO.getByDeviceId(deviceId);
		JSONArray jsonArray = new JSONArray();
		
		if(!alertList.isEmpty()){
			
			String jsonString = null;
			for(Alert alert : alertList)
			{
				
				ObjectMapper objectMapper = new ObjectMapper();
				
				try {
					jsonString = objectMapper.writeValueAsString(alert);
				} catch (JsonProcessingException ex) {

					LOG.error(ex.getMessage(),ex);
					
				}
				
				jsonArray.put( new JSONObject(jsonString));
				
			}
			
			
			return jsonArray;
		}
		else
		{
			throw new ProcessorException("No alerts for the device");
		}
		
	}

}
