package com.dmi.dao;

import java.util.List;

import com.dmi.dao.model.DeviceType;
import com.dmi.dao.model.Service;
/**
 * 
 * @author ANegi
 *
 */

public interface IServiceDAO
{
	void save(Service service);
	void delete(Long serviceId);
	Service get(String serviceName);
	Service get(Long serviceId);
	Service getByServiceNameAndDeviceType(String serviceName, DeviceType deviceType, Long oemId);
	List<Service> get(DeviceType deviceType, Long oemId);
}
