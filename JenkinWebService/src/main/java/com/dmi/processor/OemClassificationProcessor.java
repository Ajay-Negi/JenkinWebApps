package com.dmi.processor;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.dao.IOemClassificationDAO;
import com.dmi.dao.model.OemClassification;
import com.dmi.exception.ProcessorException;


/**
 * 
 * @author ANegi
 *
 */
@Service
public class OemClassificationProcessor
{
	@Autowired
	IOemClassificationDAO oemClassificationDAO;
	
	private static final Logger LOG = Logger.getLogger(OemClassificationProcessor.class);

	public JSONArray getAll() throws ProcessorException
	{
		JSONArray oemClassificationArray;
		try
		{
			oemClassificationArray=new JSONArray();
			List<OemClassification> oemClassificationList=oemClassificationDAO.getAll();
			for(OemClassification oemClassification:oemClassificationList)
			{
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("id",oemClassification.getId());
				jsonObject.put("name",oemClassification.getName());
				oemClassificationArray.put(jsonObject);
			}
		}
		catch(Exception e)
		{
		    LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
		return oemClassificationArray;
	}
}
