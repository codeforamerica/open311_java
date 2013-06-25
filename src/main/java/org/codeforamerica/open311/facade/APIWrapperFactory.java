package org.codeforamerica.open311.facade;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.codeforamerica.open311.facade.data.Endpoint;
import org.codeforamerica.open311.facade.data.ServiceDiscoveryInfo;
import org.codeforamerica.open311.facade.exceptions.APIWrapperException;
import org.codeforamerica.open311.facade.exceptions.DataParsingException;
import org.codeforamerica.open311.internals.network.HTTPNetworkManager;
import org.codeforamerica.open311.internals.network.HTTPNetworkManagerFactory;
import org.codeforamerica.open311.internals.network.NetworkManager;
import org.codeforamerica.open311.internals.network.NetworkManagerFactory;
import org.codeforamerica.open311.internals.parsing.DataParser;
import org.codeforamerica.open311.internals.parsing.DataParserFactory;
import org.codeforamerica.open311.facade.exceptions.APIWrapperException.Error;

/**
 * Builds {@link APIWrapper} instances from different aspects of the system and
 * the chosen city.
 * 
 * @author Santiago Munin <santimunin@gmail.com>
 * 
 */
public class APIWrapperFactory {

	private static APIWrapperFactory instance = new APIWrapperFactory();

	private APIWrapperFactory() {
	}

	public static APIWrapperFactory getInstance() {
		return instance;
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
	 * @param networkManagerFactory
	 *            A {@link NetworkManager} instances builder.
	 * @return An instance suited to the given parameters.
	 * 
	 * @throws APIWrapperException
	 *             If there was any problem.
	 */
	public APIWrapper buildWrapper(City city, EndpointType endpointType,
			String apiKey, NetworkManagerFactory networkManagerFactory)
			throws APIWrapperException {
		try {
			NetworkManager networkManager = networkManagerFactory
					.createNetworkManager(Format.XML);
			DataParser dataParser = DataParserFactory.getInstance()
					.buildDataParser(Format.XML);
			ServiceDiscoveryInfo serviceDiscoveryInfo = dataParser
					.parseServiceDiscovery(networkManager.doGet(new URL(city
							.getDiscoveryUrl())));
			Endpoint endpoint = serviceDiscoveryInfo
					.getMoreSuitableEndpoint(endpointType);
			Format format = endpoint.getBestFormat();
			if (format != Format.XML) {
				networkManager = new HTTPNetworkManager(format);
				dataParser = DataParserFactory.getInstance().buildDataParser(
						format);
			}
			return new APIWrapper(endpoint.getUrl(), format, endpointType,
					dataParser, networkManager, city.getJurisdictionId(),
					apiKey);
		} catch (MalformedURLException e) {
			throw new APIWrapperException(Error.URL_BUILDER, null);
		} catch (DataParsingException e) {
			throw new APIWrapperException(Error.DATA_PARSING, null);
		} catch (IOException e) {
			throw new APIWrapperException(Error.NETWORK_MANAGER, null);
		}
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
	 * @return An instance suited to the given parameters.
	 * 
	 * @throws APIWrapperException
	 *             If there was any problem.
	 */
	public APIWrapper buildWrapper(City city, EndpointType endpointType,
			String apiKey) throws APIWrapperException {
		return buildWrapper(city, endpointType, apiKey,
				new HTTPNetworkManagerFactory());
	}
}
