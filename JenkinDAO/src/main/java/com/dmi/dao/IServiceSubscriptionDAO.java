package com.dmi.dao;

import java.util.List;

import com.dmi.dao.model.DeviceRegistration;
import com.dmi.dao.model.Service;
import com.dmi.dao.model.ServiceSubscription;

/**
 * 
 * @author Ajay Negi
 */
public interface IServiceSubscriptionDAO
{
	List<ServiceSubscription> getSubscribedServices(DeviceRegistration registration);
	void save(ServiceSubscription serviceSubscription);
	ServiceSubscription get(DeviceRegistration registration,Service service);
	void remove(DeviceRegistration registration, Service service);
	void remove(DeviceRegistration deviceRegistration);
}
