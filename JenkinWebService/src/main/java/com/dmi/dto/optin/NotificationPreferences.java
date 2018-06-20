
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
{ "Sms", "Email", "Push Notification", "Voice Call" })
public class NotificationPreferences
{

	@JsonProperty(value="SMS")
	private Boolean sms;
	@JsonProperty(value="Email")
	private Boolean email;
	/*@JsonProperty(value="Push Notification")
	private Boolean pushNotfn;*/
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public NotificationPreferences()
	{
		sms = false;
		email = false;
		//pushNotfn = false;
	}

	@JsonProperty("SMS")
	public Boolean getSms()
	{
		return sms;
	}

	@JsonProperty("SMS")
	public void setSms(Boolean sms)
	{
		this.sms = sms;
	}

	@JsonProperty("Email")
	public Boolean getEmail()
	{
		return email;
	}

	@JsonProperty("Email")
	public void setEmail(Boolean email)
	{
		this.email = email;
	}

	/*@JsonProperty("Push Notification")
	public Boolean getPushNotfn()
	{
		return pushNotfn;
	}

	@JsonProperty("Push Notification")
	public void setPushNotfn(Boolean pushNotfn)
	{
		this.pushNotfn = pushNotfn;
	}
*/
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
