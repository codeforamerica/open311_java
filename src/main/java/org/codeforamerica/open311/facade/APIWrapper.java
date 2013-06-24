package org.codeforamerica.open311.facade;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.codeforamerica.open311.facade.data.Attribute;
import org.codeforamerica.open311.facade.data.PostServiceRequestResponse;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.data.ServiceRequest;
import org.codeforamerica.open311.facade.data.ServiceRequestIdResponse;
import org.codeforamerica.open311.facade.exceptions.APIWrapperException;
import org.codeforamerica.open311.facade.exceptions.APIWrapperException.Error;
import org.codeforamerica.open311.facade.exceptions.DataParsingException;
import org.codeforamerica.open311.facade.exceptions.GeoReportV2Error;
import org.codeforamerica.open311.internals.network.NetworkManager;
import org.codeforamerica.open311.internals.network.URLBuilder;
import org.codeforamerica.open311.internals.parsing.DataParser;

/**
 * Base class of wrapper of the API. This is the entry point to the system.
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

	/**
	 * Builds an API wrapper from its components. Note that this constructor
	 * can't be called outside the package. If you are a user the library please
	 * check the {@link APIWrapperFactory} class.
	 * 
	 * @param endpointUrl
	 *            Base url of the endpoint.
	 * @param type
	 *            Type of the server.
	 * @param dataParser
	 *            Instance of
	 * @param networkManager
	 */
	APIWrapper(String endpointUrl, Format format, EndpointType type,
			DataParser dataParser, NetworkManager networkManager,
			String jurisdictionId, String apiKey) {
		super();
		this.endpointUrl = endpointUrl;
		this.type = type;
		this.dataParser = dataParser;
		this.networkManager = networkManager;
		this.jurisdictionId = jurisdictionId;
		this.apiKey = apiKey;
		this.urlBuilder = new URLBuilder(endpointUrl, this.jurisdictionId,
				format.toString());
	}

	public static enum EndpointType {
		PRODUCTION, TEST;
	}

	public static enum Format {
		XML("xml"), JSON("json");

		private String description;

		Format(String description) {
			this.description = description;
		}

		public String toString() {
			return description;
		}

	}

	public String getWrapperInfo() {
		return endpointUrl + " - " + type.toString();
	}

	/**
	 * Updates the apiKey of the wrapper.
	 * 
	 * @param apiKey
	 */
	public void setAPIKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * Updates the jurisdictionId of the wrapper.
	 * 
	 * @param jurisdictionId
	 */
	public void setJurisdictionId(String jurisdictionId) {
		this.jurisdictionId = jurisdictionId;
	}

	/**
	 * Updates the format of the wrapper. A new {@link URLBuilder} will be
	 * instantiated.
	 * 
	 * @param format
	 *            New format.
	 */
	public void setFormat(Format format) {
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
		String rawServiceListData = "";
		try {
			URL serviceListUrl = urlBuilder.buildGetServiceListUrl();
			rawServiceListData = networkGet(serviceListUrl);
			return dataParser.parseServiceList(rawServiceListData);
		} catch (DataParsingException e) {
			tryToParseError(rawServiceListData);
			return null;
		} catch (MalformedURLException e) {
			throw new APIWrapperException(Error.URL_BUILDER, null);
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
			throw new APIWrapperException(Error.URL_BUILDER, null);
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
	public List<ServiceRequestIdResponse> getServiceRequestIdFromToken(
			String token) throws APIWrapperException {
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
			throw new APIWrapperException(Error.URL_BUILDER, null);
		}
	}

	/**
	 * GET Service Requests operation.
	 * 
	 * @return A list of service requests.
	 * @throws APIWrapperException
	 *             If there was any problem.
	 */
	public List<ServiceRequest> getServiceRequests(
			Map<String, String> optionalArguments) throws APIWrapperException {
		String rawServiceRequests = "";
		try {
			URL serviceRequestsUrl = urlBuilder
					.buildGetServiceRequests(optionalArguments);
			rawServiceRequests = networkGet(serviceRequestsUrl);
			return dataParser.parseServiceRequests(rawServiceRequests);
		} catch (DataParsingException e) {
			tryToParseError(rawServiceRequests);
			return null;
		} catch (MalformedURLException e) {
			throw new APIWrapperException(Error.URL_BUILDER, null);
		}
	}

	/**
	 * GET Service Request operation.
	 * 
	 * @param serviceRequestId
	 *            ID of the request to be fetched.
	 * @return A list of service request with the same ID.
	 * @throws APIWrapperException
	 *             If there was any problem.
	 */
	public List<ServiceRequest> getServiceRequest(String serviceRequestId)
			throws APIWrapperException {
		String rawServiceRequests = "";
		try {
			URL serviceRequestsUrl = urlBuilder
					.buildGetServiceRequest(serviceRequestId);
			rawServiceRequests = networkGet(serviceRequestsUrl);
			return dataParser.parseServiceRequests(rawServiceRequests);
		} catch (DataParsingException e) {
			tryToParseError(rawServiceRequests);
			return null;
		} catch (MalformedURLException e) {
			throw new APIWrapperException(Error.URL_BUILDER, null);
		}
	}

	/**
	 * Performs a POST Service Request operation.
	 * 
	 * @param serviceCode
	 *            Code of the service related to the request.
	 * @param latitude
	 *            Latitude coordinate.
	 * @param longitude
	 *            Longitude coordinate.
	 * @param optionalArguments
	 *            List of pairs (key, value)
	 * @param attributes
	 * @return A list of responses.
	 * @throws APIWrapperException
	 *             If there was any problem.
	 */
	public List<PostServiceRequestResponse> postServiceRequest(
			String serviceCode, float latitude, float longitude,
			Map<String, String> optionalArguments, List<Attribute> attributes)
			throws APIWrapperException {
		try {
			if (optionalArguments == null) {
				optionalArguments = new HashMap<String, String>();
			}
			if (attributes == null) {
				attributes = new LinkedList<Attribute>();
			}
			URL url = urlBuilder.buildPostServiceRequestUrl();
			optionalArguments.put("lat", String.valueOf(latitude));
			optionalArguments.put("long", String.valueOf(longitude));
			return postServiceRequestInternal(url, optionalArguments,
					attributes);
		} catch (MalformedURLException e) {
			throw new APIWrapperException(Error.URL_BUILDER, null);
		}
	}

	/**
	 * Performs a POST Service Request operation.
	 * 
	 * @param serviceCode
	 *            Code of the service related to the request.
	 * @param addressKey
	 *            Key of the given address parameter (address_id or
	 *            address_string)
	 * @param addressValue
	 *            Value of the given address.
	 * @param optionalArguments
	 *            List of pairs (key, value)
	 * @param attributes
	 * @return A list of responses.
	 * @throws APIWrapperException
	 *             If there was any problem.
	 */
	public List<PostServiceRequestResponse> postServiceRequest(
			String serviceCode, String addressKey, String addressValue,
			Map<String, String> optionalArguments, List<Attribute> attributes)
			throws APIWrapperException {
		try {
			if (optionalArguments == null) {
				optionalArguments = new HashMap<String, String>();
			}
			if (attributes == null) {
				attributes = new LinkedList<Attribute>();
			}
			URL url = urlBuilder.buildPostServiceRequestUrl();
			optionalArguments.put(addressKey, addressValue);
			return postServiceRequestInternal(url, optionalArguments,
					attributes);
		} catch (MalformedURLException e) {
			throw new APIWrapperException(Error.URL_BUILDER, null);
		}
	}

	/**
	 * Performs the POST service request with already built URL and body.
	 * 
	 * @param url
	 *            Target.
	 * @param body
	 *            Body of the request.
	 * @return List of Service Request responses.
	 * @throws APIWrapperException
	 *             If there was any problem.
	 * @throws MalformedURLException
	 *             If any attribute is not valid.
	 */
	private List<PostServiceRequestResponse> postServiceRequestInternal(
			URL url, Map<String, String> arguments, List<Attribute> attributes)
			throws APIWrapperException, MalformedURLException {
		if (apiKey.length() > 0) {
			arguments.put("api_key", apiKey);
		}
		if (jurisdictionId.length() > 0) {
			arguments.put("jurisdiction_id", jurisdictionId);
		}
		String body = urlBuilder.buildPostServiceRequestBody(arguments,
				buildAttributes(attributes));
		String rawPostServiceRequestResponse = networkPost(url, body);
		try {
			return dataParser
					.parsePostServiceRequestResponse(rawPostServiceRequestResponse);
		} catch (DataParsingException e) {
			tryToParseError(rawPostServiceRequestResponse);
			return null;
		}
	}

	/**
	 * Builds a map of (key, value) pairs from the attributes.
	 * 
	 * @param attributes
	 *            Request attributes.
	 * @return Map of (key, value) pairs.
	 */
	private Map<String, String> buildAttributes(List<Attribute> attributes) {
		Map<String, String> result = new HashMap<String, String>();
		for (Attribute attribute : attributes) {
			String attributeToString = attribute.toString();
			String[] parts = attributeToString.split("=");
			result.put(parts[0], parts[1]);
		}
		return result;
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
		try {
			List<GeoReportV2Error> errors = dataParser
					.parseGeoReportV2Errors(rawData);
			throw new APIWrapperException(Error.GEO_REPORT_V2, errors);
		} catch (DataParsingException ex) {
			throw new APIWrapperException(Error.DATA_PARSING, null);
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
		try {
			return networkManager.doGet(url);
		} catch (IOException e) {
			throw new APIWrapperException(Error.NETWORK_MANAGER, null);
		}
	}

	/**
	 * Tries to perform an HTTP POST operation and returns the result.
	 * 
	 * @param url
	 *            Target.
	 * @param body
	 *            Body of the request.
	 * @return Server response.
	 * @throws APIWrapperException
	 *             If there was any problem with the request.
	 */
	private String networkPost(URL url, String body) throws APIWrapperException {
		try {
			return networkManager.doPost(url, body);
		} catch (IOException e) {
			throw new APIWrapperException(Error.NETWORK_MANAGER, null);
		}
	}
}
