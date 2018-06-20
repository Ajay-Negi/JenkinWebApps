package com.dmi.controller;

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

import com.dmi.constant.Constants;
import com.dmi.dao.model.NotificationChannel;
import com.dmi.dao.model.NotificationTemplate;
import com.dmi.dao.model.NotificationType;
import com.dmi.dto.NotificationTemplateDTO;
import com.dmi.exception.ProcessorException;
import com.dmi.processor.NotificationTemplateProcessor;
import com.dmi.processor.VhrProcessor;
import com.dmi.security.JWTHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author ANegi
 *
 */
@Api(tags = "Notification Template Manager")
@RestController
@RequestMapping(value="/notificationTemplate")
public class NotificationTemplateController
{
	
	private static final Logger LOG = Logger.getLogger(NotificationTemplateController.class);
	
	@Autowired
	public NotificationTemplateProcessor notificationTemplateProcessor;
	
	@Autowired
	public VhrProcessor vhrTemplateProcessor;

	@ApiOperation(value = "Get all Notification Templates", notes = "API to fetch all notification templates of a OEM")
	@RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getNotificationTemplates(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken)
	{
		try
		{
			long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
			JSONArray response = notificationTemplateProcessor.getAll(oemId);
			JSONObject result = new JSONObject().put("success", true).put("response", response)
					.put("msg", "Notification Templates Retrieved.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}
	
	@ApiOperation(value = "Get notification template by channel type", notes = "API to fetch all template for a particular notification channel i.e. sms, email ",
			response = NotificationTemplate.class, responseContainer = "List")
	@RequestMapping(value = "/getByChannel", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getTemplateByChannel(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "Notication channel type eg. email, sms") @RequestParam String notificationChannel) {

		JSONArray notificationTemplateList = null;
		
		try {
			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
					
			notificationTemplateList = notificationTemplateProcessor.getTemplatesByChannel(notificationChannel, oemId);
			JSONObject result = new JSONObject().put(Constants.SUCCESS, true)
					.put(Constants.RESPONSE, notificationTemplateList);
			
			return ResponseEntity.ok().body(result.toString());
			
		} 
		catch (ProcessorException ex)
		{
			LOG.error(ex);
			JSONObject result = new JSONObject().put(Constants.SUCCESS, false).put(
					"msg", ex.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(
					result.toString());
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(),ex);
			JSONObject result = new JSONObject().put(Constants.SUCCESS, false).put(
					"msg", ex.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(
					result.toString());
		}
	}
	
	@ApiOperation(value = "Save a Notification Template", notes = "API to save notification template for VHR and other templates")
	@RequestMapping(value = "/save", method = RequestMethod.POST,  produces = "application/json",headers = "Accept=application/json")
	public ResponseEntity<String> saveNotificationTemplate(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "JSON string which contain notification template details") @RequestBody NotificationTemplateDTO notificationTemplateDTO)
	{
		try
		{
			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
			String userName = JWTHelper.parseJWTClaim(jwtToken, "userName");
			
			if(notificationTemplateDTO.getDeviceTypeId() == null)
				notificationTemplateProcessor.save(oemId, userName, notificationTemplateDTO);
			else
				vhrTemplateProcessor.save(userName, notificationTemplateDTO, oemId);
			
			JSONObject result = new JSONObject().put("success", true).put("msg",
					"Notification Template Saved.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (ProcessorException ex)
		{
			LOG.error(ex.getMessage(),ex);
			JSONObject result = new JSONObject().put("success", false).put("msg", ex.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}

	@ApiOperation(value = "Update a Notification Template", notes= "API to update notification template")
	@RequestMapping(value = "/update", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<String> updateNotificationTemplate(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "JSON string which contain notification template details including tempate id") @RequestBody NotificationTemplateDTO notificationTemplateDTO)
	{
		try
		{
			String userName = JWTHelper.parseJWTClaim(jwtToken, "userName");
			if(notificationTemplateDTO.getDeviceTypeId() == null)
				notificationTemplateProcessor.update(userName, notificationTemplateDTO);
			else
				vhrTemplateProcessor.update(userName, notificationTemplateDTO);
			
			
			JSONObject result = new JSONObject().put("success", true).put("msg",
					"Notification Template Updated.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (ProcessorException ex)
		{
			LOG.error(ex.getMessage(),ex);
			JSONObject result = new JSONObject().put("success", false).put("msg", ex.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}

	@ApiOperation(value = "Delete a Notification Template", notes = "API to delete notification template for provided template id")
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<String> deleteNotificationTemplate(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "Notification template id which needs to be deleted") @RequestParam(required = true, value = "templateId") Long notificationTemplateId)
	{
		try
		{
			String userName = JWTHelper.parseJWTClaim(jwtToken, "userName");
			notificationTemplateProcessor.delete(userName, notificationTemplateId);
			JSONObject result = new JSONObject().put("success", true).put("msg",
					"Notification Template Deleted.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (ProcessorException e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(),ex);
			JSONObject result = new JSONObject().put("success", false).put("msg", ex.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}
	
	
	@ApiOperation(value = "Get All Notification Types", notes = "API to fetch all notification types eg. Alert,Geofence ", response = NotificationType.class, responseContainer = "List")
	@RequestMapping(value = "/getNotificationTypes", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getNotificationTypes()
	{
		JSONArray notificationTypeList = null;
		
		try {
					
			notificationTypeList = notificationTemplateProcessor.getNotificationTypes();
			JSONObject result = new JSONObject().put(Constants.SUCCESS, true)
					.put(Constants.RESPONSE, notificationTypeList);
			
			return ResponseEntity.ok().body(result.toString());
			
		} 
		catch (ProcessorException ex)
		{
			LOG.error(ex);
			JSONObject result = new JSONObject().put(Constants.SUCCESS, false).put(
					"msg", ex.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(
					result.toString());
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(),ex);
			JSONObject result = new JSONObject().put(Constants.SUCCESS, false).put(
					"msg", ex.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(
					result.toString());
		}
		
		
	}


	@ApiOperation(value = "Get All Notification Channels",notes = "API to fetch all notification channels eg. SMS, Email", response = NotificationChannel.class, responseContainer = "List")
	@RequestMapping(value = "/getNotificationChannels", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getNotificationChannels()
	{
		
		JSONArray notificationChannelList = null;
		
		try {
					
			notificationChannelList = notificationTemplateProcessor.getNotificationChannels();
			JSONObject result = new JSONObject().put(Constants.SUCCESS, true)
					.put(Constants.RESPONSE, notificationChannelList);
			
			return ResponseEntity.ok().body(result.toString());
			
		} 
		catch (ProcessorException ex)
		{
			LOG.error(ex);
			JSONObject result = new JSONObject().put(Constants.SUCCESS, false).put(
					"msg", ex.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(
					result.toString());
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(),ex);
			JSONObject result = new JSONObject().put(Constants.SUCCESS, false).put(
					"msg", ex.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(
					result.toString());
		}
		
	}
}
