package com.dmi.processor;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.constant.Constants;
import com.dmi.dao.INotificationChannelDAO;
import com.dmi.dao.INotificationTemplateDAO;
import com.dmi.dao.INotificationTypeDAO;
import com.dmi.dao.IOemDAO;
import com.dmi.dao.ISubServiceDAO;
import com.dmi.dao.IUserDAO;
import com.dmi.dao.model.NotificationChannel;
import com.dmi.dao.model.NotificationTemplate;
import com.dmi.dao.model.NotificationType;
import com.dmi.dao.model.Oem;
import com.dmi.dao.model.SubService;
import com.dmi.dto.NotificationTemplateDTO;
import com.dmi.exception.ProcessorException;
import com.dmi.utils.ReplaceUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author ANegi
 *
 */
@Service
public class NotificationTemplateProcessor
{
	
	private static final Logger LOG = Logger.getLogger(NotificationTemplateProcessor.class);
	
	@Autowired
	IUserDAO userDAO;
	@Autowired
	IOemDAO oemDAO;
	@Autowired
	INotificationTemplateDAO notificationTemplateDAO;
	@Autowired
	INotificationChannelDAO notificationChannelDAO;
	@Autowired
	INotificationTypeDAO notificationTypeDAO;
	@Autowired
	ISubServiceDAO subServiceDAO;

	public JSONArray getAll(long oemId) throws ProcessorException
	{
		JSONArray notificationTemplates = new JSONArray();
	
		try
		{
			Oem oem = oemDAO.get(oemId);
			List<NotificationTemplate> notificationTemplateList  = notificationTemplateDAO.getAll(oem);
			
			if( !notificationTemplateList.isEmpty() ){
				
					String jsonString;
					JSONObject jsonVObj;
					ObjectMapper objectMapper = new ObjectMapper();
					
					for(NotificationTemplate notificationTemplate : notificationTemplateList){
						try {
							
							jsonString = objectMapper.writeValueAsString(notificationTemplate);
							jsonVObj = new JSONObject(jsonString);
							notificationTemplates.put(jsonVObj);
							
						} catch (JsonProcessingException ex) {
							LOG.error(ex.getMessage(),ex);
						}	
					}
			
			}
		} catch(Exception ex){
			LOG.error(ex.getMessage(),ex);
		}
		
		return notificationTemplates;
			
	}

