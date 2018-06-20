package com.dmi.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dmi.processor.UserManualProcessor;
import com.dmi.security.JWTHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author ANegi
 *
 */
@Api(tags = "User Manual Operations")
@RestController
@RequestMapping(value = "/userManual")
public class UserManualController
{
	
	private static final Logger LOG = Logger.getLogger(UserManualController.class);
	
	@Autowired
	UserManualProcessor userManualProcessor;

	@ApiOperation(value = "Get availibility of EManual for a OEM's/User's all Device Types.", notes= "API to fetch availability status for device types")
	@RequestMapping(value = "/availibility", method = RequestMethod.GET)
	public ResponseEntity<String> getEManualAvailibility(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken)
	{
		try
		{
			String roleCode = JWTHelper.parseJWTClaim(jwtToken, "roleCode");
			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
			String userName = JWTHelper.parseJWTClaim(jwtToken, "userName");
			JSONArray response = userManualProcessor.getManualAvailibility(roleCode, oemId, userName);
			JSONObject res = new JSONObject().put("success", true).put("response", response).put("msg",
					"EManual Availibility Retrieved.");
			return ResponseEntity.status(HttpStatus.OK).body(res.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject res = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res.toString());
		}
	}

	@ApiOperation(value = "Upload/Update EManual for a OEM's particular Device Type.", notes = "API to upload E- manual for a device type")
	@RequestMapping(value = "/upload", method = RequestMethod.POST, headers = "content-type=multipart/*")
	public ResponseEntity<String> handleEManualUpload(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "Device type id for which e-manual needs to be updated") @RequestParam(value = "deviceTypeId") Long deviceTypeId, 
			@ApiParam(value = "E-manual file which needs to be uploaded") @RequestParam(value = "file") MultipartFile file)
	{
		try
		{
			String roleCode = JWTHelper.parseJWTClaim(jwtToken, "roleCode");
			String oemId = JWTHelper.parseJWTClaim(jwtToken, "oemId");
			userManualProcessor.handleManualUpload(roleCode, oemId, deviceTypeId, file);
			JSONObject res = new JSONObject().put("success", true).put("msg", "EManual Successfully Saved/Updated.");
			return ResponseEntity.status(HttpStatus.OK).body(res.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject res = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res.toString());
		}
	}

	@ApiOperation(value = "View User Manual for a particular Device Type.", notes = "API for fetch e-manual for a device type")
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ResponseEntity<?> viewEManual(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "Device Type for which e-manual is required") @RequestParam(value = "deviceTypeId") Long deviceTypeId)
	{
		try
		{
			String oemId = JWTHelper.parseJWTClaim(jwtToken, "oemId");
			byte[] eManualContent = userManualProcessor.viewManual(oemId, deviceTypeId);

			String fileName = "EManual.pdf";

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
			headers.add("content-disposition", "inline;filename=" + fileName);
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

			return new ResponseEntity<byte[]>(eManualContent, headers, HttpStatus.OK);
		}
		catch (IOException e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject res = new JSONObject().put("success", false).put("msg", e.getMessage());

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("application/json"));

			return new ResponseEntity<String>(res.toString(), headers, HttpStatus.OK);
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject res = new JSONObject().put("success", false).put("msg", e.getMessage());

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("application/json"));

			return new ResponseEntity<String>(res.toString(), headers, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "DELETE an EManual for a OEM's Device Types." , notes = "API to delete e-manual for a device type")
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteEManual(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "Device type id for which e-manual needs to be deleted") @RequestParam(value = "deviceTypeId") Long deviceTypeId)
	{
		try
		{
			String roleCode = JWTHelper.parseJWTClaim(jwtToken, "roleCode");
			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
			userManualProcessor.deleteManual(roleCode, oemId, deviceTypeId);
			JSONObject res = new JSONObject().put("success", true).put("msg", "EManual Deleted.");
			return ResponseEntity.status(HttpStatus.OK).body(res.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject res = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res.toString());
		}
	}
}