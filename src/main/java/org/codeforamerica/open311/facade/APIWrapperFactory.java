package org.codeforamerica.open311.facade;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.codeforamerica.open311.facade.data.Endpoint;
import org.codeforamerica.open311.facade.data.ServiceDiscoveryInfo;
import org.codeforamerica.open311.facade.exceptions.APIWrapperException;
import org.codeforamerica.open311.facade.exceptions.APIWrapperException.Error;
import org.codeforamerica.open311.facade.exceptions.DataParsingException;
import org.codeforamerica.open311.internals.caching.Cache;
import org.codeforamerica.open311.internals.logging.LogManager;
import org.codeforamerica.open311.internals.network.HTTPNetworkManager;
import org.codeforamerica.open311.internals.network.NetworkManager;
import org.codeforamerica.open311.internals.parsing.DataParser;
import org.codeforamerica.open311.internals.parsing.DataParserFactory;
import org.codeforamerica.open311.internals.platform.PlatformManager;

/**
 * Builds {@link APIWrapper} instances from different aspects specified by the
 * user. To set the optional parameters, use the setter methods and, then, call
 * {@link #build()}.
 * 
 * @author Santiago Munin <santimunin@gmail.com>
 * 
 */
public class APIWrapperFactory {
	;
	/**
	 * Used for the first way of building the {@link APIWrapper} (
	 * {@link #APIWrapperFactory(String, String, Format)}).
	 * 
	 */
	private String endpointUrl = null;
	/**
	 * Used for the first way of building the {@link APIWrapper} (
	 * {@link #APIWrapperFactory(String, String, Format)}).
	 * 
	 */
	private String jurisdictionId = null;
	/**
	 * Used for the second way of building the {@link APIWrapper} (
	 * {@link APIWrapperFactory#APIWrapperFactory(City, EndpointType)}).
	 * 
	 */
	private City city = null;
	/**
	 * Used for the second way of building the {@link APIWrapper} (
	 * {@link APIWrapperFactory#APIWrapperFactory(City, EndpointType)}).
	 */
	private EndpointType endpointType = null;
	/**
	 * Used for the second way of building the {@link APIWrapper} (
	 * {@link APIWrapperFactory#APIWrapperFactory(City, EndpointType)}).
	 */
	private Format format = null;
	private String apiKey = "";
	private NetworkManager networkManager = new HTTPNetworkManager(Format.XML);
	/**
	 * Suitable cache (depends on the execution environment).
	 */
	private Cache cache = PlatformManager.getInstance().buildCache();
	/**
	 * <code>true</code> if the built instance should be logged.
	 */
	private boolean log = false;
	/**
	 * Reference to the {@link LogManager}.
	 */
	private LogManager logManager = LogManager.getInstance();

	/**
	 * Builds an instance from the endpoint url.
	 * 
	 * @param endpointUrl
	 *            Url of the endpoint.
	 */
	public APIWrapperFactory(String endpointUrl) {
		this.endpointUrl = endpointUrl;
		this.jurisdictionId = "";
		this.format = Format.XML;
	}

	/**
	 * Builds an instance from the endpoint url and the jurisdiction_id.
	 * 
	 * @param endpointUrl
	 *            Url of the endpoint.
	 * @param jurisdictionId
	 *            Desired jurisdiction_id (can be <code>null</code>).
	 */
	public APIWrapperFactory(String endpointUrl, String jurisdictionId) {
		this(endpointUrl);
		this.jurisdictionId = jurisdictionId == null ? "" : jurisdictionId;
	}

	/**
	 * Builds an instance from the endpoint url and the data interchange format.
	 * 
	 * @param endpointUrl
	 *            Url of the endpoint.
	 * @param format
	 *            Data format. It is your responsibility to check if the given
	 *            format is allowed.
	 */
	public APIWrapperFactory(String endpointUrl, Format format) {
		this(endpointUrl);
		this.format = format;
	}

	/**
	 * Builds an instance from the endpoint url and the jurisdiction_id.
	 * 
	 * @param endpointUrl
	 *            Url of the endpoint.
	 * @param jurisdictionId
	 *            Desired jurisdiction_id (can be <code>null</code>).
	 * @param format
	 *            Data format. It is your responsibility to check if the given
	 *            format is allowed.
	 */
	public APIWrapperFactory(String endpointUrl, String jurisdictionId,
			Format format) {
		this(endpointUrl, jurisdictionId);
		this.format = format;
	}

