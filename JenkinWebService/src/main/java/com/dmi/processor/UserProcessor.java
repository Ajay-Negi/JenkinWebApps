package com.dmi.processor;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.constant.Constants;
import com.dmi.dao.INotificationDAO;
import com.dmi.dao.IOemDAO;
import com.dmi.dao.ITncDAO;
import com.dmi.dao.IUomDAO;
import com.dmi.dao.IUserDAO;
import com.dmi.dao.model.Email;
import com.dmi.dao.model.NotificationContact;
import com.dmi.dao.model.Oem;
import com.dmi.dao.model.Role;
import com.dmi.dao.model.Sms;
import com.dmi.dao.model.Tnc;
import com.dmi.dao.model.Uom;
import com.dmi.dao.model.User;
import com.dmi.dao.model.UserStatus;
import com.dmi.exception.ProcessorException;
import com.dmi.security.PasswordAuthentication;
import com.dmi.utils.EmailSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Ajay Negi
 *
 */
@Service
public class UserProcessor
{
	
	private static final Logger LOG = Logger.getLogger(UserProcessor.class);
	
	@Autowired
	IUserDAO userDAO;
	@Autowired
	INotificationDAO notificationDAO;
	@Autowired
	IUomDAO uomDAO;
	@Autowired
	ITncDAO tncDAO;
	@Autowired
	IOemDAO oemDAO;
	@Autowired
	OemProcessor oemProcessor;
	@Autowired
	ProfileProcessor profileProcessor;
	@Autowired
	EmailConfirmationProcessor emailConfirmationProcessor;
	@Autowired
	EmailSender emailSender;
	@Autowired
	PasswordAuthentication passwordAuthentication;

	@Transactional(rollbackFor = Exception.class)
	public void register(String username, String password, String primaryEmail, String firstName,
			String lastName, Long oemId, String countryCode, String mobileNumber, long roleId, long tncId) throws ProcessorException
	{
		try
		{
			// check availability of username
			profileProcessor.checkUsernameAvailability(username);

			// check availability of emailId
			profileProcessor.checkEmailAvailability(primaryEmail);

			// check oem availability
			oemProcessor.checkOemAvailbility(oemId);

			User user = new User();
			user.setUsername(username);
			user.setPassword(passwordAuthentication.hash(password.toCharArray()));
			user.setFirstName(firstName);
			user.setLastName(lastName);

			Oem oemBean = new Oem();
			oemBean.setId(oemId);
			user.setOemBean(oemBean);

			Role roleBean = new Role();
			roleBean.setId(roleId);
			user.setRoleBean(roleBean);

			UserStatus userStatus = new UserStatus();
			userStatus.setId(Constants.USER_STATUS_CODE_INACTIVE);
			user.setUserStatusBean(userStatus);
			
			Tnc tncBean=tncDAO.getTncFromId(tncId);
			user.setTncBean(tncBean);
			user.setTncAcceptanceDt(new Date());
			
			//add default UOM
			Uom uomBean=uomDAO.get(Constants.UOM_SI);
			user.setUom(uomBean);

			// add user personal info
			userDAO.registerUser(user);
			Long userId = userDAO.getUserId(username);

			// add user notification info aka User Profile
			profileProcessor.setEmail(primaryEmail, null);
			Sms smsBean = profileProcessor.setSms(countryCode, mobileNumber);

			User userBean = new User();
			userBean.setId(userId);

			Email emailBean = profileProcessor.getEmail(primaryEmail);
			
			
			NotificationContact notificationContact = new NotificationContact();
			notificationContact.setUserBean(userBean);
			notificationContact.setEmailBean(emailBean);
			notificationContact.setSms(smsBean);
			

			notificationDAO.addNotificationInfo(notificationContact);

			emailSender.sendEmailConfirmationMail(
					Constants.JWT_SUBJECT_PRIMARY_EMAIL_CONFIRMATION, username, primaryEmail);
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
	}
	
	
	@Transactional(rollbackFor = Exception.class)
	public void registerAdmin(String username, String password, String primaryEmail, String firstName,
			String lastName, Long oemId, String countryCode, String mobileNumber, long roleId) throws ProcessorException
	{
		try
		{
			// check availability of username
			profileProcessor.checkUsernameAvailability(username);

			// check availability of emailId
			profileProcessor.checkEmailAvailability(primaryEmail);

			// check oem availability
			oemProcessor.checkOemAvailbility(oemId);

			User user = new User();
			user.setUsername(username);
			user.setPassword(passwordAuthentication.hash(password.toCharArray()));
			user.setFirstName(firstName);
			user.setLastName(lastName);

			Oem oemBean = new Oem();
			oemBean.setId(oemId);
			user.setOemBean(oemBean);

			Role roleBean = new Role();
			roleBean.setId(roleId);
			user.setRoleBean(roleBean);

			UserStatus userStatus = new UserStatus();
			userStatus.setId(Constants.USER_STATUS_CODE_INACTIVE);
			user.setUserStatusBean(userStatus);
			
			//add default UOM
			Uom uomBean=uomDAO.get(Constants.UOM_SI);
			user.setUom(uomBean);

			// add user personal info
			userDAO.registerUser(user);
			Long userId = userDAO.getUserId(username);

			// add user notification info aka User Profile
			profileProcessor.setEmail(primaryEmail, null);
			Sms smsBean = profileProcessor.setSms(countryCode, mobileNumber);

			User userBean = new User();
			userBean.setId(userId);

			Email emailBean = profileProcessor.getEmail(primaryEmail);
			
			
			NotificationContact notificationContact = new NotificationContact();
			notificationContact.setUserBean(userBean);
			notificationContact.setEmailBean(emailBean);
			notificationContact.setSms(smsBean);
			

			notificationDAO.addNotificationInfo(notificationContact);

			emailSender.sendEmailConfirmationMail(
					Constants.JWT_SUBJECT_PRIMARY_EMAIL_CONFIRMATION, username, primaryEmail);
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
	}
	
	
	public JSONArray getAllUsersByOem(Long oemId) throws ProcessorException
	{

		List<User> userList = null;
		JSONArray jsonArray = new JSONArray();
		try
		{
			Oem oem = oemDAO.get(oemId);
			userList = userDAO.getAll(oem);
			
			String jsonString = null;
			for(User user : userList)
			{
				
				ObjectMapper objectMapper = new ObjectMapper();
				
				try {
					jsonString = objectMapper.writeValueAsString(user);
				} catch (JsonProcessingException ex) {

					LOG.error(ex.getMessage(),ex);
					
				}
				
				jsonArray.put( new JSONObject(jsonString));
				
			}
		}
		catch (DataAccessException ex)
		{
			LOG.error(ex.getMessage(), ex);
			throw new ProcessorException(ex);
		}
		
		return jsonArray;
	}

}
