package com.dmi.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.constant.Constants;
import com.dmi.dao.IDeviceTypeDAO;
import com.dmi.dao.IMessageModelDAO;
import com.dmi.dao.INotificationTemplateDAO;
import com.dmi.dao.IOemDAO;
import com.dmi.dao.IRedisDAO;
import com.dmi.dao.IRuleDAO;
import com.dmi.dao.ISubServiceDAO;
import com.dmi.dao.model.DeviceType;
import com.dmi.dao.model.MessageModel;
import com.dmi.dao.model.MessageRule;
import com.dmi.dao.model.NotificationTemplate;
import com.dmi.dao.model.SubService;
import com.dmi.dto.DeviceTypeDTO;
import com.dmi.dto.KeyValuePairDTO;
import com.dmi.dto.MessageRuleDTO;
import com.dmi.dto.NotificationTemplateDTO;
import com.dmi.exception.ProcessorException;
import com.dmi.utils.RuleAdaptor;
import com.google.gson.Gson;



@Service
public class RuleProcessor {

	private static final Logger LOG = Logger.getLogger(RuleProcessor.class);
	
	@Autowired
	private IRuleDAO ruleDAO;

	@Autowired
	private ISubServiceDAO subServiceDAO;

	@Autowired
	private IMessageModelDAO messageModelDAO;

	@Autowired
	private IOemDAO oemDAO;
	
	@Autowired
	private INotificationTemplateDAO notificationTemplateDAO;

	@Autowired
	private IDeviceTypeDAO deviceTypeDAO;
	
	@Autowired
	private IRedisDAO redisDAO;