	/**
	 * Builds an instance from the desired city.
	 * 
	 * The type of the endpoint will be {@link EndpointType#PRODUCTION}) and the
	 * format will be {@link Format#XML}.
	 * 
	 * @param city
	 *            Desired city.
	 */
	public APIWrapperFactory(City city) {
		this.city = city;
		this.endpointType = EndpointType.PRODUCTION;
		this.format = Format.XML;
	}

	/**
	 * Builds an instance from the desired city and endpoint type. The format
	 * will be {@link Format#XML}.
	 * 
	 * @param city
	 *            Desired city.
	 * @param endpointType
	 *            Desired endpoint type.
	 */
	public APIWrapperFactory(City city, EndpointType endpointType) {
		this(city);
		this.endpointType = endpointType;
	}

	/**
	 * Selects the desired data exchange format.
	 * 
	 * @param format
	 *            Desired format.
	 * @return The same instance with the new selected format.
	 */
	public APIWrapperFactory setFormat(Format format) {
		this.format = format;
		return this;
	}

	/**
	 * Sets the api key. <code>""</code> by default.
	 * 
	 * @param apiKey
	 *            Api key for the endpoint.
	 * @return The same instance with the new specified api key.
	 */
	public APIWrapperFactory setApiKey(String apiKey) {
		this.apiKey = apiKey;
		return this;
	}

	/**
	 * Sets a custom {@link NetworkManager}, useful if you need to use mocks or
	 * a platform-dependent network client which you can build implementing the
	 * {@link NetworkManager} interface.
	 * 
	 * @param networkManager
	 *            A implementation of the {@link NetworkManager} interface.
	 * @return The same instance with the new specified {@link NetworkManager}.
	 */
	public APIWrapperFactory setNetworkManager(NetworkManager networkManager) {
		this.networkManager = networkManager;
		this.networkManager.setFormat(Format.XML);
		return this;
	}

	/**
	 * Sets a desired cache system.
	 * 
	 * @param cache
	 *            Implementation of the {@link Cache} interface.
	 * @return The same instance with the new specified {@link Cache}.
	 */
	public APIWrapperFactory setCache(Cache cache) {
		if (cache != null) {
			this.cache = cache;
		}
		return this;
	}

	/**
	 * The built instance will be logged.
	 * 
	 * @return The same instance.
	 */
	public APIWrapperFactory withLogs() {
		this.log = true;
		LogManager.getInstance().activate(this);
		return this;
	}

	/**
	 * Builds an {@link APIWrapper}. <b>WARNING</b>: This operation could
	 * require some time to be done (it could involve network operations).
	 * 
	 * @return An instance built from the given parameters to this object.
	 * @throws APIWrapperException
	 *             If there was any problem.
	 */
	public APIWrapper build() throws APIWrapperException {
		if (city != null) {
			return buildWrapperFromCity(city, endpointType, apiKey,
					networkManager);
		}
		if (endpointUrl != null) {
			return buildWrapperFromEndpointUrl(endpointUrl, jurisdictionId,
					format);
		}
		return null;
	}

	/**
	 * Builds an {@link APIWrapper} ignoring the {@link APIWrapperFactory#city}
	 * and {@link APIWrapperFactory#endpointType} parameters.
	 * 
	 * @param endpointUrl
	 *            Endpoint's url.
	 * @param jurisdictionId
	 *            Jurisdiction id of the city. <b>WARNING</b>: This operation
	 *            does not check if the given format is allowed by the endpoint.
	 * @param format
	 *            Desired format to use.
	 * @return An instance built from the given parameters to this object.
	 */
	private APIWrapper buildWrapperFromEndpointUrl(String endpointUrl,
			String jurisdictionId, Format format) {
		logManager.logInfo(this,
				"Building a wrapper from the given endpoint url: "
						+ endpointUrl + ", jurisdiction_id: \""
						+ jurisdictionId + "\", format: " + format);
		return activateLoginIfRequested(new APIWrapper(endpointUrl, format,
				EndpointType.UNKNOWN, DataParserFactory.getInstance()
						.buildDataParser(format), networkManager, cache,
				jurisdictionId, apiKey));
	}

