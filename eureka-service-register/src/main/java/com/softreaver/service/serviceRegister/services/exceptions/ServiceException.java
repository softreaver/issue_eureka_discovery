package com.softreaver.service.serviceRegister.services.exceptions;

public class ServiceException extends Exception{

    private static final long serialVersionUID = 1L;

    public ServiceException() {
	super("An error occur when executing the service");
    }
    
    public ServiceException(String message) {
	super(message);
    }
}
