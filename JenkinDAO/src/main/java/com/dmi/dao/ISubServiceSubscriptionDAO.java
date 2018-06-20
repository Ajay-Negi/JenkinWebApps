package com.dmi.dao;

import java.util.List;

import com.dmi.dao.model.ServiceSubscription;
import com.dmi.dao.model.SubService;
import com.dmi.dao.model.SubServiceSubscription;

/**
 * 
 * @author ANegi
 *
 */
public interface ISubServiceSubscriptionDAO
{
	List<SubServiceSubscription> getSubscribedSubServices(ServiceSubscription serviceSubscrp);

	void save(SubServiceSubscription subServiceSubscription);

	void remove(ServiceSubscription serviceSubscription, SubService subService);

	SubServiceSubscription get(ServiceSubscription serviceSubscription, SubService subService);
}
