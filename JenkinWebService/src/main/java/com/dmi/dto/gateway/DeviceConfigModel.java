package com.dmi.dto.gateway;

public class DeviceConfigModel {


	private TelemetryData telemetryData;
	private VehicleHealthData vehicleHealthData;
	
	public TelemetryData getTelemetryData() {
		return telemetryData;
	}
	public void setTelemetryData(TelemetryData telemetryData) {
		this.telemetryData = telemetryData;
	}
	public VehicleHealthData getVehicleHealthData() {
		return vehicleHealthData;
	}
	public void setVehicleHealthData(VehicleHealthData vehicleHealthData) {
		this.vehicleHealthData = vehicleHealthData;
	}
}
