package com.dmi.dao;

import java.util.List;

import com.dmi.dao.model.NotificationType;

public interface INotificationTypeDAO
{

	NotificationType get(Long notificationTypeId);
	NotificationType get(String alias);
	List<NotificationType> getAll();

}
