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
import com.dmi.dao.IOemDAO;
import com.dmi.dao.model.User;
import com.dmi.exception.ProcessorException;
import com.dmi.exception.ValidationException;
import com.dmi.processor.ProfileProcessor;
import com.dmi.processor.UserProcessor;
import com.dmi.security.JWTHelper;
import com.dmi.utils.ValidationHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author Ajay Negi
 *
 */

@Api(tags = "User Registration")
@RestController
@RequestMapping(value = "/userRegistration")
public class UserRegistrationController
{
	
	private static final Logger LOG = Logger.getLogger(UserRegistrationController.class);
	
	@Autowired
	public UserProcessor userProcessor;
	@Autowired
	public ProfileProcessor profileProcessor;
	@Autowired
	public IOemDAO oemDAO;
	

	@ApiOperation(value = "Register a new User.", notes = "API to register a new user")
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> registerUser(
			@ApiParam(value = "OemId") @RequestParam(required = true, value = "oemId") Long oemId,
			@ApiParam(value = "First Name") @RequestParam(required = true, value = "firstName") String firstName,
			@ApiParam(value = "Last Name") @RequestParam(required = true, value = "lastName") String lastName,
			@ApiParam(value = "Unique username of user") @RequestParam(required = true, value = "username") String username,
			@ApiParam(value = "Password for the user login") @RequestParam(required = true, value = "password") String password,
			@ApiParam(value = "Confirm password string") @RequestParam(required = true, value = "confirmPwd") String confirmPwd,
			@ApiParam(value = "Primary Email") @RequestParam(required = true, value = "primaryEmail") String primaryEmail,
			@ApiParam(value = "Country code of the user") @RequestParam(required = true, value = "countryCode") String countryCode,
			@ApiParam(value = "Mobile number of the user") @RequestParam(required = true, value = "mobileNumber") String mobileNumber,
			@ApiParam(value = "TNC acceptance boolean value") @RequestParam(required = true, value = "tncAccept") Boolean tncAccept,
			@ApiParam(value = "TNC id") @RequestParam(required = true, value = "tncId") Long tncId)
	{

		try
		{
			// Validate server-side
			try
			{
				ValidationHelper.validate(username, ValidationHelper.NAME);
				ValidationHelper.validate(password, ValidationHelper.PASSWORD);
				ValidationHelper.validate(primaryEmail, ValidationHelper.EMAIL);
			}
			catch (ValidationException e)
			{
				LOG.error(e.getMessage(),e);
			}

			// server side password matching
			if (!password.equals(confirmPwd))
				throw new Exception("Passwords do not match!");

			// Register the user 
			long roleId = Constants.USER_ROLE_CODE_NORMAL;
			
			// Register the user if he has accepted Terms And Conditions
			if (tncAccept)
				userProcessor.register(username, password, primaryEmail, firstName,
						lastName, oemId, countryCode, mobileNumber, roleId, tncId);
			else
				throw new Exception("Please accept Terms And Conditions before continuing.");
			
			
		
			// Return the response
			JSONObject result = new JSONObject().put("success", true).put("msg",
					"Registration successful. Confirm Email and Login to continue.");

			return ResponseEntity.ok().body(result.toString());

		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		
	}
	
	
	@ApiOperation(value = "Register a new Admin User.", notes = "API to register a admin new user")
	@RequestMapping(value = "/registerAdmin", method = RequestMethod.POST, produces = "application/json", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> registerAdminUser(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "OemId") @RequestParam(required = true, value = "oemId") Long oemId,
			@ApiParam(value = "First Name") @RequestParam(required = true, value = "firstName") String firstName,
			@ApiParam(value = "Last Name") @RequestParam(required = true, value = "lastName") String lastName,
			@ApiParam(value = "Unique username of user") @RequestParam(required = true, value = "username") String username,
			@ApiParam(value = "Password for the user login") @RequestParam(required = true, value = "password") String password,
			@ApiParam(value = "Confirm password string") @RequestParam(required = true, value = "confirmPwd") String confirmPwd,
			@ApiParam(value = "Primary Email") @RequestParam(required = true, value = "primaryEmail") String primaryEmail,
			@ApiParam(value = "Country code of the user") @RequestParam(required = true, value = "countryCode") String countryCode,
			@ApiParam(value = "Mobile number of the user") @RequestParam(required = true, value = "mobileNumber") String mobileNumber)
	{

		try
		{
			
			String roleCode = JWTHelper.parseJWTClaim(jwtToken, "roleCode");
			
			if(!roleCode.equalsIgnoreCase(Constants.USER_ROLE_CODE_SUPERADMIN))
				throw new ProcessorException("You don't have super admin permissions to add a new admin user");
			
			
			// Validate server-side
			try
			{
				ValidationHelper.validate(username, ValidationHelper.NAME);
				ValidationHelper.validate(password, ValidationHelper.PASSWORD);
				ValidationHelper.validate(primaryEmail, ValidationHelper.EMAIL);
			}
			catch (ValidationException e)
			{
				LOG.error(e.getMessage(),e);
			}

			// server side password matching
			if (!password.equals(confirmPwd))
				throw new Exception("Passwords do not match!");
			
			// Register the user  
			long roleId = Constants.ADMINUSER_ROLE_CODE;
			
		
			userProcessor.registerAdmin(username, password, primaryEmail, firstName,
					lastName, oemId, countryCode, mobileNumber, roleId);
		
						
		
			// Return the response
			JSONObject result = new JSONObject().put("success", true).put("msg",
					"Registration successful. Confirm Email and Login to continue.");

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
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		
	}
	
	
	@ApiOperation(value = "Get all users in an oem.", notes="API to get all users registered for an oem",response = User.class, responseContainer = "List")
	@RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getServices(
				@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken)
	{
		try
		{		
			Long oemId = new Long(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
			
			JSONArray response = userProcessor.getAllUsersByOem(oemId);
			
			JSONObject result = new JSONObject().put("success", true).put("response", response)
					.put("msg", "Users of oem retrieved.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (ProcessorException e)
		{
			LOG.error(e.getMessage(), e);
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
}
