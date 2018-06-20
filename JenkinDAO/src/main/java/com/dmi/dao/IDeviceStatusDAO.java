package com.dmi.dao;

import java.util.List;

import com.dmi.dao.model.DeviceStatus;

public interface IDeviceStatusDAO
{
	List<DeviceStatus> getAll();
	
	DeviceStatus get(String alias);
	
	DeviceStatus get(Long deviceStatusId);

}
