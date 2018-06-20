
package com.dmi.dto.optin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
{ "name", "status", "subServicesList" })
public class Services
{

	@JsonProperty("id")
	private Long id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("status")
	private Boolean status;
	@JsonProperty("subServicesList")
	private List<SubServices> subServicesList = null;
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

	@JsonProperty("subServicesList")
	public List<SubServices> getSubServicesList()
	{
		return subServicesList;
	}

	@JsonProperty("subServicesList")
	public void setSubServicesList(List<SubServices> subServicesList)
	{
		this.subServicesList = subServicesList;
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
