/**
 * @author AChowdhury
 * Apr 27, 2016
 */
package com.dmi.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dmi.dao.model.DeviceType;
import com.dmi.dao.model.MessageModel;

/**
 * 
 * @author ANegi
 *
 */
@Component
public interface IMessageModelDAO
{
	public List<MessageModel> getByIds(List<DeviceType> deviceTypes);
	public void deleteById(MessageModel messageModelBean);
	public Long getMessageCount(Long deviceTypeId);
	public void save(MessageModel model);
	public void update(MessageModel model);
	public List<MessageModel> getAllMessageModel(Long oemId);
	public MessageModel getMessageModelById(Long messageModelId);
	MessageModel getMessageModelByDeviceType(Long deviceTypeId);
}
