package com.dmi.dao;

import com.dmi.dao.model.Device;
import com.dmi.dao.model.Geolocation;

public interface IGeolocationDAO
{

	Geolocation get(Device device);
	Geolocation update(Geolocation geolocation);
	void save(Geolocation geolocation);
	void delete(Device device);
}
