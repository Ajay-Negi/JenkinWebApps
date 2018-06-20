package com.dmi.processor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.constant.Constants;
import com.dmi.dao.IUserDAO;
import com.dmi.dao.model.User;
import com.dmi.dao.model.UserStatus;
import com.dmi.exception.ProcessorException;
import com.dmi.security.JWTHelper;
import com.dmi.utils.EmailSender;

/**
 * 
 * @author ANegi
 *
 */

@Service
public class EmailConfirmationProcessor
{
	@Autowired
	IUserDAO userDAO;
	@Autowired
	EmailSender emailSender;
	
	private static final Logger LOG = Logger.getLogger(EmailConfirmationProcessor.class);

	public void sendVerificationEmail(String username, String email) throws ProcessorException
	{
		try
		{
			User user = userDAO.getUser(username);
			if (user != null && user.getNotificationContact().getEmailBean().getPrimaryEmail()
					.equalsIgnoreCase(email))
				emailSender.sendEmailConfirmationMail(
						Constants.JWT_SUBJECT_PRIMARY_EMAIL_CONFIRMATION, username, email);
			else
				throw new ProcessorException(
						"No registered user found with given username and email Id.");
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
	}

	@Transactional
	public void confirmEmail(String jwt) throws ProcessorException
	{
		try
		{
			String username = JWTHelper.parseJWTClaim(jwt, "username");

			User user = userDAO.getUser(username);
			UserStatus userStatusBean = new UserStatus();
			userStatusBean.setId(Constants.USER_STATUS_CODE_ACTIVE);
			user.setUserStatusBean(userStatusBean);

			userDAO.updateUser(user);

		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
	}

}
