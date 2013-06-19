package org.codeforamerica.open311.internals.network;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class URLBuilder {

	/**
	 * List of the possible optional arguments of the POST Service Request
	 * operation.
	 */
	private static final String[] POST_SERVICE_REQUEST_OPTIONAL_ARGUMENTS = {
			"api_key", "lat", "long", "address_id", "address_string", "email",
			"device_id", "account_id", "first_name", "last_name", "phone",
			"description", "media_url" };
	/**
	 * List of the possible optional arguments of the GET Service Requests
	 * operation.
	 */
	private static final String[] GET_SERVICE_REQUESTS_OPTIONAL_ARGUMENTS = {
			"service_request_id", "service_code", "start_date", "end_date",
			"status" };

	private static final String GET_SERVICE_LIST = "services";
	private static final String GET_SERVICE_DEFINITION = "services";
	private static final String POST_SERVICE_REQUEST = "requests";
	private static final String GET_SERVICE_REQUEST_FROM_A_TOKEN = "tokens";
	private static final String GET_SERVICE_REQUESTS = "requests";
	private String baseUrl;
	private String format;

	public URLBuilder(String baseUrl, String format) {
		this.baseUrl = baseUrl;
		this.format = format;
	}

	/**
	 * Builds a GET Service List URL.
	 * 
	 * @param jurisdictionId
	 *            Required if the same endpoint holds different places.
	 * @return An URL ready to be accessed.
	 * @throws MalformedURLException
	 *             If one of the parts of the url (endpoint's base url,
	 *             format...) is not correct.
	 */
	public URL buildGetServiceListUrl(String jurisdictionId)
			throws MalformedURLException {
		String url = baseUrl + "/" + GET_SERVICE_LIST + "." + format;
		return new URL(addJurisdictionId(url, jurisdictionId));
	}

	/**
	 * Builds a GET Service Definition URL.
	 * 
	 * @param jurisdictionId
	 *            Required if the same endpoint holds different places.
	 * @param serviceCode
	 *            Code of the service to find the definition.
	 * @return An URL ready to be accessed.
	 * @throws MalformedURLException
	 *             If one of the parts of the url (endpoint's base url,
	 *             format...) is not correct.
	 */
	public URL buildGetServiceDefinitionUrl(String jurisdictionId,
			String serviceCode) throws MalformedURLException {
		String url = baseUrl + "/" + GET_SERVICE_DEFINITION + "/" + serviceCode
				+ "." + format;
		return new URL(addJurisdictionId(url, jurisdictionId));
	}

	/**
	 * Builds a POST Service Request URL.
	 * 
	 * @param apiKey
	 *            Key required for the operation.
	 * @param jurisdictionId
	 *            Required if the same endpoint holds different places.
	 * @param arguments
	 *            Pairs (key, value) of optional arguments
	 * @return An URL ready to be accessed.
	 * @throws MalformedURLException
	 *             If one of the parts of the url (endpoint's base url,
	 *             format...) is not correct.
	 */
	public URL buildPostServiceRequestUrl(String apiKey, String jurisdictionId,
			Map<String, String> arguments) throws MalformedURLException {
		Set<String> validArguments = new HashSet<String>(
				Arrays.asList(POST_SERVICE_REQUEST_OPTIONAL_ARGUMENTS));
		validatearguments(arguments, validArguments);
		if (jurisdictionId.length() > 0) {
			arguments.put("jurisdiction_id", jurisdictionId);
		}
		arguments.put("api_key", apiKey);
		return buildUrl(baseUrl + "/" + POST_SERVICE_REQUEST + "." + format,
				arguments);
	}

	/**
	 * Builds a GET Service Request ID From Token URL.
	 * 
	 * @param jurisdictionId
	 *            Required if the same endpoint holds different places.
	 * @param token
	 *            Token associated to a service request.
	 * @return An URL ready to be accessed.
	 * @throws MalformedURLException
	 *             If one of the parts of the url (endpoint's base url,
	 *             format...) is not correct.
	 */
	public URL buildGetServiceRequestIdFromATokenUrl(String jurisdictionId,
			String token) throws MalformedURLException {
		String url = baseUrl + "/" + GET_SERVICE_REQUEST_FROM_A_TOKEN + "/"
				+ token + "." + format;
		return new URL(addJurisdictionId(url, jurisdictionId));
	}

	/**
	 * Builds a GET Service Requests URL.
	 * 
	 * @param jurisdictionId
	 *            Required if the same endpoint holds different places.
	 * @param arguments
	 *            Pairs (key, value) of optional arguments
	 * @return An URL ready to be accessed.
	 * @throws MalformedURLException
	 *             If one of the parts of the url (endpoint's base url,
	 *             format...) is not correct.
	 */
	public URL buildGetServiceRequests(String jurisdictionId,
			Map<String, String> arguments) throws MalformedURLException {
		Set<String> validArguments = new HashSet<String>(
				Arrays.asList(GET_SERVICE_REQUESTS_OPTIONAL_ARGUMENTS));
		validatearguments(arguments, validArguments);
		if (jurisdictionId.length() > 0) {
			arguments.put("jurisdiction_id", jurisdictionId);
		}
		return buildUrl(baseUrl + "/" + GET_SERVICE_REQUESTS + "." + format,
				arguments);
	}

	/**
	 * Builds a GET Service Request URL.
	 * 
	 * @param jurisdictionId
	 *            Required if the same endpoint holds different places.
	 * @param serviceId
	 *            Id of the service.
	 * @return An URL ready to be accessed.
	 * @throws MalformedURLException
	 *             If one of the parts of the url (endpoint's base url,
	 *             format...) is not correct.
	 */
	public URL buildGetServiceRequest(String jurisdictionId, String serviceId)
			throws MalformedURLException {
		String url = baseUrl + "/" + GET_SERVICE_REQUESTS + "/" + serviceId
				+ "." + format;
		url = addJurisdictionId(url, jurisdictionId);
		return buildUrl(url, null);
	}

	/**
	 * Checks if any given argument is not valid.
	 * 
	 * @param givenArguments
	 *            Arguments given to the operation.
	 * @param allowedArguments
	 *            Set of all the valid arguments of an operation.
	 * @throws MalformedURLException
	 *             If any of the given arguments is not valid.
	 */
	private void validatearguments(Map<String, String> givenArguments,
			Set<String> allowedArguments) throws MalformedURLException {
		if (givenArguments != null) {
			for (String key : givenArguments.keySet()) {
				if (!allowedArguments.contains(key)) {
					throw new MalformedURLException(
							"Invalid optional argument: " + key);
				}
			}
		}
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
		}
		StringBuilder builder = new StringBuilder();
		for (String key : arguments.keySet()) {
			builder.append(key + "=" + arguments.get(key) + "&");
		}
		String argumentsPart = builder.toString();
		if (argumentsPart.length() > 0) {
			base += "?";
			argumentsPart = argumentsPart.substring(0,
					argumentsPart.length() - 1);
		}
		return new URL(base + argumentsPart);
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
