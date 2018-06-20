package com.dmi.dao;

import com.dmi.dao.model.Vin;

/**
 * 
 * @author ANegi
 *
 */
public interface IVinDAO {

	void save(Vin vinBean);

	Vin getByVinId(String vinId);

	void delete(String vinId);

	boolean checkVinAvailability(String vin);

}