	public List<MessageRuleDTO> getAllAvailableRulesForOem(Long oemId) {
		List<MessageRuleDTO> ruleDTOList = null;
		List<MessageRule> ruleModelList = null;

		try {
			ruleModelList = ruleDAO.getAllAvailableRulesForOem(oemId);
		} catch (DataAccessException ex) {
			LOG.error(ex.getMessage(), ex);
		}

		ruleDTOList = new ArrayList<>();
		if (ruleModelList != null) {
			try {
				for (int i = 0; i < ruleModelList.size(); i++) {
					MessageRuleDTO messageRuleDTO = RuleAdaptor.createMessageRuleDTO(ruleModelList.get(i));
					ruleDTOList.add(messageRuleDTO);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}

		return ruleDTOList;
	}
	
	
	public List<MessageRuleDTO> getAllAvailableRulesForUser(String userName) {
		List<MessageRuleDTO> ruleDTOList = null;
		List<MessageRule> ruleModelList = null;

		try {
			ruleModelList = ruleDAO.getAllAvailableRulesForUser(userName);
		} catch (DataAccessException ex) {
			LOG.error(ex.getMessage(), ex);
		}

		ruleDTOList = new ArrayList<>();
		if (ruleModelList != null) {
			try {
				for (int i = 0; i < ruleModelList.size(); i++) {
					MessageRuleDTO messageRuleDTO = RuleAdaptor.createMessageRuleDTO(ruleModelList.get(i));
					ruleDTOList.add(messageRuleDTO);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}

		return ruleDTOList;
	}

	@Transactional
	public MessageRuleDTO getSelectedRule(Long ruleId) {
		MessageRuleDTO messageRuleDTO = new MessageRuleDTO();
		try {

			MessageRule messageRulePOJO = ruleDAO.getMessageRule(ruleId);
		
			messageRuleDTO = RuleAdaptor.createMessageRuleDTO(messageRulePOJO);
		} catch (DataAccessException ex) {
			LOG.error(ex.getMessage(), ex);
		}
		return messageRuleDTO;
	}

	@Transactional
	public void createRule(MessageRuleDTO messageRuleDTO) throws ProcessorException {

		
		try {
			if (ruleDAO.findByRuleName(messageRuleDTO.getRuleName()) != null)
				throw new ProcessorException("Rule name already exists....");

			if (ruleDAO.getAllAvailableRuleForSubService(messageRuleDTO.getSubServiceId()) != null)
				throw new ProcessorException("Rule already exists corresponding to selected subService....");

			MessageRule messageRuleBean = RuleAdaptor.createMessageRulePOJO(messageRuleDTO);
			
			MessageModel messageModelBean = messageModelDAO.getMessageModelById(messageRuleDTO.getMessageModelId());
			
			messageRuleBean.setMessageModel(messageModelBean);
			
			if (messageRuleDTO.getEmailTemplateId() != null) {
				NotificationTemplate emailTemplate = notificationTemplateDAO.get(messageRuleDTO.getEmailTemplateId());
				messageRuleBean.setNotificationEmailTemplate(emailTemplate);
			}
			if (messageRuleDTO.getSmsTemplateId() != null) {
				NotificationTemplate smsTemplate = notificationTemplateDAO.get(messageRuleDTO.getSmsTemplateId());
				messageRuleBean.setNotificationSmsTemplate(smsTemplate);
			}
			SubService subServiceBean = subServiceDAO.get(messageRuleDTO.getSubServiceId());

			messageRuleBean.setSubService(subServiceBean);
			
			//save rule in Mysql DB
			Long ruleId = ruleDAO.createRule(messageRuleBean).getId();
			
			//save in redis
			Map<String, String> hmap = new HashMap<String, String>();
			String key = messageModelBean.getDeviceTypeBean().getOemBean().getId() + "_"+ messageModelBean.getDeviceTypeBean().getId()
					+"_" + subServiceBean.getServiceBean().getId() + "_" + subServiceBean.getId();
			JSONObject json = new JSONObject(messageRuleDTO.getRuleText());
			json.put("serviceId", subServiceBean.getServiceBean().getId().toString());
			json.put("subServiceId", messageRuleDTO.getSubServiceId().toString());
			json.put("smsTempId", messageRuleDTO.getSmsTemplateId().toString());
			json.put("emailTempId", messageRuleDTO.getEmailTemplateId().toString());
			json.put("ruleId", ruleId.toString());
			hmap.put(key, json.toString());
			redisDAO.save(Constants.REDIS_IOT_DMI_RULES, hmap);

		} catch (DataAccessException e) {
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage(), e);
		}

	}

	@Transactional
	public void updateRule(MessageRuleDTO messageRuleDTO) {

		try {
			MessageRule messageRulePojo = RuleAdaptor.createMessageRulePOJO(messageRuleDTO);
			messageRulePojo.setId(messageRuleDTO.getId());
			messageRulePojo = ruleDAO.updateRule(messageRulePojo);
			
		} catch (DataAccessException ex) {
			LOG.error(ex.getMessage(), ex);
		}
	}

	/*public List<DeviceTypeDTO> getAllSubDomainList(Long domainId) {
		List<DeviceTypeDTO> subdomainList = new ArrayList<DeviceTypeDTO>();
		try {
			List<SubDomain> subDomainPojoList = domainMasterDAO.getSubDomainsByDomainId(domainId);

			for (SubDomain subDomainPojo : subDomainPojoList) {
				DeviceTypeDTO subDomainVO = new DeviceTypeDTO();
				subDomainVO.setDomainName(subDomainPojo.getDomainMaster().getDomainNm());
				subDomainVO.setSubDomainId(subDomainPojo.getSubDomainId().intValue());
				subDomainVO.setSubDomainname(subDomainPojo.getSubDomainNm());

				subdomainList.add(subDomainVO);
			}
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		}

		return subdomainList;
	}*/

	public List<KeyValuePairDTO> getAllMessageModel(Long oemId) {
		List<KeyValuePairDTO> keyValueList = new ArrayList<KeyValuePairDTO>();
		try {
			List<MessageModel> messageModelList = messageModelDAO.getAllMessageModel(oemId);

			for (MessageModel messageModel : messageModelList) {
				KeyValuePairDTO keyValuePairVO = new KeyValuePairDTO();
				keyValuePairVO.setKey(messageModel.getId());
				keyValuePairVO.setValue(messageModel.getDeviceTypeBean().getAlias() + "--" + messageModel.getModelName());

				keyValueList.add(keyValuePairVO);
			}
		} catch (DataAccessException ex) {
			LOG.error(ex.getMessage(), ex);
		}

		return keyValueList;
	}

	public KeyValuePairDTO getMessageDataElement(Long messageModelId) {
		KeyValuePairDTO keyValuePairDTO = new KeyValuePairDTO();

		MessageModel messageModel = null;
		try {
			messageModel = messageModelDAO.getMessageModelById(messageModelId);
			keyValuePairDTO.setKey(messageModel.getId());
			keyValuePairDTO.setValue(messageModel.getMessageFormat());
		} catch (DataAccessException ex) {
			LOG.error(ex.getMessage(), ex);
		}
		return keyValuePairDTO;
	}

	

	public List<NotificationTemplateDTO> getAllTemplates(Long oemId) {
		List<NotificationTemplateDTO> templateDTOList = new ArrayList<>();
		List<NotificationTemplate> templateList = null;
		try {
			templateList = notificationTemplateDAO.getAll(oemId);
		} catch (DataAccessException ex) {
			LOG.error(ex.getMessage(), ex);
		}

		for (NotificationTemplate template : templateList) {
			NotificationTemplateDTO dto = new NotificationTemplateDTO();
			dto.setId(template.getId());
			dto.setName(template.getName());
			dto.setChannel(template.getNotificationChannelBean().getId());
			dto.setType(template.getNotificationTypeBean().getId());

			templateDTOList.add(dto);
		}

		return templateDTOList;
	}


	@Transactional
	public void deleteRuleByRuleId(Long ruleId) {
		
		MessageRule messageRuleBean = ruleDAO.getMessageRule(ruleId);
		// Invoke DAO to delete rule by Id
		ruleDAO.deleteRuleById(messageRuleBean);
		
		//delete from redis
				
		String key = messageRuleBean.getMessageModel().getDeviceTypeBean().getOemBean().getId() + "_"
				+ messageRuleBean.getMessageModel().getDeviceTypeBean().getId() + "_" 
				+ messageRuleBean.getSubService().getServiceBean().getId() + "_" 
				+ messageRuleBean.getSubService().getId();
				
		
		redisDAO.delete(Constants.REDIS_IOT_DMI_RULES, key);

	}

	/**
	 * @author Anegi
	 * @param OemId
	 * @return List of Device Types and thier corressponding MessageModels
	 */
	public List<JSONObject> getDeviceTypeAndMessageModelList(Long oemId) {
		
		List<JSONObject> deviceTypeAndMessageModelList = new ArrayList<>();

		List<DeviceType> deviceTypeList = oemDAO.getDeviceTypesByOem(oemId);
		List<MessageModel> messageModelList = messageModelDAO.getAllMessageModel(oemId);
		for (DeviceType deviceType : deviceTypeList) {

			for (MessageModel messageModel : messageModelList) {
				if (deviceType.getId() == messageModel.getDeviceTypeBean().getId()) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("deviceTypeId", deviceType.getId());
					jsonObject.put("deviceTypeName", deviceType.getAlias());
					jsonObject.put("messageModelkey", messageModel.getId());
					jsonObject.put("messageModelValue", messageModel.getMessageFormat());
					deviceTypeAndMessageModelList.add(jsonObject);
				}
			}
		}
		return deviceTypeAndMessageModelList;
	}
	
	public DeviceTypeDTO getDeviceTypeByMessageModelId(Long messageModelId) {

		MessageModel messageModel = null;
		try {
			messageModel = messageModelDAO.getMessageModelById(messageModelId);
		} catch (DataAccessException ex) {
			LOG.error(ex.getMessage(), ex);
		}

		DeviceTypeDTO deviceTypeDTO = new DeviceTypeDTO();
		deviceTypeDTO.setId(messageModel.getDeviceTypeBean().getId());
		deviceTypeDTO.setDeviceTypeName(messageModel.getDeviceTypeBean().getAlias());

		return deviceTypeDTO;
	}



}
