package com.dmi.processor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.dao.IEmailDAO;
import com.dmi.dao.INotificationDAO;
import com.dmi.dao.ISmsDAO;
import com.dmi.dao.IUomDAO;
import com.dmi.dao.IUserDAO;
import com.dmi.dao.model.Email;
import com.dmi.dao.model.NotificationContact;
import com.dmi.dao.model.Sms;
import com.dmi.dao.model.User;
import com.dmi.dto.UomDTO;
import com.dmi.dto.UserDTO;
import com.dmi.exception.ProcessorException;
import com.dmi.security.PasswordAuthentication;

/**
 * 
 * @author Ajay Negi
 *
 */
@Service
public class ProfileProcessor
{

	private static final Logger LOG = Logger.getLogger(ProfileProcessor.class);

	@Autowired
	IEmailDAO emailDAO;
	@Autowired
	IUomDAO uomDAO;
	@Autowired
	ISmsDAO smsDAO;
	@Autowired
	IUserDAO userDAO;
	@Autowired
	INotificationDAO notificationDAO;
	@Autowired
	PasswordAuthentication passwordAuthentication;

	public UserDTO getUserProfile(String username) throws ProcessorException
	{
		UserDTO userDTO = new UserDTO();
		UomDTO uomDTO = new UomDTO();
		try
		{
			User user = userDAO.getUser(username);

			uomDTO.setId(user.getUom().getId());
			uomDTO.setUom(user.getUom().getAlias());

			userDTO.setFirstName(user.getFirstName());
			userDTO.setLastName(user.getLastName());
			userDTO.setUserId(user.getId());
			userDTO.setUserName(user.getUsername());
			userDTO.setOemId(user.getOemBean().getId());
			userDTO.setOemName(user.getOemBean().getName());
			userDTO.setAddress(user.getAddress());
			userDTO.setUomDTO(uomDTO);
			userDTO.setPrimaryEmail(user.getNotificationContact().getEmailBean().getPrimaryEmail());
			userDTO.setAlternateEmail(user.getNotificationContact().getEmailBean().getAlternateEmail());
			try
			{
				userDTO.setCountryCode(user.getNotificationContact().getSms().getCountryCode());
				userDTO.setMobileNumber(user.getNotificationContact().getSms().getMobileNumber());
			}
			catch (Exception e)
			{
				LOG.error(e.getMessage(), e);
			}
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
		return userDTO;
	}

	@Transactional(rollbackFor = Exception.class)
	public void saveUserProfile(String username, String firstName, String lastName, String address, String primaryEmail,
			String alternateEmail, String countryCode, String mobileNumber) throws ProcessorException
	{
		try
		{
			User user = userDAO.getUser(username);
			//Uom uomBean = uomDAO.get(uom);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setAddress(address);
			//user.setUom(uomBean);
			userDAO.updateUser(user);

			Email email = new Email();
			email.setId(user.getNotificationContact().getEmailBean().getId());
			email.setPrimaryEmail(primaryEmail);
			email.setAlternateEmail(alternateEmail);
			emailDAO.updateEmailInfo(email);

			Sms sms = user.getNotificationContact().getSms();

			if (countryCode == null || mobileNumber == null)
			{
				if (sms != null)
				{
					//make sms column null in notification contact table
					NotificationContact notificationContact = user.getNotificationContact();
					notificationContact.setSms(null);
					notificationDAO.updateNotificationInfo(notificationContact);
					
					//then delete sms entry in sms table
					smsDAO.remove(sms);
				}
			}
			else
			{
				Sms newSms = new Sms();
				newSms.setCountryCode(countryCode);
				newSms.setMobileNumber(mobileNumber);
				try
				{
					newSms.setId(sms.getId());
					smsDAO.updateSmsInfo(newSms);
				}
				catch (NullPointerException e)
				{
					Sms addedSmsBean = smsDAO.addSmsInfo(newSms);
					NotificationContact notificationContact = user.getNotificationContact();
					notificationContact.setSms(addedSmsBean);
					notificationDAO.updateNotificationInfo(notificationContact);
				}
			}
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e);
		}
	}

	@Transactional
	public void passwordReset(String username, String oldPassword, String newPassword) throws ProcessorException
	{
		try
		{
			User user = userDAO.getUser(username);
			if (passwordAuthentication.authenticate(oldPassword.toCharArray(), user.getPassword()))
				userDAO.updatePassword(user.getId(), passwordAuthentication.hash(newPassword.toCharArray()));
			else
				throw new ProcessorException("Incorrect Old Password!");
		}
		catch (Exception e)
		{
			throw new ProcessorException(e);
		}
	}

	@Transactional
	public void passwordReset(String username, String password) throws ProcessorException
	{
		try
		{
			User user = userDAO.getUser(username);
			userDAO.updatePassword(user.getId(), passwordAuthentication.hash(password.toCharArray()));
		}
		catch (Exception e)
		{
			throw new ProcessorException(e);
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/* HELPER METHODS */
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void checkUsernameAvailability(String username) throws ProcessorException
	{
		if (userDAO.checkUsernameAvailability(username))
			throw new ProcessorException("Username already taken.");
	}

	public void checkEmailAvailability(String primaryEmail) throws ProcessorException
	{
		if (emailDAO.checkEmailAvailability(primaryEmail))
			throw new ProcessorException("Email already registered with another account.");
	}

	public void setEmail(String primaryEmail, String alternateEmail)
	{
		Email email = new Email();
		email.setPrimaryEmail(primaryEmail);
		if (alternateEmail != null)
			email.setAlternateEmail(alternateEmail);

		// add notification info
		emailDAO.addEmailInfo(email);
	}

	public Email getEmail(String primaryEmail)
	{
		Email registerdEmail = emailDAO.getEmailInfo(primaryEmail);
		return registerdEmail;

	}

	public Sms setSms(String countryCode, String mobileNumber) throws ProcessorException
	{
		Sms sms = new Sms();
		sms.setCountryCode(countryCode);
		sms.setMobileNumber(mobileNumber);
		
		//Sms smsBean = smsDAO.getSmsInfo(mobileNumber);
		
		/*if(smsBean!=null)
		{
			throw new ProcessorException("Mobile Number is already registered");
		}
		else
		{
			
		}*/
		
		// add notification info
		Sms smsBean = smsDAO.addSmsInfo(sms);
		
		return smsBean;

		
	}

	public Sms getSms(String mobileNumber)
	{
		return smsDAO.getSmsInfo(mobileNumber);
	}

}
