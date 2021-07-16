package com.softreaver.service.serviceRegister.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import com.softreaver.service.serviceRegister.models.ServiceInfo;
import com.softreaver.service.serviceRegister.services.ServiceHandler;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Api(tags = { "/serviceRegister" }, description = "Offers interface for interracting with the service discovery server")
@CrossOrigin
@RestController
@RequestMapping("/serviceRegister")
public class serviceRegisterController {
    
    @Autowired
    ServiceHandler serviceHandler;
    
    @ApiOperation(value = "Register a service")
    @ApiResponses(value = { //
	    @ApiResponse(code = 201, message = "The service has been succesfully registered"), //
	    @ApiResponse(code = 500, message = "Unknown error, service could not be registered properly") //
    })
    @PostMapping(value = "/register")
    public ResponseEntity<String> registerService(@RequestBody ServiceInfo serviceInfo) {
	try {
	    this.serviceHandler.register(serviceInfo);
	} catch (Exception e) {
	    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);    
	}
	return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    @ApiOperation(value = "Unregister all instance of the given appName")
    @ApiResponses(value = { //
	    @ApiResponse(code = 200, message = "The given service has been successfully unregistered"), //
	    @ApiResponse(code = 500, message = "Error") //
    })
    @DeleteMapping(value = "/unregisterAll/{appName}")
    public ResponseEntity<String> unregisterService(
	    @ApiParam(name = "appName", type = "String", value = "The name of the service", example = "myService", required = true) @PathVariable("appName") String appName) {
	try {
	    this.serviceHandler.unregisterService(appName);
	} catch (Exception e) {
	    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @ApiOperation(value = "Unregister given instance")
    @ApiResponses(value = { //
	    @ApiResponse(code = 200, message = "The given service has been successfully unregistered"), //
	    @ApiResponse(code = 500, message = "Error") //
    })
    @DeleteMapping(value = "/unregister")
    public ResponseEntity<String> unregisterServiceInstance(@RequestBody ServiceInfo serviceInfo) {
	try {
	    this.serviceHandler.unregisterServiceInstance(serviceInfo);
	} catch (Exception e) {
	    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @ApiOperation(value = "Emit an heart beat for the given application instance and define the given status. Status must be one of these : up, down, starting or unvailable. Otherwise it will be set to unknown.")
    @ApiResponses(value = { //
	    @ApiResponse(code = 404, message = "The given ID does not exist"), //
	    @ApiResponse(code = 501, message = "Service not implemented") //
    })
    @PutMapping(value = "/heartbeat/{status}")
    public ResponseEntity<String> heartBeat(@RequestBody ServiceInfo serviceInfo, @PathVariable("status") String status) {
	try {
	    InstanceStatus statusEnum;
	    
	    switch(status) {
	    case "up":
		statusEnum = InstanceStatus.UP;
		break;
	    case "down":
		statusEnum = InstanceStatus.DOWN;
		break;
	    case "starting":
		statusEnum = InstanceStatus.STARTING;
		break;
	    case "unvailable":
		statusEnum = InstanceStatus.OUT_OF_SERVICE;
		break;
	    default:
		statusEnum = InstanceStatus.UNKNOWN;
		break;
	    }
	    this.serviceHandler.heartBeat(serviceInfo, statusEnum);
	} catch (Exception e) {
	    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<>(HttpStatus.OK);
    }
}

