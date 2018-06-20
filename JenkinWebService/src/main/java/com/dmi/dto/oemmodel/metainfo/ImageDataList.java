/**
 * 
 */
package com.dmi.dto.oemmodel.metainfo;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author AChowdhury
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageDataList {
	
	@JsonProperty("key")
	private String key;
	
	@JsonProperty("value")
	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		ReflectionToStringBuilder builder = new ReflectionToStringBuilder(this);
		if (key != null)
			builder.reflectionToString(key);
		if (value != null)
			builder.reflectionToString(value);
		return builder.toString();
	}
	
	

}
