package com.softreaver.service.serviceRegister;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class serviceRegisterApplication {

    public static void main(String[] args) {
    	SpringApplication.run(serviceRegisterApplication.class, args);
    }

}
