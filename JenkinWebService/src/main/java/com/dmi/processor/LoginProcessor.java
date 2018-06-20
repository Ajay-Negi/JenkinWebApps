package com.dmi.processor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dmi.constant.Constants;
import com.dmi.dao.IUserDAO;
import com.dmi.dao.model.User;
import com.dmi.dto.UserDTO;
import com.dmi.exception.ProcessorException;
import com.dmi.security.PasswordAuthentication;

/**
 * 
 * @author Ajay Negi
 *
 */
@Service
public class LoginProcessor
{
	
	private static final Logger LOG = Logger.getLogger(LoginProcessor.class);

	@Autowired
	IUserDAO daasUserDAO;
	@Autowired
	PasswordAuthentication passwordAuthentication;

	public UserDTO getLoginCandidate(String usernm, String password) throws ProcessorException
	{
		UserDTO dto = null;
		try
		{
			User entity=daasUserDAO.getUser(usernm);
			if (entity!=null && passwordAuthentication.authenticate(password.toCharArray(),entity.getPassword()))
			{
				if (entity.getUserStatusBean().getId() == Constants.USER_STATUS_CODE_ACTIVE)
					
					dto = new UserDTO().setUserName(entity.getUsername())
							.setUserId(entity.getId())
							.setRoleCd(entity.getRoleBean().getAlias())
							.setOemId(entity.getOemBean().getId())
							.setOemName(entity.getOemBean().getName())
							.setOemClassificationAlias(entity.getOemBean().getOemClassificationBean().getName())
							.setOemLogoURl(new String(entity.getOemBean().getLogoUrl()))
							.setOemBackgroundURl(entity.getOemBean().getBackGroundImageUrl())
							.setLocationServicesStatus(entity.getOemBean().getLocationServicesStatus());
							
				else
					throw new ProcessorException("Confirm Your Email To Login.");
			}
			else
			{
				throw new ProcessorException("Invalid Credentials!");
			}
		}
		catch (DataAccessException ex)
		{
			LOG.error(ex.getMessage(), ex);
			throw new ProcessorException(ex);
		}
		return dto;
	}
}
