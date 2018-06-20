package com.dmi.dto;

public class ConnectorEndpointDTO {

	long id;
	String jmsqname;
	String jmsurl;
	String restServiceName;
	String restServiceUrl;
	String notificationType;
	long userId;   

	public ConnectorEndpointDTO(int endpointId, String jmsqname, String jmsurl, String restServiceName, String restServiceUrl,
			String notificationType,long userId ) {
		super();
		this.id = endpointId;
		this.jmsqname = jmsqname;
		this.jmsurl = jmsurl;
		this.restServiceName = restServiceName;
		this.restServiceUrl = restServiceUrl;
		this.notificationType = notificationType;
		this.userId = userId;
	}

	public ConnectorEndpointDTO() {

	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getJmsqname() {
		return jmsqname;
	}

	public void setJmsqname(String jmsqname) {
		this.jmsqname = jmsqname;
	}

	public String getJmsurl() {
		return jmsurl;
	}

	public void setJmsurl(String jmsurl) {
		this.jmsurl = jmsurl;
	}
	
	public String getRestServiceName() {
		return restServiceName;
	}

	public void setRestServiceName(String restservicename) {
		this.restServiceName = restservicename;
	}
	

	public String getRestServiceUrl() {
		return restServiceUrl;
	}

	public void setRestServiceUrl(String restserviceUrl) {
		this.restServiceUrl = restserviceUrl;
	}
	
	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}
	

}
