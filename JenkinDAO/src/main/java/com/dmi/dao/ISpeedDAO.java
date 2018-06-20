package com.dmi.dao;

import com.dmi.dao.model.Device;
import com.dmi.dao.model.Speed;

public interface ISpeedDAO
{

	Speed get(Device device);
	void save(Speed speed);
	void update(Speed speed);
	void delete(Device device);
}
