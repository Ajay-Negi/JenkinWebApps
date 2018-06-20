package com.dmi.dao;

import java.util.List;

import com.dmi.dao.model.DeviceType;
import com.dmi.dao.model.Oem;

public interface IOemDAO {

	boolean checkOemAvailability(Long oemId);

	List<Oem> getAll();
	
	Oem get(Long oemId);

	void save(Oem oemBean);

	void delete(Long oemId);

	void update(Oem oemBean);

	List<Oem> getOemsForChatBot(long chatBotId);
	
	List<DeviceType> getDeviceTypesByOem(Long oemId);

	Oem getByName(String name);

	//void toggleLocationServices(String status, Long oemId);

}
