package com.dmi.processor;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.constant.Constants;
import com.dmi.dao.IDeviceTypeDAO;
import com.dmi.dao.IUserDAO;
import com.dmi.dao.IVhrHistoryDAO;
import com.dmi.dao.IVhrTemplateDAO;
import com.dmi.dao.model.DeviceType;
import com.dmi.dao.model.User;
import com.dmi.dao.model.VhrHistory;
import com.dmi.dao.model.VhrTemplate;
import com.dmi.dto.NotificationTemplateDTO;
import com.dmi.exception.ProcessorException;
import com.dmi.utils.EmailSender;
import com.dmi.utils.ReplaceUtil;

/**
 * 
 * @author Ajay Negi
 */

@Service
public class VhrProcessor {

	private static final Logger LOG = Logger.getLogger(VhrProcessor.class);
	
	@Autowired
	IVhrTemplateDAO vhrTemplateDAO; 
	
	@Autowired
	DeviceOnboardingProcessor deviceOnboardingProcessor;
	
	@Autowired
	IDeviceTypeDAO deviceTypeDAO;
	
	@Autowired
	IUserDAO userDAO;
	
	@Autowired
	IVhrHistoryDAO vhrHistoryDAO; 
	
	@Autowired
	EmailSender emailSender;
	
	@Transactional
	public void save(String userName, NotificationTemplateDTO notificationTemplateDTO, Long oemId) throws ProcessorException
	{
		try
		{
			// check if user is admin
			if (!userDAO.getUser(userName).getRoleBean().getAlias()
					.equalsIgnoreCase(Constants.USER_ROLE_CODE_ADMIN))
				throw new Exception("Only Admin can add new Notification Template.");
						
			DeviceType deviceTypeBean = deviceTypeDAO.get(notificationTemplateDTO.getDeviceTypeId(), oemId);
			
			//hardcoded replacement of string for the template content
			String content = ReplaceUtil.replaceString(notificationTemplateDTO.getContent());
			
			//check if template exist for devicetype
			VhrTemplate vhrTemplateBean  = vhrTemplateDAO.getByDeviceTypeId(notificationTemplateDTO.getDeviceTypeId());
			
			if(vhrTemplateBean != null)
				throw new ProcessorException("VHR template exist for Device Type");
			else
			{
				VhrTemplate vhrTemplate = new VhrTemplate();
				vhrTemplate.setContent(content.getBytes());
				vhrTemplate.setName(notificationTemplateDTO.getName());
				vhrTemplate.setDeviceTypeBean(deviceTypeBean);
			
				vhrTemplateDAO.save(vhrTemplate);
			}
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}


	}	
	
	@Transactional
	public void update(String userName, NotificationTemplateDTO notificationTemplateDTO)
			throws ProcessorException
	{
		try
		{
			// check if user is admin
			if (!userDAO.getUser(userName).getRoleBean().getAlias()
					.equalsIgnoreCase(Constants.USER_ROLE_CODE_ADMIN))
				throw new Exception("Only Admin can add new Notification Template.");
			
			VhrTemplate vhrTemplate = vhrTemplateDAO.getByDeviceTypeId(notificationTemplateDTO.getDeviceTypeId());
			
			vhrTemplate.setContent(notificationTemplateDTO.getContent().getBytes());
			vhrTemplate.setName(notificationTemplateDTO.getName());
		
			vhrTemplateDAO.update(vhrTemplate);
		}
		catch (NullPointerException npe)
		{
			LOG.error(npe.getMessage(), npe);
			throw new ProcessorException("Either username or vhr template Id is not valid");
		}catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}

	}
	
	public VhrTemplate getVhrTemplateByDeviceType(Long deviceTypeId) {

		VhrTemplate vhrTemplateBean = null;
		try {
			vhrTemplateBean = vhrTemplateDAO.getByDeviceTypeId(deviceTypeId);
		} catch (DataAccessException ex) {
			LOG.error(ex.getMessage(), ex);
		}
		return vhrTemplateBean;
	}
	
	public JSONArray getAllVhrTemplates(Long oemId) {
		
		//ObjectMapper objectMapper = new ObjectMapper();
		JSONArray jsonArray = new JSONArray();

		List<VhrTemplate> vhrTemplateList = new ArrayList<>();;
		try {
			vhrTemplateList = vhrTemplateDAO.getAllByOemId(oemId);
			
			for(VhrTemplate vhr: vhrTemplateList){
				
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("id", vhr.getId());
				jsonObj.put("name", vhr.getName());
				jsonObj.put("content", new String(vhr.getContent()));
				jsonObj.put("deviceTypeId", vhr.getDeviceTypeBean().getId());
				jsonArray.put(jsonObj);
			}
		} catch (DataAccessException ex) {
			LOG.error(ex.getMessage(), ex);
		}
		return jsonArray;
	}
	
	@Transactional
	public void deleteById(Long id) {

		try {
			vhrTemplateDAO.delete(id);
		} catch (DataAccessException ex) {
			LOG.error(ex.getMessage(), ex);
		}
	}
	
	
	public void getOnDemandVhr(String vin, Long userId, String month, String year){
	
		VhrHistory vhrHistory = null;
		
		try{
			vhrHistory = vhrHistoryDAO.getByVinMonthYear(vin, month, year);
			User userBean = userDAO.getUserById(userId);
			String primaryEmail = userBean.getNotificationContact().getEmailBean().getPrimaryEmail();
			
			String emailContent = new String(vhrHistory.getContent());
			
			emailSender.sendVHROnDemandMail(emailContent, primaryEmail, month , year);
			
			System.out.println("EMAIL CONTENT::" + emailContent);
			
		} catch(DataAccessException e){
			LOG.error(e.getMessage(), e);
		} catch (FileNotFoundException ex) {
			LOG.error(ex.getMessage(), ex);
		} catch (MessagingException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	
	
}
