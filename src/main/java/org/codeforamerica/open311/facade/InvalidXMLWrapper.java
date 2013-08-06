package org.codeforamerica.open311.facade;

import java.net.URL;
import java.util.Map;

import org.codeforamerica.open311.facade.exceptions.APIWrapperException;
import org.codeforamerica.open311.internals.caching.Cache;
import org.codeforamerica.open311.internals.network.NetworkManager;
import org.codeforamerica.open311.internals.parsing.DataParser;

/**
 * Some endpoints return invalid characters in its XML response. Instances of
 * this class will parse those responses and clear the invalid characters.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class InvalidXMLWrapper extends APIWrapper {

	InvalidXMLWrapper(String endpointUrl, Format format, EndpointType type,
			DataParser dataParser, NetworkManager networkManager, Cache cache,
			String jurisdictionId, String apiKey) {
		super(endpointUrl, format, type, dataParser, networkManager, cache,
				jurisdictionId, apiKey);
	}

	/**
	 * GET operation. Calls {@link #sanitizeOutput(String)} right after
	 * receiving the response.
	 */
	@Override
	protected String networkGet(URL url) throws APIWrapperException {
		return sanitizeOutput(super.networkGet(url));
	}

	/**
	 * POST operation. Calls {@link #sanitizeOutput(String)} right after
	 * receiving the response.
	 */
	@Override
	protected String networkPost(URL url, Map<String, String> parameters)
			throws APIWrapperException {
		return sanitizeOutput(super.networkPost(url, parameters));
	}

	/**
	 * Skips invalid characters.
	 * 
	 * @param response
	 *            Received response.
	 * @return Same string without the invalid characters.
	 */
	private String sanitizeOutput(String response) {
		return response.replace("\u0010", "");
	}

}
