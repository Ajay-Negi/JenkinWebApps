package com.dmi.processor;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.dao.IGeofenceActionDAO;
import com.dmi.dao.model.GeofenceAction;
import com.dmi.exception.ProcessorException;


/**
 * 
 * @author Ajay Negi
 */
@Service
public class GeofenceActionProcessor
{
	@Autowired
	IGeofenceActionDAO geofenceActionDAO;

	public JSONArray getGeofenceActions() throws ProcessorException
	{
		JSONArray jsonArray;
		try
		{
			jsonArray=new JSONArray();
			List<GeofenceAction> geofenceActions = geofenceActionDAO.getAll();
			for(GeofenceAction fenceArea:geofenceActions)
			{
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("id", fenceArea.getId());
				jsonObject.put("area", fenceArea.getAlias());
				jsonArray.put(jsonObject);
			}
			return jsonArray;
		}
		catch(Exception e)
		{
			throw new ProcessorException(e.getMessage());
		}
	}
}
