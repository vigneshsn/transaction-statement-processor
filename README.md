##Introduction
Transaction processor which takes csv and xml upto 5MB as input and returns duplicate and incorrect transactions.

##Project Dependencies
* OpenCsv
* lombok
* JAXB
* Swagger2

##How to build and run application
* Run maven command - mvn clean install (This command will compile -> unit test -> package jar)
* Execute the command ```java -jar target/statement-processor.jar``` to run application

##API Documentation and testing endpoints
Information regarding REST endpoints can be found in the below link. Application should be up and running to access the link
http://localhost:8080/swagger-ui.html

Click ```transaction-statement-process-controller``` to access the ```/process-transaction``` endpoint.
Files can be uploaded using ```Try it out``` option.


