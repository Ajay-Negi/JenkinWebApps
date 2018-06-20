package com.dmi.dao;

import com.dmi.dao.model.DeviceConfig;

public interface IDeviceConfigDAO {

	DeviceConfig getByVinId(String vinId);

}
