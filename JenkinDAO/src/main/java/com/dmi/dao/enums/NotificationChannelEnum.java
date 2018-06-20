package com.dmi.dao.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum NotificationChannelEnum {
	EMAIL("Email"), SMS("SMS"), PUSH("Push Notification"), VOICE("Voice Call");

	private final String notificationChannelName;

	@JsonCreator
	private NotificationChannelEnum(String notificationChannelName) {
		this.notificationChannelName = notificationChannelName;
	}

	public String getChannelType() {
		return notificationChannelName;
	}

	public static List<NotificationChannelEnum> getAllTemplateTypes() {
		return new ArrayList<>(Arrays.asList(NotificationChannelEnum.values()));
	}

	@Override
	public String toString() {
		return notificationChannelName;
	}
	
	
}
