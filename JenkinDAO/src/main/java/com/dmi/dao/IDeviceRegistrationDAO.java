package com.dmi.dao;

import java.util.List;

import com.dmi.dao.model.Device;
import com.dmi.dao.model.DeviceRegistration;
import com.dmi.dao.model.Oem;
import com.dmi.dao.model.User;

public interface IDeviceRegistrationDAO
{
	List<DeviceRegistration> getRegisteredDevices(Oem oem);

	DeviceRegistration getRegisteredDevice(User user, Device device);

	void registerDevice(DeviceRegistration deviceRegistration);

	void updateDevice(DeviceRegistration deviceRegistration);

	List<DeviceRegistration> getRegisteredDevicesByUser(User user);

	DeviceRegistration getDeviceRegistrationById(Long registrationId);

	DeviceRegistration getDeviceRegistrationByDevice(Device deviceBean);

	List<DeviceRegistration> getRegisteredDevicesForAllUsers();

	List<DeviceRegistration> getRegisteredDevicesByDeviceType(Long oemId, Long deviceTypeId);

}
