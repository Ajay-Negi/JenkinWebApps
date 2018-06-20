package com.dmi.controller;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dmi.constant.Constants;
import com.dmi.processor.DeviceDataProcessor;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import redis.clients.jedis.Jedis;


/**@author ANegi
*
*/
@Api(tags = "Device Data")
@RestController
@RequestMapping(value="/deviceData")
public class DeviceDataController
{
	private static final Logger LOG = Logger.getLogger(DeviceDataController.class);
	
	@Autowired
	DeviceDataProcessor deviceDataProcessor;

	
	@ApiOperation(value = "Get device data for dashboard", notes = "API to fetch device data from non NoSql database to show in the portal dashboard")
	@RequestMapping(value = "/getDashboardDeviceData/{deviceId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> fetchDeviceData(@ApiParam(value = "Device Id whose data needs to be fetched", required = true)
								@PathVariable("deviceId") String deviceId)
	{
		
		String redisKey = "vehicleData:" + deviceId;
		Jedis jedis = new Jedis(Constants.REDIS_IP);
		jedis.auth("chanje");
		
		try
		{
			
			Map<String, String> jmap = jedis.hgetAll(redisKey);
			
			/**
			 * Add iterated json object in this array
			 */
			JSONArray jsonArray = new JSONArray();
			
			for(Map.Entry<String,String> entry: jmap.entrySet())
			{
				if(! entry.getKey().equalsIgnoreCase("vin") && !entry.getKey().equalsIgnoreCase("id"))
				{
					JSONObject jsonObj = new JSONObject();
					
					String key = WordUtils.capitalize(StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(entry.getKey()), ' '));
					
					jsonObj.put("sensor", key);
					jsonObj.put("value", entry.getValue());
					
					jsonArray.put(jsonObj);
				}
			}
			
			System.out.println("JSON ARRAY::" + jsonArray); 
			
			//JSONObject deviceData = deviceDataProcessor.getDataFromDB(deviceId);

			JSONObject response = new JSONObject().put("success", true)
					.put("msg", "Device data retrieved.")
					.put("deviceData", jsonArray);
			
			jedis.close();
			return ResponseEntity.ok().body(response.toString());
			
		}
		catch (Exception e)
		{
			jedis.close();
			LOG.error(e.getMessage(), e);
			
			JSONObject response = new JSONObject().put("success", false)
					.put("msg", e.getMessage());
			
			return ResponseEntity.ok().body(response.toString());
		}
		
	}
	
	
	/*@ApiOperation(value = "Get latest device data.", notes = "API to fetch latest device data from NoSql database eg.Redis")
	@RequestMapping(value = "/getLatestDeviceData", method = RequestMethod.POST)
	public ResponseEntity<String> getLatest(@ApiParam(value = "Device Id of the device", required = true) @RequestParam(required = true, value = "deviceId") String deviceId,
			@ApiParam(value = "Start Date", required = true) @RequestParam(required = false, value = "startDate") String startDate,
			@ApiParam(value = "End Date", required = true) @RequestParam(required = false, value = "endDate") String endDate) throws IOException
	{
		
		String requestUrl = null;
		ResponseEntity<String> response = null;
		
		try{
			
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
		}catch(Exception e)
		{
			LOG.error(e.getMessage(), e);
			return response;
		}
		
		JSONObject result = new JSONObject().put("success", true);

		return ResponseEntity.ok().body(result.toString());
		
	}*/
	
	
	
	/*@ApiOperation(value = "Fetch latest device data from Hbase")
	@RequestMapping(value = "/getLatestDeviceData", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> fetchLatestData(@RequestParam(required = true, value = "userId") Long userId,
			@RequestParam(required = true, value = "deviceAlias") String deviceAlias,
			@RequestParam(required = false, value = "payload") String payload)
	{
		try
		{
			return deviceDataProcessor.fetchLatestData(userId, deviceAlias, payload);
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}
	*/
	
	
	
	
}
