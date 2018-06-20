package com.dmi.dao;

import java.util.List;

import com.dmi.dao.model.GeofenceAction;

public interface IGeofenceActionDAO
{

	GeofenceAction get(Long geofenceActionId);

	List<GeofenceAction> getAll();

}
