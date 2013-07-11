package org.codeforamerica.open311.internals.network;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Builds the necessary URLs to communicate with the endpoints.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class URLBuilder {

	private static final String GET_SERVICE_LIST = "services";
	private static final String GET_SERVICE_DEFINITION = "services";
	private static final String POST_SERVICE_REQUEST = "requests";
	private static final String GET_SERVICE_REQUEST_FROM_A_TOKEN = "tokens";
	private static final String GET_SERVICE_REQUESTS = "requests";
	private String baseUrl;
	private String jurisdictionId;
	private String format;

	public URLBuilder(String baseUrl, String jurisdictionId, String format) {
		this.baseUrl = baseUrl;
		this.jurisdictionId = jurisdictionId;
		this.format = format;
	}

	/**
	 * Builds a GET Service List URL.
	 * 
	 * @return An URL ready to be accessed.
	 * @throws MalformedURLException
	 *             If one of the parts of the url (endpoint's base url,
	 *             format...) is not correct.
	 */
	public URL buildGetServiceListUrl() throws MalformedURLException {
		String url = baseUrl + "/" + GET_SERVICE_LIST + "." + format;
		return new URL(addJurisdictionId(url, jurisdictionId));
	}

	/**
	 * Builds a GET Service Definition URL.
	 * 
	 * @param serviceCode
	 *            Code of the service to find the definition.
	 * @return An URL ready to be accessed.
	 * @throws MalformedURLException
	 *             If one of the parts of the url (endpoint's base url,
	 *             format...) is not correct.
	 */
	public URL buildGetServiceDefinitionUrl(String serviceCode)
			throws MalformedURLException {
		String url = baseUrl + "/" + GET_SERVICE_DEFINITION + "/" + serviceCode
				+ "." + format;
		return new URL(addJurisdictionId(url, jurisdictionId));
	}

	/**
	 * Builds a POST Service Request URL.
	 * 
	 * @return An URL ready to be accessed.
	 * @throws MalformedURLException
	 *             If one of the parts of the url (endpoint's base url,
	 *             format...) is not correct.
	 */
	public URL buildPostServiceRequestUrl() throws MalformedURLException {
		String base = baseUrl + "/" + POST_SERVICE_REQUEST + "." + format;
		base = addJurisdictionId(base, jurisdictionId);
		return new URL(base);
	}

	/**
	 * Builds a GET Service Request ID From Token URL.
	 * 
	 * @param token
	 *            Token associated to a service request.
	 * @return An URL ready to be accessed.
	 * @throws MalformedURLException
	 *             If one of the parts of the url (endpoint's base url,
	 *             format...) is not correct.
	 */
	public URL buildGetServiceRequestIdFromATokenUrl(String token)
			throws MalformedURLException {
		String url = baseUrl + "/" + GET_SERVICE_REQUEST_FROM_A_TOKEN + "/"
				+ token + "." + format;
		return new URL(addJurisdictionId(url, jurisdictionId));
	}

	/**
	 * Builds a GET Service Requests URL.
	 * 
	 * @param arguments
	 *            Pairs (key, value) of optional arguments
	 * @return An URL ready to be accessed.
	 * @throws MalformedURLException
	 *             If one of the parts of the url (endpoint's base url,
	 *             format...) is not correct.
	 */
	public URL buildGetServiceRequests(Map<String, String> arguments)
			throws MalformedURLException {
		if (jurisdictionId.length() > 0) {
			arguments.put("jurisdiction_id", jurisdictionId);
		}
		return buildUrl(baseUrl + "/" + GET_SERVICE_REQUESTS + "." + format,
				arguments);
	}

	/**
	 * Builds a GET Service Request URL.
	 * 
	 * @param serviceId
	 *            Id of the service.
	 * @return An URL ready to be accessed.
	 * @throws MalformedURLException
	 *             If one of the parts of the url (endpoint's base url,
	 *             format...) is not correct.
	 */
	public URL buildGetServiceRequest(String serviceId)
			throws MalformedURLException {
		String url = baseUrl + "/" + GET_SERVICE_REQUESTS + "/" + serviceId
				+ "." + format;
		url = addJurisdictionId(url, jurisdictionId);
		return buildUrl(url, null);
	}

	/**
	 * Builds the body of a POST service request body.
	 * 
	 * @param arguments
	 *            List of (key, value) pairs.
	 * @param attributes
	 *            List of (key, value) attributes.
	 * @return A string of the form (key=value&key2=value2&...).
	 * @throws MalformedURLException
	 *             If any of the arguments given is not allowed.
	 */
	public String buildPostServiceRequestBody(Map<String, String> arguments,
			Map<String, String> attributes) throws MalformedURLException {
		arguments.putAll(attributes);
		return buildParameterString(arguments);
	}

	/**
	 * Builds a &-separated parameter string.
	 * 
	 * @param parameters
	 *            List of pairs (key, value).
	 * @return A string of the form (key=value&key2=value2&...).
	 */
	private String buildParameterString(Map<String, String> parameters) {
		boolean first = true;
		if (parameters != null) {
			StringBuilder builder = new StringBuilder();
			for (String key : parameters.keySet()) {
				if (first) {
					first = false;
				} else {
					builder.append("&");
				}
				builder.append(key + "=" + parameters.get(key));
			}
			return builder.toString();
		}
		return "";
	}

	/**
	 * Builds an URL from a base plus a list of arguments.
	 * 
	 * @param base
	 *            Host address.
	 * @param arguments
	 *            List of pairs (key, value).
	 * @return a URL built from the given data.
	 * @throws MalformedURLException
	 *             If there was any problem building the URL.
	 */
	private URL buildUrl(String base, Map<String, String> arguments)
			throws MalformedURLException {
		if (arguments == null) {
			return new URL(base);
		} else {
			return new URL(base + "?" + buildParameterString(arguments));
		}
	}

	/**
	 * Adds a jurisdictionId to an url.
	 * 
	 * @param url
	 *            Base url.
	 * @param jurisdictionId
	 *            Given jurisdiction id.
	 * @return The new url.
	 */
	private String addJurisdictionId(String url, String jurisdictionId) {
		if (jurisdictionId.length() > 0) {
			return url += "?jurisdiction_id=" + jurisdictionId;
		}
		return url;
	}
}
