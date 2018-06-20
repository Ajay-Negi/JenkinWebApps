package com.dmi.dao;

import com.dmi.dao.model.DeviceData;

@FunctionalInterface
public interface IDeviceDataDAO {

	DeviceData getByDeviceId(String deviceId);

}
