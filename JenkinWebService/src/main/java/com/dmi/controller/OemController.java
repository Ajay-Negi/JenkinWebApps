package com.dmi.controller;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dmi.constant.Constants;
import com.dmi.dao.model.Oem;
import com.dmi.exception.ProcessorException;
import com.dmi.processor.OemProcessor;
import com.dmi.security.JWTHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author ANegi
 *
 */
@Api(tags = "OEM Contoller")
@RestController
public class OemController
{

	private static final Logger LOG = Logger.getLogger(OemController.class);
	
	@Autowired
	OemProcessor oemProcessor;

	@ApiOperation(value = "Get list of all available OEMs.", notes = "Returns list of Oem entity in json format", response = Oem.class, responseContainer = "List")
	@RequestMapping(value = "/oem", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getAllOem()
	{
		try
		{
			JSONArray oemList = oemProcessor.getAllOem();
			JSONObject response = new JSONObject().put("oemList", oemList);
			JSONObject result = new JSONObject().put("success", true).put("response", response)
					.put("msg", "Oem List Retrieved.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());

		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		
	}
	
	@ApiOperation(value = "Get Oem by name.", notes = "API to fetch Oem details eg. Id and name in json format")
	@RequestMapping(value = "/oem/getByName", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getOemByName(
			@ApiParam(value = "Oem name") @RequestParam(required = true, value = "oemName") String oemName)
	{
		try
		{
			JSONObject oemObj = oemProcessor.getOemByName(oemName);
			JSONObject response = new JSONObject().put("oem", oemObj);
			JSONObject result = new JSONObject().put("success", true).put("response", response)
					.put("msg", "Oem List Retrieved.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());

		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		
	}

	@ApiOperation(value = "Create a new oem.", notes = "API to save a new Oem")
	@RequestMapping(value = "/oem", method = RequestMethod.POST, produces = "application/json", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> saveOem(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "Url for oem logo")@RequestParam(required = true, value = "logoUrl") String logoUrl,
			@ApiParam(value = "URL for background image of the OEM, optional") @RequestParam(required = true, value = "backGroundImageURL") String backGroundImageURL,
			@ApiParam(value = "OEM name") @RequestParam(required = true, value = "oemName") String oemName,
			@ApiParam(value = "Oem classification Id") @RequestParam(required = true, value = "oemClassificationId") Long oemClassificationId,
			@ApiParam(value = "Active or Inactive Location Based Services status") @RequestParam(required = true, value = "locationServicesStatus") String locationServicesStatus
			)
	{
		try
		{
			String roleCode = JWTHelper.parseJWTClaim(jwtToken, "roleCode");
			
			if(!roleCode.equalsIgnoreCase(Constants.USER_ROLE_CODE_SUPERADMIN))
				throw new ProcessorException("You don't have super admin permissions to create a new Oem");
					
			oemProcessor.saveOem(logoUrl,oemName,oemClassificationId,locationServicesStatus,backGroundImageURL);
			JSONObject result = new JSONObject().put("success", true).put("msg", "Oem saved.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());

		}
		catch (ProcessorException ex)
		{
			LOG.error(ex);
			
			JSONObject result = new JSONObject().put(Constants.SUCCESS, false).put(
					"msg", ex.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(
					result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		
		
	}

	@ApiOperation(value = "Delete an oem." , notes = "Provide the Oem Id for its deletion")
	@RequestMapping(value = "/oem", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<String> deleteOem(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "OEM id which needs to be deleted") @RequestParam(required = false, value = "oemId") Long oemId)
	{
		try
		{
			String roleCode=JWTHelper.parseJWTClaim(jwtToken, "roleCode");
			oemProcessor.deleteOem(roleCode,oemId);
			JSONObject result = new JSONObject().put("success", true).put("msg", "Oem deleted.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());

		}
		catch (ProcessorException pe)
		{
			LOG.error(pe.getMessage(),pe);
			JSONObject result = new JSONObject().put("success", false).put("msg", pe.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		
	
	}
	
	
	/*
	@RequestMapping(value = "oem/toggleLocationServices/{oemId}/{status}", method = RequestMethod.PUT, produces = "application/json")
	@ApiOperation(value = "Set location services status enabled or disabled", notes = "Provide OEM Id and status as enabled or disbaled to set the same for the OEM")
	public ResponseEntity<String> toggleLocationServices( @PathVariable("status") String status,
			@PathVariable("oemId") long oemId)
	{
		try
		{
			
			oemProcessor.toggleLocationServices(status, oemId);

			JSONObject response = new JSONObject().put("success", true).put(
					"msg", "Location services status set successfully");
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
		
		JSONObject response = new JSONObject().put("success", true);
		return ResponseEntity.status(HttpStatus.OK).body(
				response.toString());
		
	}*/
		
		

}
