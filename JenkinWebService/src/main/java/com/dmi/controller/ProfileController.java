package com.dmi.controller;

import org.apache.log4j.Logger;
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

import com.dmi.dto.UserDTO;
import com.dmi.exception.ProcessorException;
import com.dmi.exception.ValidationException;
import com.dmi.processor.ProfileProcessor;
import com.dmi.security.JWTHelper;
import com.dmi.utils.ValidationHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author Ajay Negi
 */
@Api(tags = "User Profile")
@RestController
@RequestMapping(value = "/userProfile")
public class ProfileController
{
	
	private static final Logger LOG = Logger.getLogger(ProfileController.class);
	
	@Autowired
	ProfileProcessor profileProcessor;

	@ApiOperation(value = "Get profile info of a user.", notes = "API to fetch the details of a user from the JWT token provided", response = UserDTO.class)
	@RequestMapping(value = "/get", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getUserProfile(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken)
	{

		try
		{
			String username = JWTHelper.parseJWTClaim(jwtToken, "userName");
			UserDTO userDTO = profileProcessor.getUserProfile(username);
			JSONObject result = new JSONObject().put("success", true)
					.put("msg", "User Profile Retrieved.").put("response", new JSONObject(userDTO));
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	

	}

	@ApiOperation(value = "Update profile of a user.", notes = "API to fetch user details which needs to be updated")
	@RequestMapping(value = "/update", method = RequestMethod.PUT, produces = "application/json", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> saveUserProfile(
			@ApiParam(value = "JWT authorization token") @RequestHeader(required = true, value = HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "First Name") @RequestParam(required = true, value = "firstName") String firstName,
			@ApiParam(value = "Last Name") @RequestParam(required = true, value = "lastName") String lastName,
			@ApiParam(value = "Address") @RequestParam(required = true, value = "address") String address,
			@ApiParam(value = "Primary email addresss") @RequestParam(required = true, value = "primaryEmail") String primaryEmail,
			@ApiParam(value = "Alternate email address") @RequestParam(required = false, value = "alternateEmail") String alternateEmail,
			@ApiParam(value = "Country code") @RequestParam(required = false, value = "countryCode") String countryCode,
			@ApiParam(value = "Mobile number of the user") @RequestParam(required = false, value = "mobileNumber") String mobileNumber)
	{
		//TODO Email verification of alternateEMail
		//TODO Mobile Number Verification
		//TODO Check if primary Email is changed, if yes, then log user out and re-confirm the new Email. 
		try
		{
			String userName = JWTHelper.parseJWTClaim(jwtToken, "userName");
			profileProcessor.saveUserProfile(userName, firstName, lastName, address, primaryEmail,
					alternateEmail, countryCode, mobileNumber);
			JSONObject result = new JSONObject().put("success", true).put("msg",
					"User Profile Updated.");
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

	@ApiOperation(value = "Change password of a logged-in user.", notes = "API to change password.Provide old and new passoword to update the password")
	@RequestMapping(value = "/changePassword", method = RequestMethod.PUT, produces = "application/json", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> changePassword(
			@ApiParam(value = "JWT authorization token") @RequestHeader(required = true, value = HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "Old user's password") @RequestParam(required = true, value = "oldPassword") String oldPassword,
			@ApiParam(value = "User's new password") @RequestParam(required = true, value = "password") String password,
			@ApiParam(value = "User's new confirm password") @RequestParam(required = true, value = "confirmPwd") String confirmPwd)
	{
		try
		{
			// server side password matching
			if (!password.equals(confirmPwd))
				throw new ProcessorException("New passwords do not match!");

			// Validate server-side
			try
			{
				ValidationHelper.validate(password, ValidationHelper.PASSWORD);
			}
			catch (ValidationException e)
			{
				LOG.error(e.getMessage(),e);
				throw new Exception(e.getMessage());
			}

			String username = JWTHelper.parseJWTClaim(jwtToken, "userName");
			profileProcessor.passwordReset(username, oldPassword, password);
			JSONObject result = new JSONObject().put("success", true).put("msg",
					"Password Changed.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	
	}
}
