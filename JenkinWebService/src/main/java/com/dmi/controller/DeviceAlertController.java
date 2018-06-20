package com.dmi.controller;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dmi.dao.model.Alert;
import com.dmi.exception.ProcessorException;
import com.dmi.processor.DeviceAlertProcessor;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**@author ANegi
*
*/
@Api(tags = "Device Alerts Controller")
@RestController
@RequestMapping(value="/deviceAlerts")
public class DeviceAlertController {

private static final Logger LOG = Logger.getLogger(DeviceAlertController.class);
	
	@Autowired
	DeviceAlertProcessor deviceAlertProcessor;
	
	@ApiOperation(value = "Save alert messages for a device", notes = "API to save alert messages for a device")
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> save(@ApiParam(value = "Device Id of device for whom alert message needs to be saved", required = true) @RequestParam("deviceId") 
	String deviceId, @ApiParam(value = "Alert message text which needs to be saved", required = true) @RequestParam("alertText") String alertText)
	{
		try
		{
			deviceAlertProcessor.save(deviceId, alertText);

			JSONObject response = new JSONObject().put("success", true)
					.put("msg", "Device alert saved");
			
			return ResponseEntity.ok().body(response.toString());
		}catch (ProcessorException ex)
		{
			LOG.error(ex.getMessage(), ex);
			
			JSONObject response = new JSONObject().put("success", false)
					.put("msg", ex.getMessage());
			
			return ResponseEntity.ok().body(response.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			
			JSONObject response = new JSONObject().put("success", false)
					.put("msg", e.getMessage());
			
			return ResponseEntity.ok().body(response.toString());
		}
		
	}
		
	@ApiOperation(value = "Get alerts for a deviceId", notes = "API for getting alerts for a device id", response = Alert.class, responseContainer = "List")
	@RequestMapping(value = "/getByDeviceId/{deviceId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getByDeviceId(@ApiParam(value = "Device Id for which all alerts needs to be fetched", required = true) @PathVariable("deviceId") String deviceId)
	{
		try
		{
			JSONArray alertArray = deviceAlertProcessor.getByDeviceId(deviceId);

			JSONObject response = new JSONObject().put("success", true)
					.put("msg", "Device alerts retrieved.")
					.put("deviceAlerts", alertArray)
					.put("count", alertArray.length());
			
			return ResponseEntity.ok().body(response.toString());
			
		}catch (ProcessorException ex)
		{
			LOG.error(ex.getMessage(), ex);
			
			JSONObject response = new JSONObject().put("success", false)
					.put("msg", ex.getMessage());
			
			return ResponseEntity.ok().body(response.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			
			JSONObject response = new JSONObject().put("success", false)
					.put("msg", e.getMessage());
			
			return ResponseEntity.ok().body(response.toString());
		}
		
	}
	
	@RequestMapping(value = "/delete/{deviceId}", method = RequestMethod.DELETE, produces = "application/json")
	@ApiOperation(value = "Delete alerts of a device", notes = "API for deleting device's alerts")
	public ResponseEntity<String> delete(@ApiParam(value = "Device Id for which alerts needs to be deleted", required = true) @PathVariable("deviceId") String deviceId)
	{
		try
		{
			deviceAlertProcessor.delete(deviceId);

			JSONObject response = new JSONObject().put("success", true).put(
					"msg", "Device alerts deleted.");
			return ResponseEntity.status(HttpStatus.OK).body(
					response.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getLocalizedMessage(), e);
			JSONObject response = new JSONObject().put("success", false).put(
					"msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(
					response.toString());
		}
		
	}
}
