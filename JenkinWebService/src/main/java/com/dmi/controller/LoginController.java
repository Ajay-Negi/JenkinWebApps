package com.dmi.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dmi.dao.IRedisDAO;
import com.dmi.dto.UserDTO;
import com.dmi.exception.ValidationException;
import com.dmi.processor.LoginProcessor;
import com.dmi.security.JWTTokenService;
import com.dmi.utils.ValidationHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author Ajay Negi
 *
 */

@Api(tags = "User Login")
@RestController
public class LoginController
{
	
	private static final Logger LOG = Logger.getLogger(LoginController.class);
	
	@Autowired
	LoginProcessor loginProcessor;
	
	@Autowired
	IRedisDAO redisDAO;
	
	

	@ApiOperation(value = "Checks for User authentication.", notes = "Provide username and password for user authentication")
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> loginUser(
			@ApiParam(value = "Username") @RequestParam(required = true, value = "username") String username,
			@ApiParam(value = "Password") @RequestParam(required = true, value = "password") String password)
	{
		
		try
		{
			UserDTO user = new UserDTO().setUserName(username).setPassword(
					password);

			// Validate server-side
			try
			{
				ValidationHelper.validate(username, ValidationHelper.NAME);
				ValidationHelper.validate(password, ValidationHelper.PASSWORD);
			}
			catch (ValidationException e)
			{
				LOG.error(e.getMessage(),e);
				throw new Exception(e.getMessage());
			}

			// Authenticate the user using the credentials provided
			user = loginProcessor.getLoginCandidate(user.getUserName().toLowerCase(),
					user.getPassword());

			// Issue a token for the user
			String token = JWTTokenService.issueToken(user);
			
			if (token.trim().isEmpty())
				throw new Exception(
						"Error occured while processing login request.Try again..");

			// Return the token on the response
			JSONObject result = new JSONObject().put("success", true)
					.put("brandLogoUrl", user.getOemLogoURl())
					.put("backgroundImageUrl", user.getOemBackgroundURl())
					.put("msg", "Login successful.").put("token", token);

			return ResponseEntity.ok().body(result.toString());

		}
		catch (Exception e)
		{

			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put(
					"msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(
					result.toString());
		}

	}
	
	@ApiOperation(value = "Redis function.", notes = "Test method")
	@RequestMapping(value = "/test", method = RequestMethod.POST, produces = "application/json", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> test()
	{
		
		Map<String, String> hmap = new HashMap<String, String>();
		
		hmap.put("abc_123", "12345value");
		redisDAO.save("testMap", hmap);
		return ResponseEntity.status(HttpStatus.OK).body("success");
	}

}
