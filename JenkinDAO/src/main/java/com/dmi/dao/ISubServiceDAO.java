package com.dmi.dao;

import java.util.List;

import com.dmi.dao.model.Service;
import com.dmi.dao.model.SubService;


/**
 * 
 * @author ANegi
 *
 */
public interface ISubServiceDAO
{
	List<SubService> get(Service service);
	SubService get(String subServiceName);
	SubService get(Long subServiceId);
	SubService get(Long serviceId, String subServiceName);
	void save(SubService subService);
	void delete(Long subServiceId);
}
