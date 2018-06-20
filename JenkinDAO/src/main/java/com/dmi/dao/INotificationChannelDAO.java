package com.dmi.dao;

import java.util.List;

import com.dmi.dao.model.NotificationChannel;

public interface INotificationChannelDAO
{

	NotificationChannel get(Long notificationTypeId);
	NotificationChannel get(String alias);
	List<NotificationChannel> getAll();

}
