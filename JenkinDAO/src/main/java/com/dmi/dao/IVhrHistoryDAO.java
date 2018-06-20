package com.dmi.dao;

import com.dmi.dao.model.VhrHistory;

public interface IVhrHistoryDAO {

	VhrHistory getByVinMonthYear(String vin, String month, String year);

}
