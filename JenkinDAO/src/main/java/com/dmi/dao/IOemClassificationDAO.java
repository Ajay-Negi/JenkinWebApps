package com.dmi.dao;

import java.util.List;

import com.dmi.dao.model.OemClassification;

public interface IOemClassificationDAO
{
	OemClassification get(Long oemClassificationId);
	List<OemClassification> getAll();
}
