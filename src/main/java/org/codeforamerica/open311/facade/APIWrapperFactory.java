package org.codeforamerica.open311.facade;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.codeforamerica.open311.facade.data.Endpoint;
import org.codeforamerica.open311.facade.data.ServiceDiscoveryInfo;
import org.codeforamerica.open311.facade.exceptions.APIWrapperException;
import org.codeforamerica.open311.facade.exceptions.APIWrapperException.Error;
import org.codeforamerica.open311.facade.exceptions.DataParsingException;
import org.codeforamerica.open311.internals.network.HTTPNetworkManager;
import org.codeforamerica.open311.internals.network.NetworkManager;
import org.codeforamerica.open311.internals.parsing.DataParser;
import org.codeforamerica.open311.internals.parsing.DataParserFactory;

/**
 * Builds {@link APIWrapper} instances from different aspects of the system and
 * the chosen city. To set the optional parameters, use the setter methods and,
 * then, <code>build</code>.
 * 
 * @author Santiago Munin <santimunin@gmail.com>
 * 
 */
public class APIWrapperFactory {

	private City city;
	private EndpointType endpointType = EndpointType.PRODUCTION;
	private String apiKey = "";
	private NetworkManager networkManager = new HTTPNetworkManager(Format.XML);

	public APIWrapperFactory(City city) {
		this.city = city;
	}

	/**
	 * Sets the endpoint type. <code>PRODUCTION</code> type is chosen by
	 * default.
	 * 
	 * @param endpointType
	 *            Desired type of the endpoint.
	 * @return The same instance with the new specified endpoint type.
	 */
	public APIWrapperFactory setEndpointType(EndpointType endpointType) {
		this.endpointType = endpointType;
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
		return this;
	}

	/**
	 * Builds an {@link APIWrapper}. <b>NOTE</b>: This operation could require
	 * some time to be done (it involves network operations).
	 * 
	 * @return An instance built from the given parameters to this objects.
	 * @throws APIWrapperException
	 *             If there was any problem.
	 */
	public APIWrapper build() throws APIWrapperException {
		return buildWrapper(city, endpointType, apiKey, networkManager);
	}

	/**
	 * Builds an {@link APIWrapper}. <b>NOTE</b>: This operation could require
	 * some time to be done (it involves network operations).
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
	private APIWrapper buildWrapper(City city, EndpointType endpointType,
			String apiKey, NetworkManager networkManager)
			throws APIWrapperException {
		try {
			DataParser dataParser = DataParserFactory.getInstance()
					.buildDataParser(Format.XML);
			ServiceDiscoveryInfo serviceDiscoveryInfo = dataParser
					.parseServiceDiscovery(networkManager.doGet(new URL(city
							.getDiscoveryUrl())));
			Endpoint endpoint = serviceDiscoveryInfo
					.getMoreSuitableEndpoint(endpointType);
			Format format = endpoint.getBestFormat();
			if (format != Format.XML) {
				networkManager.setFormat(format);
				dataParser = DataParserFactory.getInstance().buildDataParser(
						format);
			}
			return new APIWrapper(endpoint.getUrl(), format, endpointType,
					dataParser, networkManager, city.getJurisdictionId(),
					apiKey);
		} catch (MalformedURLException e) {
			throw new APIWrapperException(e.getMessage(), Error.URL_BUILDER,
					null);
		} catch (DataParsingException e) {
			throw new APIWrapperException(e.getMessage(), Error.DATA_PARSING,
					null);
		} catch (IOException e) {
			throw new APIWrapperException(e.getMessage(),
					Error.NETWORK_MANAGER, null);
		}
	}
}
