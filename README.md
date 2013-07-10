# Open311 Java wrapper (Jreport) [![Build Status](https://travis-ci.org/codeforamerica/open311_java.png)](https://travis-ci.org/codeforamerica/open311_java)

This is a Java language binding (wrapper) to the Open311 GeoReport REST API. Under construction.

## Usage

### Build a wrapper
```java
// Simple wrapper
APIWrapper wrapper = new APIWrapperFactory(City.SAN_FRANCISCO).build();

// Test endpoint wrapper
wrapper = new APIWrapperFactory(City.SAN_FRANCISCO).setEndpointType(EndpointType.TEST).build();

// With the api key
wrapper = new APIWrapperFactory(City.SAN_FRANCISCO).setApiKey("your api key").build();

// All together?
wrapper = new APIWrapperFactory(City.SAN_FRANCISCO).setEndpointType(EndpointType.TEST).
  setApiKey("your api key").build();
```

Check all the possible parameters of the `APIWrapperFactory` in the [documentation](http://codeforamerica.github.io/open311_java/apidocs/index.html).


### Invoke operations
```java
// GET service list
List<Service> listOfServices = wrapper.getServiceList();

// GET service definition
ServiceDefinition definition = wrapper.getServiceDefinition("serviceCode");

// POST service request
POSTServiceRequestResponse response = wrapper.postServiceRequest(
  new POSTServiceRequestData("serviceCode", addressId, listOfattributes));

// GET service_request_id from a token
List<ServiceRequestIdResponse> serviceRequestIdresponse = wrapper.getServiceRequestIdFromToken("token");

// GET service requests
List<ServiceRequest> serviceRequests = wrapper.getServiceRequests(
  new GETServiceRequestsFilter().setStatus(Status.OPEN));

// GET service request 
ServiceRequest serviceRequest = wrapper.getServiceRequest("serviceRequestId");
```

**IMPORTANT**: It is worth it to check the [documentation](http://codeforamerica.github.io/open311_java/apidocs/index.html) and find all the possible parameters of the `GETServiceRequestFilter` and `POSTServiceRequestData` classes.
## Testing and building


In order to compile and test this project you should have [Maven](http://maven.apache.org/) installed in your system. You can find it in any repository you use (brew, apt...).

```bash
# Compile the project (and download dependencies)
mvn compile

# Execute tests
mvn test

# Execute tests with cobertura analysis
mvn cobertura:cobertura

# Generate the .jar without dependencies
mvn package

# Generate the .jar with dependencies
mvn assembly:single
```

### Testing and building outputs

 + `mvn cobertura:cobertura` will write its output in `target/site/cobertura/`, open the `index.html` file to check it.
 + `mvn package` will output the `target/jreport-{version}.jar` file.
 + `mvn assembly:single` will output the `target/jreport-{version}-jar-with-dependencies.jar` file.

## Caching
This library tries to save some responses for a certain time in order to avoid expensive network operations.
 + In a regular Java application, it is activated by default.
 + If you don't want to cache anything: `factory = new APIWrapperFactory().setCache(new NoCache());`
 + Using an Android app: `factory = new APIWrapperFactory().setCache(new AndroidCache(getApplicationContext()));`
 + Using a special platform which doesn't allow to create or write to files: Extend the [AbstractCache](https://github.com/codeforamerica/open311_java/blob/master/src/main/java/org/codeforamerica/open311/internals/caching/AbstractCache.java) class and `factory = new APIWrapperFactory().setCache(new YourCacheImplementation());`
