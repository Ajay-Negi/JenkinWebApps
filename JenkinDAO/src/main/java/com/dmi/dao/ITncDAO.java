package com.dmi.dao;

import java.util.List;

import com.dmi.dao.model.Oem;
import com.dmi.dao.model.Tnc;


public interface ITncDAO
{
	Tnc getTncFromId(Long id);

	Tnc getLatestTnc(Oem oem);

	void save(Tnc tnc);
	
	boolean checkTnc(Oem oem,Float tncVersion);

	List<Tnc> get(Oem oem);

	void update(Tnc tnc);

	void delete(Long tncId);

}
