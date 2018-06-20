package com.dmi.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dmi.processor.PluggableModelProcessor;
import com.dmi.security.JWTHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags= "Pluggable Model Controller")
@RestController
@RequestMapping(value = "/pluggableModel")
public class PluggableModelController
{
	private static final Logger LOG = Logger.getLogger(PluggableModelController.class);
	
	@Autowired
	public PluggableModelProcessor pluggableModelProcessor;

	@ApiOperation(value = "Get list of all pluggable models", 
			notes = "API to fetch list of all pluggable models")
	@RequestMapping(value = "/getAllPluggableModels", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getPluggableModel(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken)
	{
		try
		{
			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
			JSONArray pluggableModelList = pluggableModelProcessor.getModelsByOemId(oemId);

			JSONObject response = new JSONObject().put("success", true)
					.put("msg", "Pluggable Models retrieved.")
					.put("pluggableModels", pluggableModelList);
			return ResponseEntity.status(HttpStatus.OK).body(
					response.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			JSONObject response = new JSONObject().put("success", false)
					.put("msg", e.getMessage())
					.put("pluggableModels", new JSONArray());
			return ResponseEntity.status(HttpStatus.OK).body(
					response.toString());
		}
	}

	@ApiOperation(value = "Get attribute list for rule builder from pluggable model", 
			notes = "API to fetch rule filter attrributes if pluggable model exist for selected device type")
	@RequestMapping(value = "/getModel", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getPluggableModel(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@RequestParam(required = true, value = "deviceTypeId") long deviceTypeId)
	{
		try
		{
			long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
			JSONArray pluggableModelList = pluggableModelProcessor.getModelsByOemAndDeviceType(oemId, deviceTypeId);

			JSONObject response = new JSONObject().put("success", true)
					.put("msg", "Pluggable Models retrieved.")
					.put("ruleFilters", pluggableModelList);
			return ResponseEntity.status(HttpStatus.OK).body(
					response.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			JSONObject response = new JSONObject().put("success", false)
					.put("msg", e.getMessage())
					.put("ruleFilters", new JSONArray());
			return ResponseEntity.status(HttpStatus.OK).body(
					response.toString());
		}
	}

	@RequestMapping(value = "/uploadModel", method ={ RequestMethod.POST, RequestMethod.PUT })
	public ResponseEntity<String> uploadPluggableModel(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@RequestBody(required = true) MultipartFile fileData,
			@RequestParam(required = true, value = "deviceTypeId") long deviceTypeId,
			@RequestParam(required = true, value = "modelTypeName") String modelTypeName,
			@RequestParam(required = true, value = "modelName") String modelName)
	{
		try
		{
			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken,"oemId"));
			String userName = JWTHelper.parseJWTClaim(jwtToken, "userName");

			if (!fileData.isEmpty())
			{
				
				LOG.info("file data is not empty");
				byte[] modelText;
				modelText = fileData.getBytes();

				pluggableModelProcessor.saveModel(oemId, userName, deviceTypeId,
						modelName, modelText, modelTypeName);
			}
			else
			{
				LOG.info("file data is empty");
				JSONObject result = new JSONObject().put("success", false).put(
						"msg", "Empty file was Uploaded.");
				return ResponseEntity.status(HttpStatus.OK).body(
						result.toString());
			}

			JSONObject response = new JSONObject().put("success", true).put(
					"msg", "Pluggable Model saved.");
			return ResponseEntity.status(HttpStatus.OK).body(
					response.toString());

		}
		catch (IOException ex)
		{
			LOG.error(ex.getMessage(), ex);
			JSONObject response = new JSONObject().put("success", false).put(
					"msg", ex.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(
					response.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			JSONObject response = new JSONObject().put("success", false).put(
					"msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(
					response.toString());
		}
	}

	/*@RequestMapping(value = "/saveModel", method =
	{ RequestMethod.POST, RequestMethod.PUT })
	public ResponseEntity<String> addPluggableModel(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@RequestParam(required = true, value = "deviceTypeId") long deviceTypeId,
			@RequestParam(required = true, value = "modelTypeName") String modelTypeName,
			@RequestParam(required = true, value = "modelName") String modelName,
			@RequestParam(required = true, value = "expression") String expression)
	{
		try
		{
			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken,
					"oemId"));
			String userName = JWTHelper.parseJWTClaim(jwtToken, "userName");
			
			pluggableModelProcessor.saveModel(oemId, userName, deviceTypeId, modelName,
					expression.getBytes(), modelTypeName);

			JSONObject response = new JSONObject().put("success", true).put(
					"msg", "Pluggable Model saved.");
			return ResponseEntity.status(HttpStatus.OK).body(
					response.toString());

		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			JSONObject response = new JSONObject().put("success", false).put(
					"msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(
					response.toString());
		}
	}*/

	@RequestMapping(value = "/deleteModel", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<String> getPluggableModel(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@RequestParam(required = true, value = "deviceTypeId") long deviceTypeId,
			@RequestParam(required = true, value = "modelId") long modelId,
			@RequestParam(required = true, value = "modelName") String modelName,
			@RequestParam(required = true, value = "modelTypeName") String modelTypeName)
	{
		try
		{
			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));

			pluggableModelProcessor.deleteModel(oemId, deviceTypeId,
					modelName, modelId, modelTypeName);

			JSONObject response = new JSONObject().put("success", true).put(
					"msg", "Pluggable Model deleted.");
			return ResponseEntity.status(HttpStatus.OK).body(
					response.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			JSONObject response = new JSONObject().put("success", false).put(
					"msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(
					response.toString());
		}
	}
}