	/**
	 * Builds an {@link APIWrapper} ignoring the {@link #endpointUrl} and
	 * {@link #jurisdictionId} parameters. <b>NOTE</b>: This operation could
	 * require some time to be done (it involves network operations).
	 * 
	 * @param city
	 *            Desired city to work with.
	 * @param endpointType
	 *            Type of the desired endpoint (useful if you need, for example,
	 *            a dev one to test your application).
	 * @param apiKey
	 *            Api key which allows to perform some operations. If you need
	 *            one, go to the <a
	 *            href="http://wiki.open311.org/GeoReport_v2/Servers">list of
	 *            servers</a> and request it.
	 * @param networkManager
	 *            A {@link NetworkManager} implementation.
	 * @return An instance suited to the given parameters.
	 * 
	 * @throws APIWrapperException
	 *             If there was any problem.
	 */
	private APIWrapper buildWrapperFromCity(City city,
			EndpointType endpointType, String apiKey,
			NetworkManager networkManager) throws APIWrapperException {
		try {
			logManager.logInfo(this, "Getting the service discovery file.");
			DataParser dataParser = DataParserFactory.getInstance()
					.buildDataParser(Format.XML);
			ServiceDiscoveryInfo serviceDiscoveryInfo = cache
					.retrieveCachedServiceDiscoveryInfo(city);
			if (serviceDiscoveryInfo == null) {
				logManager
						.logInfo(this,
								"Service discovery file was not cached, downloading it.");
				serviceDiscoveryInfo = dataParser
						.parseServiceDiscovery(networkManager.doGet(new URL(
								city.getDiscoveryUrl())));
				cache.saveServiceDiscovery(city, serviceDiscoveryInfo);
			}
			Endpoint endpoint = serviceDiscoveryInfo
					.getMoreSuitableEndpoint(endpointType);
			if (endpoint == null) {
				logManager.logError(this, "No suitable endpoint was found.");
				throw new APIWrapperException(
						"No suitable endpoint was found.",
						Error.NOT_SUITABLE_ENDPOINT_FOUND, null);
			}
			logManager.logInfo(this, "Selected the more suitable endpoint.");
			Format format = selectFormat(this.format, endpoint);
			if (format != Format.XML) {
				logManager
						.logInfo(
								this,
								"The selected endpoint allows to use the "
										+ format
										+ " format which is a best option, selecting it.");
				networkManager.setFormat(format);
				dataParser = DataParserFactory.getInstance().buildDataParser(
						format);
			}

			return activateLoginIfRequested(new APIWrapper(endpoint.getUrl(),
					format, endpointType, dataParser, networkManager, cache,
					city.getJurisdictionId(), apiKey));
		} catch (MalformedURLException e) {
			logManager.logError(this, "Problem building the url");
			throw new APIWrapperException(e.getMessage(), Error.URL_BUILDER,
					null);
		} catch (DataParsingException e) {
			logManager.logError(this,
					"Problem parsing reveived data: " + e.getMessage());
			throw new APIWrapperException(e.getMessage(), Error.DATA_PARSING,
					null);
		} catch (IOException e) {
			logManager.logError(this,
					"Problem with the network request: " + e.getMessage());
			throw new APIWrapperException(e.getMessage(),
					Error.NETWORK_MANAGER, null);
		}
	}

	/**
	 * Selects the given {@link Format} if it is allowed by the {@link Endpoint}
	 * .
	 * 
	 * @param format
	 *            Desired format.
	 * @param endpoint
	 *            Selected endpoint.
	 * @return The given format if it is allowed by the endpoint, the more
	 *         suitable otherwise.
	 */
	private Format selectFormat(Format format, Endpoint endpoint) {
		if (format != null && endpoint.isCompatibleWithFormat(format)) {
			return format;
		} else {
			return endpoint.getBestFormat();
		}
	}

	/**
	 * Activates a wrapper in the {@link LogManager} if it was requested.
	 * 
	 * @param wrapperInstance
	 *            Instance of the wrapper which could be logged.
	 * @return the given instance.
	 */
	private APIWrapper activateLoginIfRequested(APIWrapper wrapperInstance) {
		if (log) {
			LogManager.getInstance().activate(wrapperInstance);
		}
		return wrapperInstance;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder("APIWrapperFactory");
		if (city != null) {
			builder.append(" - building from a City object");
		}
		if (endpointUrl != null) {
			builder.append(" - building from an endpoint url");
		}
		return builder.toString();
	}
}
