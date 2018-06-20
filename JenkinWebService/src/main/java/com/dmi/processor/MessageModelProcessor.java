package com.dmi.processor;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.constant.Constants;
import com.dmi.dao.IDeviceTypeDAO;
import com.dmi.dao.IMessageModelDAO;
import com.dmi.dao.IRedisDAO;
import com.dmi.dao.model.DeviceType;
import com.dmi.dao.model.MessageModel;
import com.dmi.dto.MessageModelDTO;
import com.dmi.exception.ProcessorException;
import com.dmi.utils.DateTimeUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MessageModelProcessor
{

	private static final Logger LOG = Logger.getLogger(MessageModelProcessor.class);
	
	@Autowired
	IMessageModelDAO messageModelDAO;
	
	@Autowired
	IDeviceTypeDAO deviceTypeDAO;
	
	@Autowired
	IRedisDAO redisDAO;
	
	@PersistenceContext 
	EntityManager entityManager;
	
	@Transactional
	public void saveMessageModel(MessageModelDTO dto, String userName, Long oemId)
			throws ProcessorException
	{

		DeviceType deviceType = deviceTypeDAO.get(dto.getDeviceTypeId(), oemId);
		
		if(deviceType==null)
			throw new ProcessorException("Device Type not found!");
		
		MessageModel messageModel = new MessageModel();
		
		messageModel.setModelName(dto.getModelName());
		messageModel.setMessageFormat(dto.getMessageFormat());
		messageModel.setCreatedTimestamp(new Date());
		messageModel.setUpdatedTimestamp(new Date());
		messageModel.setDeviceTypeBean(deviceType);
		messageModel.setCreatedById(userName);
		
		
		try
		{
			messageModelDAO.save(messageModel);
			
			//save the  deviceId key mapping with customerId_vin value
			Map<String, String> hmap = new HashMap<String, String>();
			hmap.put(oemId+"_"+deviceType.getId(), messageModel.getMessageFormat());
			redisDAO.save(Constants.REDIS_IOT_DMI_MESSAGEMODEL, hmap);
		}
		catch (DataAccessException ex)
		{
			LOG.error(ex.getMessage(), ex);
			throw new ProcessorException(ex);
		}
	}

	@Transactional
	public void updateMessageModel(MessageModel messageModel) throws ProcessorException
	{

		try
		{
			messageModelDAO.update(messageModel);
			
			//save the  deviceId key mapping with customerId_vin value
			Map<String, String> hmap = new HashMap<String, String>();
			hmap.put(messageModel.getDeviceTypeBean().getOemBean().getId() +"_"+ messageModel.getDeviceTypeBean().getId(), messageModel.getMessageFormat());
			redisDAO.save(Constants.REDIS_IOT_DMI_MESSAGEMODEL, hmap);
		}
		catch (DataAccessException ex)
		{
			LOG.error(ex.getMessage(), ex);
			throw new ProcessorException(ex);
		}
	}


	public MessageModel getByDeviceType(Long deviceTypeId) throws ProcessorException
	{

		MessageModel messageModel = null;
		try
		{
			messageModel = messageModelDAO.getMessageModelByDeviceType(deviceTypeId);
		}
		catch (DataAccessException e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e);
		}
		return messageModel;
	}
	
	public MessageModel getById(Long messageModelId) throws ProcessorException
	{

		MessageModel messageModel = null;
		try
		{
			messageModel = messageModelDAO.getMessageModelById(messageModelId);
		}
		catch (DataAccessException e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e);
		}
		return messageModel;
	}
	
	public JSONArray getAll(Long oemId) throws ProcessorException
	{

		List<MessageModel> messageModelList;
		JSONArray jsonArray = new JSONArray();
		messageModelList = messageModelDAO.getAllMessageModel(oemId);
		
		
		if(!messageModelList.isEmpty()){
			
			String jsonString = null;
			for(MessageModel messageModel : messageModelList)
			{
				
				ObjectMapper objectMapper = new ObjectMapper();
				
				try {
					jsonString = objectMapper.writeValueAsString(messageModel);
				} catch (JsonProcessingException ex) {

					LOG.error(ex.getMessage(),ex);
					
				}
				
				jsonArray.put( new JSONObject(jsonString));
				
			}
			
			
			return jsonArray;
		}
		else
		{
			throw new ProcessorException("No MessageModel exists");
		}
		
	}

	//NOT NEEDED
	/*public Map<Long, String> findSubDomainName(Long domainId)
			throws ProcessorException
	{

		List<Object[]> subDomainInfo = null;
		try
		{
			subDomainInfo = messageMasterDAO.getSubDomainName(domainId);
		}
		catch (DataAccessException ex)
		{
			ex.printStackTrace();
			throw new ProcessorException(ex);
		}

		Map<Long, String> result = null;
		if (subDomainInfo != null && !subDomainInfo.isEmpty())
		{
			result = new HashMap<Long, String>();
			for (Object[] ob : subDomainInfo)
			{
				result.put(Long.parseLong(ob[0].toString()), ob[1].toString());
			}
		}
		else
		{
			result = new HashMap<Long, String>();
			result.put(401l, "No data");
		}
		return result;
	}*/
	

	
	public List<MessageModelDTO> getMessageModelsForDeviceTypeList(
			List<DeviceType> deviceTypes) throws ProcessorException
	{

		List<MessageModel> messageModelList = null;
		try
		{
			messageModelList = messageModelDAO.getByIds(deviceTypes);
		}
		catch (DataAccessException ex)
		{
			LOG.error(ex.getMessage(), ex);
			throw new ProcessorException(ex);
		}
		
		List<MessageModelDTO> messageModelVOList = new LinkedList<>();

		for (MessageModel messageModel : messageModelList)
		{
			MessageModelDTO vo = new MessageModelDTO()
					.setId(messageModel.getId())
					.setMessageFormat(messageModel.getMessageFormat())
					.setUpdatedTimeStamp(DateTimeUtils.convertToCommonTimeFormat(messageModel.getUpdatedTimestamp()))
					.setCreatedTimeStamp(DateTimeUtils.convertToCommonTimeFormat(messageModel.getCreatedTimestamp()));;

					messageModelVOList.add(vo);
		}

		return messageModelVOList;
	}

	@Transactional
	public void deleteMessageModelById(Long messageModelId) throws ProcessorException
	{

		try{
			MessageModel messageModel = entityManager.find(MessageModel.class, messageModelId);
			messageModelDAO.deleteById(messageModel);
			
			//delete key value pair for oemId_deviceTypeId key in Redis
			String key = messageModel.getDeviceTypeBean().getOemBean().getId()+"_"+messageModel.getDeviceTypeBean().getId();
			redisDAO.delete(Constants.REDIS_IOT_DMI_MESSAGEMODEL, key );
		}
		catch(Exception ex){
			LOG.error(ex.getMessage(), ex);
			throw new ProcessorException("MessageModel is used in any of the rules");
		}

	}
}
