package org.codeforamerica.open311.facade;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codeforamerica.open311.facade.data.Service;
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
	private String format;
	private String apiKey;
	private String jurisictionId;
	private Type type;
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
	APIWrapper(String endpointUrl, String format, Type type,
			DataParser dataParser, NetworkManager networkManager,
			String jurisdictionId, String apiKey) {
		super();
		this.endpointUrl = endpointUrl;
		this.format = format;
		this.type = type;
		this.dataParser = dataParser;
		this.networkManager = networkManager;
		this.jurisictionId = jurisdictionId;
		this.apiKey = apiKey;
		this.baseParameters = getBaseParameters();
		this.urlBuilder = new URLBuilder(endpointUrl, format);
	}

	public static enum Type {
		PRODUCTION, TEST;
	}

	public String getWrapperInfo() {
		return endpointUrl + " - " + type.toString();
	}

	public void setAPIKey(String apiKey) {
		this.apiKey = apiKey;
		this.baseParameters = getBaseParameters();
	}

	public void setJurisdictionId(String jurisdictionId) {
		this.jurisictionId = jurisdictionId;
		this.baseParameters = getBaseParameters();
	}

	/**
	 * Gets a list of services from the endpoint.
	 * 
	 * @return List of fetched services.
	 * @throws APIWrapperException
	 *             is there was any problem.
	 */
	public List<Service> getServiceList() throws APIWrapperException {
		try {
			URL serviceListUrl;
			String rawServiceListData;
			try {
				serviceListUrl = urlBuilder.buildGetServiceListUrl();
			} catch (MalformedURLException e) {
				throw new APIWrapperException(Error.URL_BUILDER, null);
			}
			try {
				rawServiceListData = networkManager.doGet(serviceListUrl,
						baseParameters);

			} catch (IOException e) {
				throw new APIWrapperException(Error.NETWORK_MANAGER, null);
			}
			try {
				return dataParser.parseServiceList(rawServiceListData);
			} catch (DataParsingException e) {
				GeoReportV2Error error = dataParser.parseGeoReportV2Errors(
						rawServiceListData).get(0);
				throw new APIWrapperException(Error.GEO_REPORT_V2, error);
			}
		} catch (Exception e) {
			throw new APIWrapperException(Error.DATA_PARSING, null);
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
