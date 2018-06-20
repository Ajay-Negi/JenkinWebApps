package com.dmi.dao;

import java.util.List;

import com.dmi.dao.model.Uom;

public interface IUomDAO
{
	Uom get(Long uomId);
	
	Uom get(String uomName);

	List<Uom> getAll();
}
