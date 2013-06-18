package org.codeforamerica.open311.internals.network;

import java.net.MalformedURLException;
import java.net.URL;

public class URLBuilder {
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

	public URL buildGetServiceListUrl() throws MalformedURLException {
		return new URL(baseUrl + "/" + GET_SERVICE_LIST + "." + format);
	}

	public URL buildGetServiceDefinitionUrl(String serviceCode)
			throws MalformedURLException {
		return new URL(baseUrl + "/" + GET_SERVICE_DEFINITION + "/"
				+ serviceCode + "." + format);
	}

	public URL buildPostServiceRequestUrl() throws MalformedURLException {
		return new URL(baseUrl + "/" + POST_SERVICE_REQUEST + "." + format);
	}

	public URL buildGetServiceRequestIdFromATokenUrl(String token)
			throws MalformedURLException {
		return new URL(baseUrl + "/" + GET_SERVICE_REQUEST_FROM_A_TOKEN + "/"
				+ token + "." + format);
	}

	public URL buildGetServicesRequests() throws MalformedURLException {
		return new URL(baseUrl + "/" + GET_SERVICE_REQUESTS + "." + format);
	}

	public URL buildGetServiceRequest(String serviceId)
			throws MalformedURLException {
		return new URL(baseUrl + "/" + GET_SERVICE_REQUESTS + "/" + serviceId
				+ "." + format);
	}
}