	@Transactional
	public void save(Long oemId, String username, NotificationTemplateDTO notificationTemplateDTO)
			throws ProcessorException
	{
		try
		{
			// check if user is admin
			if (!userDAO.getUser(username).getRoleBean().getAlias()
					.equalsIgnoreCase(Constants.USER_ROLE_CODE_ADMIN))
				throw new Exception("Only Admin can add new Notification Template.");
			
			NotificationTemplate checkBean = notificationTemplateDAO.getBySubserviceAndChannel(notificationTemplateDTO.getSubService(), notificationTemplateDTO.getChannel());

			if(checkBean != null)
				throw new ProcessorException("Template already exist for subservice and channel");
			
			if(notificationTemplateDTO.getContent() == null)
				throw new ProcessorException("No email or sms content is provided");
			
			String content = ReplaceUtil.replaceString(notificationTemplateDTO.getContent());
			
			Long notificationChannelId = notificationTemplateDTO.getChannel();
			Long notificationTypeId = notificationTemplateDTO.getType();
			String templateName = notificationTemplateDTO.getName();

			NotificationTemplate notificationTemplate = new NotificationTemplate();
			Oem oem = oemDAO.get(oemId);
			NotificationType notificationType = notificationTypeDAO.get(notificationTypeId);
			NotificationChannel notificationChannel = notificationChannelDAO.get(notificationChannelId);
			Long subServiceId = notificationTemplateDTO.getSubService();
			
			
			if (subServiceId != null)
			{
				SubService subService = subServiceDAO.get(subServiceId);
				notificationTemplate.setSubServiceBean(subService);
			}
			else{
				throw new ProcessorException("Subservice is not valid");
			}
			notificationTemplate.setContent(content.getBytes());
			notificationTemplate.setName(templateName);
			notificationTemplate.setNotificationChannelBean(notificationChannel);
			notificationTemplate.setNotificationTypeBean(notificationType);
			notificationTemplate.setOemBean(oem);
			notificationTemplateDAO.save(notificationTemplate);
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
	}

	@Transactional
	public void update(String username, NotificationTemplateDTO notificationTemplateDTO)
			throws ProcessorException
	{
		try
		{
			// check if user is admin
			if (!userDAO.getUser(username).getRoleBean().getAlias()
					.equalsIgnoreCase(Constants.USER_ROLE_CODE_ADMIN))
				throw new Exception("Only Admin can update Notification Template.");

			NotificationTemplate notificationTemplate = notificationTemplateDAO
					.get(notificationTemplateDTO.getId());
			
			String content = ReplaceUtil.replaceString(notificationTemplateDTO.getContent());
			
			notificationTemplate.setContent(content.getBytes());
			notificationTemplateDAO.update(notificationTemplate);
		}catch (NullPointerException npe)
		{
			LOG.error(npe.getMessage(), npe);
			throw new ProcessorException("Either username or template Id is not valid");
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}

	}

	@Transactional
	public void delete(String username, Long notificationTemplateId) throws ProcessorException
	{
		try
		{
			// check if user is admin
			if (!userDAO.getUser(username).getRoleBean().getAlias()
					.equalsIgnoreCase(Constants.USER_ROLE_CODE_ADMIN))
				throw new Exception("Only Admin can update Notification Template.");

			NotificationTemplate notificationTemplate = notificationTemplateDAO.get(notificationTemplateId);
			if(notificationTemplate == null)
				throw new ProcessorException("Template with given Id doesn't exist");
			
			notificationTemplateDAO.delete(notificationTemplate);
		}catch (ProcessorException pe)
		{
			LOG.error(pe.getMessage(), pe);
			throw new ProcessorException(pe.getMessage());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException("This notification template with id: "+ notificationTemplateId + " is being used in some rule created");
		}

	}
	
	/**
	 * 
	 * @return all notification types i.e. alert, welcome etc
	 * @throws ProcessorException
	 */
	public JSONArray getNotificationTypes() throws ProcessorException
	{
		List<NotificationType> notificationTypes;
		JSONArray jsonArray = new JSONArray();
		notificationTypes = notificationTypeDAO.getAll();
		
		
		if(!notificationTypes.isEmpty()){
			
			String jsonString = null;
			for(NotificationType notificationType : notificationTypes)
			{
				
				ObjectMapper objectMapper = new ObjectMapper();
				
				try {
					jsonString = objectMapper.writeValueAsString(notificationType);
				} catch (JsonProcessingException ex) {

					LOG.error(ex.getMessage(),ex);
					
				}
				
				jsonArray.put( new JSONObject(jsonString));
				
			}
			
			
			return jsonArray;
		}
		else
		{
			throw new ProcessorException("No notificationType exists");
		}
	}
	
	/**
	 * 
	 * @return all notification channels i.e. sms, email
	 * @throws ProcessorException
	 */
	public JSONArray getNotificationChannels() throws ProcessorException
	{
		List<NotificationChannel> notificationChannels;
		JSONArray jsonArray = new JSONArray();
		notificationChannels = notificationChannelDAO.getAll();
		
		
		if(!notificationChannels.isEmpty()){
			
			String jsonString = null;
			for(NotificationChannel notificationChannel : notificationChannels)
			{
				
				ObjectMapper objectMapper = new ObjectMapper();
				
				try {
					jsonString = objectMapper.writeValueAsString(notificationChannel);
				} catch (JsonProcessingException ex) {

					LOG.error(ex.getMessage(),ex);
					
				}
				
				jsonArray.put( new JSONObject(jsonString));
				
			}
			
			
			return jsonArray;
		}
		else
		{
			throw new ProcessorException("No notification channel exists");
		}
	}

	
	public JSONArray getTemplatesByChannel(String channel, Long oemId) throws ProcessorException
	{
		List<NotificationTemplate> notificationTemplates;
		JSONArray jsonArray = new JSONArray();
		notificationTemplates = notificationTemplateDAO.getByChannel(channel, oemId);
		
		
		if(!notificationTemplates.isEmpty()){
			
			String jsonString = null;
			for(NotificationTemplate notificationtemplate : notificationTemplates)
			{
				
				ObjectMapper objectMapper = new ObjectMapper();
				
				try {
					jsonString = objectMapper.writeValueAsString(notificationtemplate);
				} catch (JsonProcessingException ex) {

					LOG.error(ex.getMessage(),ex);
					
				}
				
				jsonArray.put( new JSONObject(jsonString));
				
			}
			
			
			return jsonArray;
		}
		else
		{
			throw new ProcessorException("No notification templates exists for this channel");
		}
	}
	
}
