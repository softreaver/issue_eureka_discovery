# issue_eureka_discovery
A minimal repository for an issue I have using eureka register method

**NOTE : This project is using Maven to pull dependencies.**

# Steps to reproduce 
 - Launch eureka-service-discovery wich is a simple Eureka discovery server
 - Launch eureka-service-register wich is an Eureka client and manages the manual registration of services.
 - Open new tab on your web browser and go to http://localhost:8761 to open Eureka server page
 - Open new tab on your web browser and go to http://localhost:9999 to open the welcome page of the service register
 - click 'go swaggy' and you will get the Swagger ui page where you will be able to test end points
 - Test the endpoint '/serviceRegister/register' you will get an **HTTP Error 500**.
 - You will see the error message **ERROR ===>> service discovery responded with HTTP error 400** in the console of *Eureka-service-register*
 
 
