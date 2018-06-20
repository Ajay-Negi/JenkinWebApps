package com.dmi.dao;

import com.dmi.dao.model.NotificationContact;
import com.dmi.dao.model.User;

public interface INotificationDAO {

	NotificationContact get(User user);
	
	void updateNotificationInfo(NotificationContact notificationContact);

	void addNotificationInfo(NotificationContact notificationContact);

}
