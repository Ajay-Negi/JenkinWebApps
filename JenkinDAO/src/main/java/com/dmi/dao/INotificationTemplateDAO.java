package com.dmi.dao;

import java.util.List;

import com.dmi.dao.enums.NotificationChannelEnum;
import com.dmi.dao.model.NotificationTemplate;
import com.dmi.dao.model.Oem;


public interface INotificationTemplateDAO 
{
	List<NotificationTemplate> getAll(Oem oem);
	NotificationTemplate get(Long notificationTemplateId);
	void save(NotificationTemplate notificationTemplate);
	void update(NotificationTemplate notificationTemplate);
	void delete(NotificationTemplate notificationTemplateId);
	NotificationTemplate getBySubserviceAndChannel(Long subserviceId, NotificationChannelEnum type);
	List<NotificationTemplate> getAll(Long oemId);
	NotificationTemplate getBySubserviceAndChannel(Long subServiceId, Long channelTypeId);
	List<NotificationTemplate> getByChannel(String channel, Long oemId);
}
