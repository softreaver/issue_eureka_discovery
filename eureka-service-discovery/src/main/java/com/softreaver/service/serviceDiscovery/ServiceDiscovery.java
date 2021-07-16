package com.softreaver.service.serviceDiscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class ServiceDiscovery extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(ServiceDiscovery.class, args);
	}
}
