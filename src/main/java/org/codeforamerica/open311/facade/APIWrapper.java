package org.codeforamerica.open311.facade;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
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
	private String jurisictionId;
	private EndpointType type;
	private DataParser dataParser;
	private NetworkManager networkManager;
	private URLBuilder urlBuilder;
	private Map<String, String> baseParameters;

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
		this.jurisictionId = jurisdictionId;
		this.apiKey = apiKey;
		this.baseParameters = getBaseParameters();
		this.urlBuilder = new URLBuilder(endpointUrl, format.toString());
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
		this.baseParameters = getBaseParameters();
	}

	/**
	 * Updates the jurisdictionId of the wrapper.
	 * 
	 * @param jurisdictionId
	 */
	public void setJurisdictionId(String jurisdictionId) {
		this.jurisictionId = jurisdictionId;
		this.baseParameters = getBaseParameters();
	}

	/**
	 * Updates the format of the wrapper. A new {@link URLBuilder} will be
	 * instantiated.
	 * 
	 * @param format
	 *            New format.
	 */
	public void setFormat(Format format) {
		urlBuilder = new URLBuilder(endpointUrl, format.toString());

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
			rawServiceListData = networkGet(serviceListUrl, baseParameters);
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
			rawServiceDefinitionData = networkGet(serviceDefinitionUrl,
					baseParameters);
			return dataParser.parseServiceDefinition(rawServiceDefinitionData);
		} catch (DataParsingException e) {
			tryToParseError(rawServiceDefinitionData);
			return null;
		} catch (MalformedURLException e) {
			throw new APIWrapperException(Error.URL_BUILDER, null);
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
	 *             <code>rawData</code> the exact
	 *             {@link APIWrapperException.Error}.
	 */
	private void tryToParseError(String rawData) throws APIWrapperException {
		try {
			// TODO check if we need more than one.
			GeoReportV2Error error = dataParser.parseGeoReportV2Errors(rawData)
					.get(0);
			throw new APIWrapperException(Error.GEO_REPORT_V2, error);
		} catch (DataParsingException ex) {
			throw new APIWrapperException(Error.DATA_PARSING, null);
		}
	}

	/**
	 * Tries to perform an HTTP GET operation and returns the result.
	 * 
	 * @param url
	 *            Target.
	 * @param parameters
	 *            GET parameters.
	 * @return Server response.
	 * @throws APIWrapperException
	 *             If there was any problem with the request.
	 */
	private String networkGet(URL url, Map<String, String> parameters)
			throws APIWrapperException {
		try {
			return networkManager.doGet(url, baseParameters);
		} catch (IOException e) {
			throw new APIWrapperException(Error.NETWORK_MANAGER, null);
		}
	}

	/**
	 * Builds a Map<String, String> and adds parameters such as jurisdiction_id
	 * and api_key if the wrapper have them.
	 * 
	 * @return A list of pairs <Key, Value> useful to perform operations through
	 *         the {@link NetworkManager}.
	 */
	private Map<String, String> getBaseParameters() {
		Map<String, String> baseParameters = new HashMap<String, String>();
		if (this.jurisictionId.length() > 0) {
			baseParameters.put("jurisdiction_id", jurisictionId);
		}
		if (this.apiKey.length() > 0) {
			baseParameters.put("api_key", apiKey);
		}
		return baseParameters;
	}
}
