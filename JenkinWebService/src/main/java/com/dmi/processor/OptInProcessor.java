package com.dmi.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.constant.Constants;
import com.dmi.dao.IDeviceDAO;
import com.dmi.dao.IDeviceRegistrationDAO;
import com.dmi.dao.IDeviceTypeDAO;
import com.dmi.dao.INotificationChannelDAO;
import com.dmi.dao.INotificationSubscriptionDAO;
import com.dmi.dao.IRedisDAO;
import com.dmi.dao.IServiceDAO;
import com.dmi.dao.IServiceSubscriptionDAO;
import com.dmi.dao.ISubServiceDAO;
import com.dmi.dao.ISubServiceSubscriptionDAO;
import com.dmi.dao.IUserDAO;
import com.dmi.dao.model.Device;
import com.dmi.dao.model.DeviceRegistration;
import com.dmi.dao.model.DeviceType;
import com.dmi.dao.model.NotificationSubscription;
import com.dmi.dao.model.ServiceSubscription;
import com.dmi.dao.model.SubService;
import com.dmi.dao.model.SubServiceSubscription;
import com.dmi.dao.model.User;
import com.dmi.dto.optin.NotificationPreferences;
import com.dmi.dto.optin.OptInDTO;
import com.dmi.dto.optin.Services;
import com.dmi.dto.optin.SubServices;
import com.dmi.exception.ProcessorException;


/**
 * @author ANegi
 *
 */
@Service
public class OptInProcessor
{
	private static final Logger LOG = Logger.getLogger(OptInProcessor.class);
	
	@Autowired
	IUserDAO daasUserDAO;
	@Autowired
	IDeviceDAO deviceDAO;
	@Autowired
	IDeviceRegistrationDAO deviceRegistrationDAO;
	@Autowired
	IDeviceTypeDAO deviceTypeDAO;
	@Autowired
	IServiceDAO serviceDAO;
	@Autowired
	IRedisDAO redisDAO;
	@Autowired
	ISubServiceDAO subServiceDAO;
	@Autowired
	INotificationChannelDAO notificationChannelDAO;
	@Autowired
	IServiceSubscriptionDAO serviceSubscriptionDAO;
	@Autowired
	ISubServiceSubscriptionDAO subServiceSubscriptionDAO;
	@Autowired
	INotificationSubscriptionDAO notificationSubscriptionDAO;

