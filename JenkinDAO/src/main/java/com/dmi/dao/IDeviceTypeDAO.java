package com.dmi.dao;

import java.util.List;

import com.dmi.dao.model.DeviceType;
import com.dmi.dao.model.Oem;
import com.dmi.dao.model.User;


public interface IDeviceTypeDAO
{

	List<DeviceType> getAll(Oem oem);

	List<DeviceType> getAll(User user);

	DeviceType get(Long deviceTypeId, Long oemId);

	void delete(Long deviceTypeId);

	void save(DeviceType deviceType);

}
