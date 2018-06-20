package com.dmi.dao;

import com.dmi.dao.model.Device;

public interface IDeviceDAO
{

	void saveDevice(Device device);

	Device get(String deviceId);

	boolean checkDeviceIdAvailability(String deviceId);

	boolean checkVinAvailability(String vin);

	void delete(String deviceId);

	Device getByVinId(String vindId);

}
