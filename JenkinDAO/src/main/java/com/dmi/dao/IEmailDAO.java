package com.dmi.dao;

import com.dmi.dao.model.Email;

public interface IEmailDAO {

	boolean checkEmailAvailability(String primaryEmail);

	void addEmailInfo(Email email);

	Email getEmailInfo(String primaryEmail);

	void updateEmailInfo(Email email);

}
