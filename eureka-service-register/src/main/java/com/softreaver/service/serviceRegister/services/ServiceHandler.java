package com.softreaver.service.serviceRegister.services;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.cloud.netflix.eureka.http.RestTemplateTransportClientFactory;
import org.springframework.stereotype.Service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import com.netflix.appinfo.LeaseInfo;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.resolver.DefaultEndpoint;
import com.netflix.discovery.shared.resolver.EurekaEndpoint;
import com.netflix.discovery.shared.transport.EurekaHttpClient;
import com.netflix.discovery.shared.transport.EurekaHttpResponse;
import com.netflix.discovery.shared.transport.TransportClientFactory;
import com.softreaver.service.serviceRegister.models.ServiceInfo;
import com.softreaver.service.serviceRegister.services.exceptions.ServiceException;

@Service
public class ServiceHandler {
    final String serviceUrl = "http://localhost:8761/eureka/";
    private EurekaHttpClient eurekaHttpClient;

    @PostConstruct
    public void init() {
	TransportClientFactory factory = new RestTemplateTransportClientFactory();
	EurekaEndpoint defaultEndpoint = new DefaultEndpoint(serviceUrl);
	this.eurekaHttpClient = factory.newClient(defaultEndpoint);
    }

    public void register(ServiceInfo serviceInfo) throws ServiceException {	
	String instanceID = new StringBuilder()
		.append("service-register:")
		.append(serviceInfo.getAppName())
		.append(":")
		.append(serviceInfo.getPortNumber())
		.toString();
	
	String homeURL = new StringBuilder()
		.append("http://")
		//.append("${netflix.appinfo.hostname}")
		.append(serviceInfo.getIPAddress())
		.append(":")
		.append(serviceInfo.getPortNumber())
		.append("/")
		.toString();
	
	LeaseInfo leaseInfo = LeaseInfo.Builder.newBuilder()
		.setRenewalIntervalInSecs(10)
		.setDurationInSecs(15)
		.build();

	InstanceInfo info = InstanceInfo.Builder.newBuilder()
		.setAppGroupName(serviceInfo.getGoupName())
		.setAppName(serviceInfo.getAppName())
		.setIPAddr(serviceInfo.getIPAddress())
		.setPort(serviceInfo.getPortNumber())
		.setInstanceId(instanceID)
		.setHostName("localhost")
//		.setHomePageUrlForDeser(homeURL)
//		.setHealthCheckUrlsForDeser(homeURL + "actuator/health", homeURL + "actuator/health")
//		.setDataCenterInfo(null)
//		.setStatusPageUrlForDeser(homeURL + "actuator/info")
		.setLeaseInfo(leaseInfo)
		.build();

	EurekaHttpResponse<Void> httpResp = this.eurekaHttpClient.register(info);
	
	if (httpResp.getStatusCode() >= 400) {
	    System.err.println("ERROR ===>> service discovery responded with HTTP error " + httpResp.getStatusCode());
	    throw new ServiceException();
	}
    }

    public void unregisterService(String appName) {
	Application app = this.getEurekaApplication(appName);
	if (app != null) {
	    app.getInstances().forEach((InstanceInfo instance) -> {
		this.unregisterEurekaInstace(instance);
	    });
	}
    }

    public void unregisterServiceInstance(ServiceInfo serviceInfo) {
	Application app = this.getEurekaApplication(serviceInfo.getAppName());
	if (app != null) {
	    app.getInstances().forEach((InstanceInfo instance) -> {
		if (instance.getIPAddr().equals(serviceInfo.getIPAddress())
			&& instance.getPort() == serviceInfo.getPortNumber()
			&& instance.getAppGroupName().equals(serviceInfo.getGoupName())) {
		    this.unregisterEurekaInstace(instance);
		}
	    });
	}
    }

    public void heartBeat(ServiceInfo serviceInfo, InstanceStatus status) {
	InstanceInfo instance = this.getInstance(serviceInfo);
	this.eurekaHttpClient.sendHeartBeat(instance.getAppName(), instance.getId(), instance, status);
    }

    private Application getEurekaApplication(String appName) {
	EurekaHttpResponse<Application> httpResponse = this.eurekaHttpClient.getApplication(appName);
	System.out.println(httpResponse.getStatusCode());
	return httpResponse.getEntity();
    }

    private void unregisterEurekaInstace(InstanceInfo instance) {
	this.eurekaHttpClient.cancel(instance.getAppName(), instance.getId());
    }

    private InstanceInfo getInstance(ServiceInfo serviceInfo) {
	Application app = this.getEurekaApplication(serviceInfo.getAppName());
	List<InstanceInfo> instances = app.getInstances();
	for (int i = 0; i < instances.size(); i++) {
	    if (instances.get(i).getIPAddr().equals(serviceInfo.getIPAddress())
		    && instances.get(i).getPort() == serviceInfo.getPortNumber()) {
		return instances.get(i);
	    }
	}
	return null;
    }
}
