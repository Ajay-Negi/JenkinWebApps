package com.dmi.dao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the vhr_history database table.
 * 
 */
@Embeddable
public class VhrHistoryPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="VIN_ID")
	private String vinId;

	@Column(name="NOTIFICATION_YEAR")
	private String notificationYear;

	@Column(name="NOTIFICATION_MONTH")
	private String notificationMonth;

	public VhrHistoryPK() {
	}
	public String getVinId() {
		return this.vinId;
	}
	public void setVinId(String vinId) {
		this.vinId = vinId;
	}
	public String getNotificationYear() {
		return this.notificationYear;
	}
	public void setNotificationYear(String notificationYear) {
		this.notificationYear = notificationYear;
	}
	public String getNotificationMonth() {
		return this.notificationMonth;
	}
	public void setNotificationMonth(String notificationMonth) {
		this.notificationMonth = notificationMonth;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof VhrHistoryPK)) {
			return false;
		}
		VhrHistoryPK castOther = (VhrHistoryPK)other;
		return 
			this.vinId.equals(castOther.vinId)
			&& this.notificationYear.equals(castOther.notificationYear)
			&& this.notificationMonth.equals(castOther.notificationMonth);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.vinId.hashCode();
		hash = hash * prime + this.notificationYear.hashCode();
		hash = hash * prime + this.notificationMonth.hashCode();
		
		return hash;
	}	
}
