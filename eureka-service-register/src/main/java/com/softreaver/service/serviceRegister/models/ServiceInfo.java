package com.softreaver.service.serviceRegister.models;

import com.netflix.appinfo.InstanceInfo;

public class ServiceInfo {
    private String IPAddress;
    private String goupName;
    private String appName;
    private Integer portNumber;
    
    public String getIPAddress() {
        return IPAddress;
    }
    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }
    public String getGoupName() {
        return goupName;
    }
    public void setGoupName(String goupName) {
        this.goupName = goupName;
    }
    public String getAppName() {
        return appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }
    public Integer getPortNumber() {
	return portNumber;
    }
    public void setPortNumber(Integer portNumber) {
	this.portNumber = portNumber;
    }
    
    public static ServiceInfo parseFromEurekaInstance(InstanceInfo instance) {
	ServiceInfo service = new ServiceInfo();
	service.setAppName(instance.getAppName());
	service.setGoupName(instance.getAppGroupName());
	service.setIPAddress(instance.getIPAddr());
	service.setPortNumber(instance.getPort());
	return service;
    }
}
