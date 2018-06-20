package com.dmi.dao;

import com.dmi.dao.model.Sms;

public interface ISmsDAO {

	Sms addSmsInfo(Sms sms);
	
	void updateSmsInfo(Sms sms);
	
	void remove(Sms sms);

	Sms getSmsInfo(String mobileNumber);

}
