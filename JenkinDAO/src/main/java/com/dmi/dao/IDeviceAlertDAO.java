package com.dmi.dao;

import java.util.List;

import com.dmi.dao.model.Alert;

/**
 * 
 * @author ANegi
 *
 */
public interface IDeviceAlertDAO {

	void save(Alert deviceAlertBean);

	List<Alert> getByDeviceId(String deviceId);

	void delete(String deviceId);

	long getAlertsCount(String deviceId);

}
