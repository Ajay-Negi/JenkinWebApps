/**
 * 
 */
package com.dmi.processor;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.dao.IMessageModelDAO;
import com.dmi.dao.model.DeviceType;
import com.dmi.dao.model.MessageModel;
import com.dmi.dto.MessageModelDTO;
import com.dmi.exception.ProcessorException;
import com.dmi.utils.DateTimeUtils;


/**
 * 
 * @author ANegi
 *
 */
@Service
public class MessageMasterProcessor
{

	@Autowired
	private IMessageModelDAO messageModelDAO;

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
			ex.printStackTrace();
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
	public void deleteMessageModelById(Long messageModelId)
	{

		MessageModel messageModel = messageModelDAO.getMessageModelById(messageModelId);

		messageModelDAO.deleteById(messageModel);

	}
}
