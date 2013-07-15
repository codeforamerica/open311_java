# Open311 Java wrapper (Jreport) [![Build Status](https://travis-ci.org/codeforamerica/open311_java.png)](https://travis-ci.org/codeforamerica/open311_java)

This is a Java language binding (wrapper) to the Open311 GeoReport REST API. This library is in a early stage of its development but it is already usable. If you find any problem or miss any feature, just open an [issue](https://github.com/codeforamerica/open311_java/issues?state=open).

If you are insterested about new features or the development process please check this [blog](http://santimunin.blogspot.com.es/).

## Usage

### Build a wrapper
**IMPORTANT**: You could have problems with some endpoints of cities because of their SSL certificates. Check the [solution](README.md#ssl-certificates).

You will need the [APIWrapperFactory](http://codeforamerica.github.io/open311_java/apidocs/org/codeforamerica/open311/facade/APIWrapperFactory.html) in order to build the [APIWrapper](http://codeforamerica.github.io/open311_java/apidocs/org/codeforamerica/open311/facade/APIWrapper.html) (which will allow you to query the endpoint). There are two different ways of creating the factory:
 + Specifiying the desired **city** and **type** of the endpoint (production, test...). The wrapper will get the most suitable endpoint and data interchange format for you.
 + Specifiying the **url of endpoint**, the **jurisdiction_id** parameter and the **data interchange format**. It is your responsibility to check if the format you desire is supported.

```java
// From the city (EndpointType.PRODUCTION is selected by default)
APIWrapper wrapper = new APIWrapperFactory(City.SAN_FRANCISCO).build();

// From the city, test endpoint.
APIWrapper wrapper = new APIWrapperFactory(City.SAN_FRANCISCO, EndpointType.TEST).build();

// From the url of the endpoint
APIWrapper wrapper = new APIWrapperFactory("https://www.myendpointurl.com/").build();

// From the url of the endpoint, the jurisdiction_id, and the data interchange format
APIWrapper wrapper = new APIWrapperFactory("https://www.myendpointurl.com/",
  "myjurisdictionId", Format.JSON).build();

// With the api key
wrapper = new APIWrapperFactory(City.SAN_FRANCISCO).setApiKey("your api key").build();

// Without caching data and the api key
wrapper = new APIWrapperFactory(City.SAN_FRANCISCO).setCache(new NoCache()).
  setApiKey("your api key").build();
```

Check all the possible parameters and constructors of the `APIWrapperFactory` in its [documentation page](http://codeforamerica.github.io/open311_java/apidocs/org/codeforamerica/open311/facade/APIWrapperFactory.html).


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
List<ServiceRequestIdResponse> serviceRequestIdresponse =
  wrapper.getServiceRequestIdFromToken("token");

// GET service requests
List<ServiceRequest> serviceRequests = wrapper.getServiceRequests(
  new GETServiceRequestsFilter().setStatus(Status.OPEN));

// GET service request 
ServiceRequest serviceRequest = wrapper.getServiceRequest("serviceRequestId");
```

It is worth it to check the [documentation](http://codeforamerica.github.io/open311_java/apidocs/index.html) and find all the possible parameters of the `GETServiceRequestFilter` and `POSTServiceRequestData` classes.
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

### Locations

 + `mvn cobertura:cobertura` will write its output in `target/site/cobertura/`, open the `index.html` file to check it.
 + `mvn package` will output the `target/jreport-{version}.jar` file.
 + `mvn assembly:single` will output the `target/jreport-{version}-jar-with-dependencies.jar` file.
 
## Useful information

 + [Javadocs](http://codeforamerica.github.io/open311_java/apidocs/index.html)
 + [Cobertura report (code coverage)](http://codeforamerica.github.io/open311_java/cobertura/index.html)
 + [Team](http://codeforamerica.github.io/open311_java/team-list.html)
 + [Dependencies](http://codeforamerica.github.io/open311_java/dependencies.html)
 + [Continuous Integration System](https://travis-ci.org/codeforamerica/open311_java)

## Caching
This library tries to save some responses for a certain time in order to avoid expensive network operations.
 + In a regular Java application, it is activated by default.
 + If you do not want to cache anything: `factory = new APIWrapperFactory().setCache(new NoCache());`
 + Using an Android app: `factory = new APIWrapperFactory().setCache(new AndroidCache(getApplicationContext()));`
 + Using a special platform which doesn't allow to create or write to files: Extend the [AbstractCache](http://codeforamerica.github.io/open311_java/apidocs/org/codeforamerica/open311/internals/caching/AbstractCache.html) class and `factory = new APIWrapperFactory().setCache(new YourCacheImplementation());`

## SSL certificates
Some of the endpoints could have SSL certificates which signature won't be recognize by Java. We are working to make them valid in Android, but there is already a solution if you are using just Java:

You can add the certificates to the Java's keystore (those certificates are in the `/certificates` folder of this repository). The keystore is probably in `$JAVA_HOME/lib/security/cacerts` or `$JAVA_HOME/jre/lib/security/cacerts`. The password should be *changeit* (try *changeme* if you are on a Mac and *changeit* doesn't work).

```bash
sudo sh add_certificates.sh <path/to/your/key/store>
``` 