	public OptInDTO getOfferings(String deviceId, Long oemId) throws ProcessorException
	{
		OptInDTO optInDTO = null;
		try
		{
			Device device = deviceDAO.get(deviceId);
			if (device == null)
				throw new ProcessorException("Incorrect device Id. No such device found.");

			DeviceRegistration registration = deviceRegistrationDAO.getDeviceRegistrationByDevice(device);
			DeviceType deviceType = registration.getDeviceBean().getDeviceTypeBean();

			optInDTO = new OptInDTO();
			optInDTO.setOemId(registration.getDeviceBean().getOemBean().getId());
			optInDTO.setUserId(registration.getUserBean().getId());
			optInDTO.setDeviceTypeId(deviceType.getId());
			optInDTO.setDeviceId(deviceId);
			List<Services> servicesList = new ArrayList<>();
			optInDTO.setServicesList(servicesList);

			// GET All Available Services for a deviceType
			List<com.dmi.dao.model.Service> allServicesList = serviceDAO.get(deviceType, oemId);
			// GET SUBSCRIBED SERVICES
			List<ServiceSubscription> optedInServicesList = serviceSubscriptionDAO.getSubscribedServices(registration);

			servicesLoop: for (Iterator iterator1 = allServicesList.iterator(); iterator1.hasNext();)
			{
				com.dmi.dao.model.Service service = (com.dmi.dao.model.Service) iterator1.next();

				Services servicesObj = new Services();
				List<SubServices> subServicesList = new ArrayList<>();
				Boolean serviceStatus = false;

				// GET All Available Sub-Services for a Service
				List<SubService> allSubServicesList = subServiceDAO.get(service);
				// GET SUBSCRIBED SUB-SERVICES
				List<SubServiceSubscription> optedInSubServicesList = subServiceSubscriptionDAO
						.getSubscribedSubServices(serviceSubscriptionDAO.get(registration, service));

				if (isSubscribed(optedInServicesList, service))
					serviceStatus = true;
				else
					serviceStatus = false;

				subServicesLoop: for (Iterator iterator2 = allSubServicesList.iterator(); iterator2.hasNext();)
				{
					SubService subService = (SubService) iterator2.next();

					SubServices subServicesObj = new SubServices();
					NotificationPreferences notificationPreferences = new NotificationPreferences();
					Boolean subServiceStatus = false;

					if (isSubscribed(optedInSubServicesList, subService))
						subServiceStatus = true;
					else
						subServiceStatus = false;

					if (subServiceStatus)
					{
						// GET SUBSCRIBED NOTIFICATION PREFERENCES
						List<NotificationSubscription> notificationSubscriptionList = notificationSubscriptionDAO
								.getNotificationSubscriptions(
										findSubServiceSubscription(optedInSubServicesList, subService));
						
						for (NotificationSubscription notificationSubscription : notificationSubscriptionList)
						{
							switch (notificationSubscription.getNotificationChannelBean().getAlias())
							{
							case Constants.NOTIFICATION_CHANNEL_EMAIL:
								notificationPreferences.setEmail(true);
								break;
							case Constants.NOTIFICATION_CHANNEL_SMS:
								notificationPreferences.setSms(true);
								break;
							/*case Constants.NOTIFICATION_CHANNEL_PUSHNOTFN:
								notificationPreferences.setPushNotfn(true);
								break;
*/
							}
						}
					}

					subServicesObj.setId(subService.getId());
					subServicesObj.setName(subService.getName());
					subServicesObj.setStatus(subServiceStatus);
					subServicesObj.setNotificationPreferences(notificationPreferences);
					subServicesList.add(subServicesObj);
				}

				servicesObj.setId(service.getId());
				servicesObj.setName(service.getName());
				servicesObj.setStatus(serviceStatus);
				servicesObj.setSubServicesList(subServicesList);
				servicesList.add(servicesObj);
			}

		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
		return optInDTO;
	}

	public SubServiceSubscription findSubServiceSubscription(List<SubServiceSubscription> optedInSubServicesList,
			SubService subService)
	{
		for (SubServiceSubscription subServiceSubscription : optedInSubServicesList)
		{
			if (subServiceSubscription.getSubServiceBean().getId().longValue() == subService.getId().longValue())
				return subServiceSubscription;
		}
		return null;
	}

	public boolean isSubscribed(List<ServiceSubscription> subscribedServicesList,
			com.dmi.dao.model.Service service)
	{
		for (ServiceSubscription serviceSubscription : subscribedServicesList)
			if (serviceSubscription.getServiceBean().getId().longValue() == service.getId().longValue())
				return true;
		return false;
	}

	public boolean isSubscribed(List<SubServiceSubscription> subscribedSubServicesList, SubService subService)
	{
		for (SubServiceSubscription subServiceSubscription : subscribedSubServicesList)
			if (subServiceSubscription.getSubServiceBean().getId().longValue() == subService.getId().longValue())
				return true;
		return false;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Transactional(rollbackFor = Exception.class)
	public void saveServicePreferences(String username, Long oemId, OptInDTO optInDTO) throws ProcessorException
	{
		try
		{
			User user = daasUserDAO.getUser(username);

			Device device = deviceDAO.get(optInDTO.getDeviceId());
			
			if (device == null)
				throw new ProcessorException("Incorrect device Id. No such device found.");

			DeviceRegistration deviceRegistration = deviceRegistrationDAO.getRegisteredDevice(user, device);
			
			if(deviceRegistration == null)
				throw new ProcessorException("Error occured while finding device registration device and logged in user");

			// GET ALL SERVICE IN OPTINDTO
			List<Services> servicesList = optInDTO.getServicesList();
			// GET SUBSCRIBED SERVICES
			List<ServiceSubscription> optedInServicesList = serviceSubscriptionDAO.getSubscribedServices(deviceRegistration);
			
			//REDIS -- setting value for redis KEY
			String redisKey = oemId + "_" + device.getDeviceTypeBean().getId() + "_" + user.getId() + "_" + device.getId();
			List<String> serviceSubserviceList = new ArrayList<>();
			String redisValue = null;

			int count = 0;
			servicesLoop: for (Iterator iterator1 = servicesList.iterator(); iterator1.hasNext();)
			{
				Services servicesObj = (Services) iterator1.next();

				ServiceSubscription serviceSubscription = new ServiceSubscription();
				com.dmi.dao.model.Service service = serviceDAO.get(servicesObj.getId());
				serviceSubscription.setDeviceRegistrationBean(deviceRegistration);
				serviceSubscription.setServiceBean(service);

				if (servicesObj.getStatus())
				{
					if (isSubscribed(optedInServicesList, service))
						;
					else
						serviceSubscriptionDAO.save(serviceSubscription);

					serviceSubscription = serviceSubscriptionDAO.get(deviceRegistration, service);

					// GET ALL SUB-SERVICES IN OPTINDTO
					List<SubServices> subServicesList = optInDTO.getServicesList().get(count).getSubServicesList();
					// GET SUBSCRIBED SUB-SERVICES
					List<SubServiceSubscription> optedInSubServicesList = subServiceSubscriptionDAO
							.getSubscribedSubServices(serviceSubscription);
					
					subServicesLoop: for (Iterator iterator2 = subServicesList.iterator(); iterator2.hasNext();)
					{
						SubServices subServicesObj = (SubServices) iterator2.next();
						SubServiceSubscription subServiceSubscription = new SubServiceSubscription();
						SubService subService = subServiceDAO.get(subServicesObj.getId());
						subServiceSubscription.setSubServiceBean(subService);
						subServiceSubscription.setServiceSubscription(serviceSubscription);

						if (subServicesObj.getStatus())
						{
							
							//add service_subService value into list for redis entry
							serviceSubserviceList.add(service.getId()+"_"+subService.getId());
							
							SubServiceSubscription currentSubServiceSubscription = findSubServiceSubscription(
									optedInSubServicesList, subService);

							//if already subscribed remove notificationSubscription entry as 
							//it is later anyhow added freshly for every opted in subservice
							if (isSubscribed(optedInSubServicesList, subService))
								notificationSubscriptionDAO.remove(currentSubServiceSubscription);
							else
								subServiceSubscriptionDAO.save(subServiceSubscription);
								

							currentSubServiceSubscription = subServiceSubscriptionDAO.get(serviceSubscription, subService);

							// UPDATING NOTIFICATION SUBSCRIPTIONS
							// notification preferences for already opted in subservice was removed 
							//so that fresh preferences can be added here
							if (subServicesObj.getNotificationPreferences().getEmail())
							{
								NotificationSubscription notificationSubscription = new NotificationSubscription();
								notificationSubscription.setNotificationChannelBean(notificationChannelDAO.get(Constants.NOTIFICATION_CHANNEL_EMAIL));
								notificationSubscription.setSubServiceSubscriptionBean(currentSubServiceSubscription);
								notificationSubscriptionDAO.save(notificationSubscription);
							}
							if (subServicesObj.getNotificationPreferences().getSms())
							{
								NotificationSubscription notificationSubscription = new NotificationSubscription();
								notificationSubscription.setNotificationChannelBean(
										notificationChannelDAO.get(Constants.NOTIFICATION_CHANNEL_SMS));
								notificationSubscription.setSubServiceSubscriptionBean(currentSubServiceSubscription);
								notificationSubscriptionDAO.save(notificationSubscription);
							}
							/*if (subServicesObj.getNotificationPreferences().getPushNotfn())
							{
								NotificationSubscription notificationSubscription = new NotificationSubscription();
								notificationSubscription.setNotificationChannelBean(
										notificationChannelDAO.get(Constants.NOTIFICATION_CHANNEL_PUSHNOTFN));
								notificationSubscription.setSubServiceSubscriptionBean(currentSubServiceSubscription);
								notificationSubscriptionDAO.save(notificationSubscription);
							}*/
						}
						else
						{
							subServiceSubscriptionDAO.remove(serviceSubscription, subService);
						}
					}
				}
				else
				{
					serviceSubscriptionDAO.remove(deviceRegistration, service);
				}

				count++;
			}
			
			redisValue = StringUtils.join(serviceSubserviceList, '\t');
			
			Map<String, String> hmap = new HashMap<String, String>();
			hmap.put(redisKey, redisValue);
			redisDAO.save(Constants.REDIS_IOT_DMI_CUSTOMERSERVSUBSERV, hmap);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new ProcessorException(e.getMessage());

		}
	}

}
