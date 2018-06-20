package com.dmi.dao.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the service database table.
 * 
 */
@Entity
@NamedQuery(name="Service.findAll", query="SELECT s FROM Service s")
public class Service implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String name;

	//bi-directional many-to-one association to DeviceType
	@ManyToOne
	@JoinColumn(name="DEVICE_TYPE")
	private DeviceType deviceTypeBean;

	//bi-directional many-to-one association to ServiceSubscription
	@OneToMany(mappedBy="serviceBean", cascade=CascadeType.ALL)
	@JsonIgnore
	private List<ServiceSubscription> serviceSubscriptions;

	//bi-directional many-to-one association to SubService
	@OneToMany(mappedBy="serviceBean")
	@JsonIgnore
	private List<SubService> subServices;

	public Service() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DeviceType getDeviceTypeBean() {
		return this.deviceTypeBean;
	}

	public void setDeviceTypeBean(DeviceType deviceTypeBean) {
		this.deviceTypeBean = deviceTypeBean;
	}

	public List<ServiceSubscription> getServiceSubscriptions() {
		return this.serviceSubscriptions;
	}

	public void setServiceSubscriptions(List<ServiceSubscription> serviceSubscriptions) {
		this.serviceSubscriptions = serviceSubscriptions;
	}

	public ServiceSubscription addServiceSubscription(ServiceSubscription serviceSubscription) {
		getServiceSubscriptions().add(serviceSubscription);
		serviceSubscription.setServiceBean(this);

		return serviceSubscription;
	}

	public ServiceSubscription removeServiceSubscription(ServiceSubscription serviceSubscription) {
		getServiceSubscriptions().remove(serviceSubscription);
		serviceSubscription.setServiceBean(null);

		return serviceSubscription;
	}

	public List<SubService> getSubServices() {
		return this.subServices;
	}

	public void setSubServices(List<SubService> subServices) {
		this.subServices = subServices;
	}

	public SubService addSubService(SubService subService) {
		getSubServices().add(subService);
		subService.setServiceBean(this);

		return subService;
	}

	public SubService removeSubService(SubService subService) {
		getSubServices().remove(subService);
		subService.setServiceBean(null);

		return subService;
	}

}