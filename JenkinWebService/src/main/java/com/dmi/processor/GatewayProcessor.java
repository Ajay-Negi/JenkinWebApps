package com.dmi.processor;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dmi.constant.Constants;
import com.dmi.dao.IDeviceConfigDAO;
import com.dmi.dao.IDeviceDAO;
import com.dmi.dao.model.Device;
import com.dmi.dao.model.DeviceConfig;
import com.dmi.dto.gateway.DeviceConfigModel;
import com.dmi.dto.gateway.TelemetryData;
import com.dmi.dto.gateway.VehicleHealthData;
import com.dmi.exception.ProcessorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author ANegi
 *
 */
@Service
public class GatewayProcessor {
	
	@Autowired
	IDeviceConfigDAO deviceConfigDAO;
	
	@Autowired
	IDeviceDAO deviceDAO;

	private static final Logger LOG = Logger.getLogger(GatewayProcessor.class);
	
	public JSONObject getDeviceConfigModelByVinId(String vinId) throws ProcessorException {

		DeviceConfigModel deviceConfigModel = new DeviceConfigModel();
		TelemetryData telemetryData = new TelemetryData();
		VehicleHealthData vehicleHealthData = new VehicleHealthData();
		
		JSONObject jsonVObj = null;
		
		try {
			DeviceConfig deviceConfigBean = deviceConfigDAO.getByVinId(vinId);
			
			telemetryData.setDataCollectionInterval(deviceConfigBean.getTelemetryDataCollectionInterval());
			telemetryData.setDataUploadInterval(deviceConfigBean.getTelemetryDataUploadInterval());
			
			vehicleHealthData.setDataCollectionInterval(deviceConfigBean.getVehicleHealthDataCollectionInterval());
			vehicleHealthData.setDataUploadInterval(deviceConfigBean.getVehicleHealthDataUploadInterval());
			
			deviceConfigModel.setTelemetryData(telemetryData);
			deviceConfigModel.setVehicleHealthData(vehicleHealthData);
			
			String jsonString;
			
			ObjectMapper objectMapper = new ObjectMapper();
			
			jsonString = objectMapper.writeValueAsString(deviceConfigModel);
			
			jsonVObj = new JSONObject(jsonString);
			
		} catch (DataAccessException e) {
			LOG.error(e.getMessage(), e);
		}
		 catch (JsonProcessingException ex) {
				LOG.error(ex.getMessage(), ex);
			}
		return jsonVObj;
	}

	public JSONObject getKeyDataByDevice(String vinId) throws ProcessorException  {

		JSONObject jsonObj = new JSONObject();
		
		try{
			Device deviceBean = deviceDAO.getByVinId(vinId);
		
			jsonObj.put("deviceId", deviceBean.getId());
			jsonObj.put("configDataURL", Constants.APPLICATION_PROJECT_URL + "connectedVehicle" + "/getConfigData");
			jsonObj.put("topicURL", Constants.TOPIC_URL + deviceBean.getId() + "/" + vinId);
		}catch(Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
		
			return jsonObj;
	}
	
	
}
