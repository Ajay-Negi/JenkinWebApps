package com.dmi.dao.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the service_subscription database table.
 * 
 */
@Entity
@Table(name="service_subscription")
@NamedQuery(name="ServiceSubscription.findAll", query="SELECT s FROM ServiceSubscription s")
public class ServiceSubscription implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	//bi-directional many-to-one association to DeviceRegistration
	@ManyToOne
	@JoinColumn(name="DEVICE_REGISTRATION")
	private DeviceRegistration deviceRegistrationBean;

	//bi-directional many-to-one association to Service
	@ManyToOne
	@JoinColumn(name="SERVICE")
	private Service serviceBean;

	public ServiceSubscription() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DeviceRegistration getDeviceRegistrationBean() {
		return this.deviceRegistrationBean;
	}

	public void setDeviceRegistrationBean(DeviceRegistration deviceRegistrationBean) {
		this.deviceRegistrationBean = deviceRegistrationBean;
	}

	public Service getServiceBean() {
		return this.serviceBean;
	}

	public void setServiceBean(Service serviceBean) {
		this.serviceBean = serviceBean;
	}

}