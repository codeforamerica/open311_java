package org.codeforamerica.open311.facade;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.codeforamerica.open311.facade.data.Attribute;
import org.codeforamerica.open311.facade.data.POSTServiceRequestResponse;
import org.codeforamerica.open311.facade.data.RelationshipManager;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.data.ServiceRequest;
import org.codeforamerica.open311.facade.data.ServiceRequestIdResponse;
import org.codeforamerica.open311.facade.data.operations.GETServiceRequestsFilter;
import org.codeforamerica.open311.facade.data.operations.POSTServiceRequestData;
import org.codeforamerica.open311.facade.exceptions.APIWrapperException;
import org.codeforamerica.open311.facade.exceptions.APIWrapperException.Error;
import org.codeforamerica.open311.facade.exceptions.DataParsingException;
import org.codeforamerica.open311.facade.exceptions.GeoReportV2Error;
import org.codeforamerica.open311.facade.exceptions.InvalidValueError;
import org.codeforamerica.open311.internals.caching.Cache;
import org.codeforamerica.open311.internals.logging.LogManager;
import org.codeforamerica.open311.internals.network.NetworkManager;
import org.codeforamerica.open311.internals.network.URLBuilder;
import org.codeforamerica.open311.internals.parsing.DataParser;

/**
 * Base class of the API. This is the entry point to the system. You can build
 * objects of this class using the {@link APIWrapperFactory} class.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class APIWrapper {

	private String endpointUrl;
	private String apiKey;
	private String jurisdictionId;
	private EndpointType type;
	private DataParser dataParser;
	private NetworkManager networkManager;
	private URLBuilder urlBuilder;
	private Cache cache;
	/**
	 * Useful to log events.
	 */
	private LogManager logManager = LogManager.getInstance();

	/**
	 * Builds an API wrapper from its components. Note that this constructor
	 * can't be invoked from outside the package. If you are a user the library
	 * please check the {@link APIWrapperFactory} class.
	 * 
	 * @param endpointUrl
	 *            Base url of the endpoint.
	 * @param type
	 *            Type of the server.
	 * @param dataParser
	 *            Instance of
	 * @param networkManager
	 */
	/* package */APIWrapper(String endpointUrl, Format format,
			EndpointType type, DataParser dataParser,
			NetworkManager networkManager, Cache cache, String jurisdictionId,
			String apiKey) {
		super();
		this.endpointUrl = endpointUrl;
		this.type = type;
		this.dataParser = dataParser;
		this.networkManager = networkManager;
		this.cache = cache;
		this.jurisdictionId = jurisdictionId;
		this.apiKey = apiKey;
		this.urlBuilder = new URLBuilder(endpointUrl, this.jurisdictionId,
				format.toString());
	}

	public String getEndpointUrl() {
		return endpointUrl;
	}

	/**
	 * Returns a string with some info.
	 * 
	 * @return endpointUrl - type.
	 */
	public String getWrapperInfo() {
		return endpointUrl + " - " + type.toString();
	}

	/**
	 * Returns the cache used by the wrapper.
	 * 
	 * @return Cache used by the wrapper, useful to delete it.
	 */
	public Cache getCache() {
		return cache;
	}

	/**
	 * Updates the format of the wrapper. A new {@link URLBuilder} will be
	 * instantiated.
	 * 
	 * @param format
	 *            New format.
	 */
	public void setFormat(Format format) {
		logManager.logInfo(this,
				"Changing wrapper format to " + format.toString());
		urlBuilder = new URLBuilder(endpointUrl, jurisdictionId,
				format.toString());
	}

	/**
	 * Gets a list of services from the endpoint. <a
	 * href="http://wiki.open311.org/GeoReport_v2#GET_Service_List">More
	 * info</a>
	 * 
	 * @return List of fetched services.
	 * @throws APIWrapperException
	 *             If there was any problem (data parsing, I/O...).
	 */
	public List<Service> getServiceList() throws APIWrapperException {
		logManager.logInfo(this, "GET Service List");
		List<Service> result = cache
				.retrieveCachedServiceList(this.endpointUrl);
		if (result == null) {
			result = askEndpointForTheServiceList();
			cache.saveListOfServices(endpointUrl, result);
		}
		if (result != null) {
			RelationshipManager.getInstance().addServiceWrapperRelationship(
					result, this);
		}
		return result;
	}

	/**
	 * Makes a network operation to ask the endpoint for the service list.
	 * 
	 * @return The list of services of the endpoint.
	 * @throws APIWrapperException
	 *             If there was any problem (data parsing, I/O...).
	 */
	private List<Service> askEndpointForTheServiceList()
			throws APIWrapperException {
		logManager.logInfo(this,
				"GET Service List is not cached, asking endpoint.");
		String rawServiceListData = "";
		try {
			URL serviceListUrl = urlBuilder.buildGetServiceListUrl();
			rawServiceListData = networkGet(serviceListUrl);
			return dataParser.parseServiceList(rawServiceListData);
		} catch (DataParsingException e) {
			tryToParseError(rawServiceListData);
			return null;
		} catch (MalformedURLException e) {
			throw new APIWrapperException(e.getMessage(), Error.URL_BUILDER,
					null);
		}
	}

	/**
	 * Gets the service definition of a concrete service. <a
	 * href="http://wiki.open311.org/GeoReport_v2#GET_Service_Definition">More
	 * info</a>
	 * 
	 * @param serviceCode
	 *            Code of the service of interest.
	 * @return All the information related to the given code.
	 * @throws APIWrapperException
	 *             If there was any problem (data parsing, I/O...).
	 */
	public ServiceDefinition getServiceDefinition(String serviceCode)
			throws APIWrapperException {
		logManager.logInfo(this, "GET Service Definition (service_code: "
				+ serviceCode + ")");
		ServiceDefinition result = cache.retrieveCachedServiceDefinition(
				endpointUrl, serviceCode);
		if (result == null) {
			result = askEndpointForAServiceDefinition(serviceCode);
			cache.saveServiceDefinition(endpointUrl, serviceCode, result);
		}
		return result;
	}

	/**
	 * Makes a network operation to ask the endpoint for the service definition.
	 * 
	 * @param serviceCode
	 *            Code of the service of interest.
	 * @return All the information related to the given code.
	 * @throws APIWrapperException
	 */
	private ServiceDefinition askEndpointForAServiceDefinition(
			String serviceCode) throws APIWrapperException {
		logManager.logInfo(this, "GET Service Definition (service_code: "
				+ serviceCode + ") is not cached, asking endpoint.");
		String rawServiceDefinitionData = "";
		try {
			URL serviceDefinitionUrl = urlBuilder
					.buildGetServiceDefinitionUrl(serviceCode);
			rawServiceDefinitionData = networkGet(serviceDefinitionUrl);
			return dataParser.parseServiceDefinition(rawServiceDefinitionData);
		} catch (DataParsingException e) {
			tryToParseError(rawServiceDefinitionData);
			return null;
		} catch (MalformedURLException e) {
			throw new APIWrapperException(e.getMessage(), Error.URL_BUILDER,
					null);
		}
	}

	/**
	 * This function is useful when the POST Service Request returns a token.
	 * 
	 * @param token
	 *            Given token.
	 * @return A service id useful to the operation GET Service Request.
	 * @throws APIWrapperException
	 *             If there was any problem.
	 */
	public ServiceRequestIdResponse getServiceRequestIdFromToken(String token)
			throws APIWrapperException {
		logManager.logInfo(this, "GET Service Request Id from token (token: "
				+ token + "), asking endpoint.");
		String rawServiceRequestId = "";
		try {
			URL serviceDefinitionUrl = urlBuilder
					.buildGetServiceRequestIdFromATokenUrl(token);
			rawServiceRequestId = networkGet(serviceDefinitionUrl);
			return dataParser
					.parseServiceRequestIdFromAToken(rawServiceRequestId);
		} catch (DataParsingException e) {
			tryToParseError(rawServiceRequestId);
			return null;
		} catch (MalformedURLException e) {
			throw new APIWrapperException(e.getMessage(), Error.URL_BUILDER,
					null);
		}
	}

	/**
	 * Retrieves all the service requests which accord to the given data.
	 * 
	 * @param operationData
	 *            An object with all the desired optional filtering parameters
	 *            to send.
	 * @return A list of service requests.
	 * @throws APIWrapperException
	 *             If there was any problem.
	 */
	public List<ServiceRequest> getServiceRequests(
			GETServiceRequestsFilter operationData) throws APIWrapperException {
		logManager.logInfo(this, "GET Service Requests");
		operationData = operationData == null ? new GETServiceRequestsFilter()
				: operationData;
		List<ServiceRequest> result = cache.retrieveCachedServiceRequests(
				endpointUrl, operationData);
		if (result == null) {
			result = askEndpointForServiceRequests(operationData);
			cache.saveServiceRequestList(endpointUrl, operationData, result);
		}
		return result;
	}

	/**
	 * Makes a network operation to ask the endpoint for service requests.
	 * 
	 * @param operationData
	 *            Filter to apply to the search in the endpoint.
	 * @return A list of service requests.
	 * @throws APIWrapperException
	 *             If there was any problem.
	 */
	private List<ServiceRequest> askEndpointForServiceRequests(
			GETServiceRequestsFilter operationData) throws APIWrapperException {
		logManager
				.logInfo(this,
						"GET Service Requests with the given filter is not cached, asking endpoint.");
		String rawServiceRequests = "";
		try {
			URL serviceRequestsUrl = operationData != null ? urlBuilder
					.buildGetServiceRequests(operationData
							.getOptionalParametersMap()) : urlBuilder
					.buildGetServiceRequests(null);
			rawServiceRequests = networkGet(serviceRequestsUrl);
			return dataParser.parseServiceRequests(rawServiceRequests);
		} catch (DataParsingException e) {
			tryToParseError(rawServiceRequests);
			return null;
		} catch (MalformedURLException e) {
			throw new APIWrapperException(e.getMessage(), Error.URL_BUILDER,
					null);
		}
	}

	/**
	 * GET Service Request operation.
	 * 
	 * @param serviceRequestId
	 *            ID of the request to be fetched.
	 * @return The info related to the given ID.
	 * @throws APIWrapperException
	 *             If there was any problem.
	 */
	public ServiceRequest getServiceRequest(String serviceRequestId)
			throws APIWrapperException {
		logManager.logInfo(this, "GET Service Request (service_request_id: "
				+ serviceRequestId + ")");
		ServiceRequest result = cache.retrieveCachedServiceRequest(endpointUrl,
				serviceRequestId);
		if (result == null) {
			result = askEndpointForAServiceRequest(serviceRequestId);
			cache.saveSingleServiceRequest(endpointUrl, serviceRequestId,
					result);
		}
		return result;
	}

	/**
	 * Makes a network operation to ask the endpoint for a service request.
	 * 
	 * @param serviceRequestId
	 *            Id of the request.
	 * @return The info related to the given ID.
	 * @throws APIWrapperException
	 *             If there was any problem.
	 */
	private ServiceRequest askEndpointForAServiceRequest(String serviceRequestId)
			throws APIWrapperException {
		logManager.logInfo(this, "GET Service Request (service_request_id: "
				+ serviceRequestId + ") is not cached, asking endpoint.");
		String rawServiceRequests = "";
		try {
			URL serviceRequestsUrl = urlBuilder
					.buildGetServiceRequest(serviceRequestId);
			rawServiceRequests = networkGet(serviceRequestsUrl);
			List<ServiceRequest> parsedServiceRequests = dataParser
					.parseServiceRequests(rawServiceRequests);
			return parsedServiceRequests.size() > 0 ? parsedServiceRequests
					.get(0) : null;
		} catch (DataParsingException e) {
			tryToParseError(rawServiceRequests);
			return null;
		} catch (MalformedURLException e) {
			throw new APIWrapperException(e.getMessage(), Error.URL_BUILDER,
					null);
		}
	}

	/**
	 * Performs a POST Service Request operation.
	 * 
	 * @param operationData
	 *            An object with all the desired parameters and attributes to be
	 *            sents.
	 * @return A list of responses
	 * @throws APIWrapperException
	 *             If there was any problem.
	 */
	public List<POSTServiceRequestResponse> postServiceRequest(
			POSTServiceRequestData operationData) throws APIWrapperException {
		logManager.logInfo(this, "POST Service Request");
		if (operationData == null) {
			throw new InvalidValueError("The given parameter is null");
		}
		Map<String, String> optionalArguments = operationData
				.getBodyRequestParameters() != null ? operationData
				.getBodyRequestParameters() : new HashMap<String, String>();
		List<Attribute> attributes = operationData.getAttributes() != null ? operationData
				.getAttributes() : new LinkedList<Attribute>();
		try {
			URL url = urlBuilder.buildPostServiceRequestUrl();
			return postServiceRequestInternal(url, optionalArguments,
					attributes);
		} catch (MalformedURLException e) {
			throw new APIWrapperException(e.getMessage(), Error.URL_BUILDER,
					null);
		}
	}

	/**
	 * Performs the POST service request with already built URL and body.
	 * 
	 * @param url
	 *            Target.
	 * @param arguments
	 *            Pairs (key,value) of optional arguments.
	 * @param attributes
	 *            List of attributes (check the GeoReport v2 wiki).
	 * @return List of Service Request responses.
	 * @throws APIWrapperException
	 *             If there was any problem.
	 * @throws MalformedURLException
	 *             If any attribute is not valid.
	 */
	private List<POSTServiceRequestResponse> postServiceRequestInternal(
			URL url, Map<String, String> arguments, List<Attribute> attributes)
			throws APIWrapperException, MalformedURLException {
		if (apiKey.length() > 0) {
			arguments.put("api_key", apiKey);
		}
		if (jurisdictionId.length() > 0) {
			arguments.put("jurisdiction_id", jurisdictionId);
		}
		Map<String, String> postArguments = urlBuilder
				.buildPostServiceRequestBody(arguments, attributes);
		String rawPostServiceRequestResponse = networkPost(url, postArguments);
		try {
			return dataParser
					.parsePostServiceRequestResponse(rawPostServiceRequestResponse);
		} catch (DataParsingException e) {
			tryToParseError(rawPostServiceRequestResponse);
			return null;
		}
	}

	/**
	 * This function has to be used after a DataParsingException. It means that
	 * the response is not the expected, but it still could be an API response
	 * (error).
	 * 
	 * @param rawData
	 *            Obtained data.
	 * @throws APIWrapperException
	 *             Always throws it, it depends on the content of the given
	 *             <code>rawData</code> the exact {@link Error}.
	 */
	private void tryToParseError(String rawData) throws APIWrapperException {
		logManager.logError(this,
				"There was an error, trying checking if it was an API error");
		try {
			List<GeoReportV2Error> errors = dataParser
					.parseGeoReportV2Errors(rawData);
			throw new APIWrapperException("GeoReport_v2 error",
					Error.GEO_REPORT_V2, errors);
		} catch (DataParsingException ex) {
			logManager.logError(this,
					"The error couldn't be parsed (it is not an API error.)");
			throw new APIWrapperException(ex.getMessage(), Error.DATA_PARSING,
					null);
		}
	}

	/**
	 * Tries to perform an HTTP GET operation and returns the result.
	 * 
	 * @param url
	 *            Target.
	 * @return Server response.
	 * @throws APIWrapperException
	 *             If there was any problem with the request.
	 */
	private String networkGet(URL url) throws APIWrapperException {
		logManager.logInfo(this, "HTTP GET " + url.toString());
		try {
			String response = networkManager.doGet(url);
			String cutResponse = response.length() > 50 ? response.substring(0,
					50) + "..." : response;
			logManager.logInfo(this,
					"HTTP GET response (50 or less first characters)"
							+ cutResponse);
			return response;
		} catch (IOException e) {
			logManager.logError(this, "HTTP GET error: " + e.getMessage());
			throw new APIWrapperException(e.getMessage(),
					Error.NETWORK_MANAGER, null);
		}
	}

	/**
	 * Tries to perform an HTTP POST operation and returns the result.
	 * 
	 * @param url
	 *            Target.
	 * @param parameters
	 *            Parameters of the request.
	 * @return Server response.
	 * @throws APIWrapperException
	 *             If there was any problem with the request.
	 */
	private String networkPost(URL url, Map<String, String> parameters)
			throws APIWrapperException {
		logManager.logInfo(this, "HTTP POST " + url.toString());
		try {
			String response = networkManager.doPost(url, parameters);
			String cutResponse = response.length() > 50 ? response.substring(0,
					50) + "..." : response;
			logManager.logInfo(this,
					"HTTP POST response (50 or less first characters)"
							+ cutResponse);
			return response;
		} catch (IOException e) {
			logManager.logError(this, "HTTP POST error: " + e.getMessage());
			throw new APIWrapperException(e.getMessage(),
					Error.NETWORK_MANAGER, null);
		}
	}

	public String toString() {
		return this.getWrapperInfo();
	}
}
