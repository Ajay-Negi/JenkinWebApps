package com.dmi.dao.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum NotificationTypeEnum {
	ALERT("Alert"), WELCOME("Welcome"), VHR("Vhr");

	private final String notificationTypeName;

	@JsonCreator
	private NotificationTypeEnum(String notificationTypeName) {
		this.notificationTypeName = notificationTypeName;
	}

	public String getNotificationTypeName() {
		return notificationTypeName;
	}

	public static List<NotificationTypeEnum> getAllNotificationTypeNames() {
		return new ArrayList<>(Arrays.asList(NotificationTypeEnum.values()));
	}

	@Override
	public String toString() {
		return this.getNotificationTypeName();
	}
	
	
}
