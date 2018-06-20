package com.dmi.dao;

import java.util.List;

import com.dmi.dao.model.NotificationSubscription;
import com.dmi.dao.model.SubServiceSubscription;

public interface INotificationSubscriptionDAO
{

	List<NotificationSubscription> getNotificationSubscriptions(SubServiceSubscription findSubServiceSubscription);

	void remove(SubServiceSubscription findSubServiceSubscription);
	
	void save(NotificationSubscription notificationSubscription);

}
