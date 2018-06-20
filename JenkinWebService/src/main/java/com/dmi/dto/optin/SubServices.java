
package com.dmi.dto.optin;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
{ "name", "status", "notificationPreferences" })
public class SubServices
{

	@JsonProperty("id")
	private Long id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("status")
	private Boolean status;
	@JsonProperty("notificationPreferences")
	private NotificationPreferences notificationPreferences;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	@JsonProperty("name")
	public String getName()
	{
		return name;
	}

	@JsonProperty("name")
	public void setName(String name)
	{
		this.name = name;
	}

	@JsonProperty("status")
	public Boolean getStatus()
	{
		return status;
	}

	@JsonProperty("status")
	public void setStatus(Boolean status)
	{
		this.status = status;
	}

	@JsonProperty("notificationPreferences")
	public NotificationPreferences getNotificationPreferences()
	{
		return notificationPreferences;
	}

	@JsonProperty("notificationPreferences")
	public void setNotificationPreferences(NotificationPreferences notificationPreferences)
	{
		this.notificationPreferences = notificationPreferences;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties()
	{
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value)
	{
		this.additionalProperties.put(name, value);
	}

}
