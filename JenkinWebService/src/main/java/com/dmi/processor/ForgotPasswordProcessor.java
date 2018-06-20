package com.dmi.processor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.constant.Constants;
import com.dmi.dao.IUserDAO;
import com.dmi.dao.model.User;
import com.dmi.exception.ProcessorException;
import com.dmi.security.JWTHelper;
import com.dmi.utils.EmailSender;

/**
 * 
 * @author ANegi
 *
 */

@Service
public class ForgotPasswordProcessor
{
	@Autowired
	EmailSender emailSender;
	@Autowired
	IUserDAO userDAO;
	@Autowired
	ProfileProcessor profileProcessor;
	
	private static final Logger LOG = Logger.getLogger(ForgotPasswordProcessor.class);

	public void sendPasswordResetEmail(String username) throws ProcessorException
	{
		try
		{
			User user = userDAO.getUser(username);
			if (user != null)
				emailSender.sendPasswordResetMail(Constants.JWT_SUBJECT_FORGOT_PASSWORD, username,
						user.getNotificationContact().getEmailBean().getPrimaryEmail());
			else
				throw new ProcessorException("No registered user found with given username and email Id.");
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
	}

	public void resetPassword(String jwt, String password) throws ProcessorException
	{
		try
		{
			String username = JWTHelper.parseJWTClaim(jwt, "username");

			profileProcessor.passwordReset(username, password);
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}

	}

}
