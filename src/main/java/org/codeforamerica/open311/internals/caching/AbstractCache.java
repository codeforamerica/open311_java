package org.codeforamerica.open311.internals.caching;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codeforamerica.open311.facade.City;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.data.ServiceDiscoveryInfo;
import org.codeforamerica.open311.facade.data.ServiceRequest;
import org.codeforamerica.open311.facade.data.operations.GETServiceRequestsFilter;

/**
 * Implements all the operations of the {@link Cache} interface. Classes which
 * extend this will have to implement the abstract methods.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public abstract class AbstractCache implements Cache {
	protected final static String FILE = "cache.prop";
	/**
	 * Relationship between operations and time to live of the obtained data.
	 */
	private final Map<CacheableOperation, Integer> timeToLive;

	public AbstractCache() {
		timeToLive = new HashMap<CacheableOperation, Integer>();
		timeToLive.put(CacheableOperation.GET_SERVICE_DISCOVERY, 720);
		timeToLive.put(CacheableOperation.GET_SERVICE_LIST, 24);
		timeToLive.put(CacheableOperation.GET_SERVICE_DEFINITION, 24);
		timeToLive.put(CacheableOperation.GET_SERVICE_REQUEST_LIST, 24);
		timeToLive.put(CacheableOperation.GET_SINGLE_SERVICE_REQUEST, 24);
		timeToLive.put(CacheableOperation.GET_CITIES_SERVICE_DISCOVERY_URLS,
				1440);
	}

	@Override
	public void saveCitiesInfo(String data) {
		CacheableObject cacheableObject = new CacheableObject(
				data,
				timeToLive
						.get(CacheableOperation.GET_CITIES_SERVICE_DISCOVERY_URLS));
		saveProperty(
				CacheableOperation.GET_CITIES_SERVICE_DISCOVERY_URLS.toString(),
				cacheableObject.serialize());
	}

	@Override
	public String retrieveCitiesInfo() {
		return (String) new CacheableObject(
				getProperty(CacheableOperation.GET_CITIES_SERVICE_DISCOVERY_URLS
						.toString())).getObject();
	}

	@Override
	public void saveServiceDiscovery(City city,
			ServiceDiscoveryInfo serviceDiscovery) {
		if (city != null && serviceDiscovery != null) {
			CacheableObject cacheableObject = new CacheableObject(
					serviceDiscovery,
					timeToLive.get(CacheableOperation.GET_SERVICE_DISCOVERY));
			saveProperty(CacheableOperation.GET_SERVICE_DISCOVERY.toString()
					+ city.toString(), cacheableObject.serialize());
		}
	}

	@Override
	public ServiceDiscoveryInfo retrieveCachedServiceDiscoveryInfo(City city) {
		String rawData = getProperty(CacheableOperation.GET_SERVICE_DISCOVERY
				+ city.toString());
		CacheableObject deserializedObject = new CacheableObject(rawData);
		return (ServiceDiscoveryInfo) deserializedObject.getObject();
	}

	@Override
	public void saveListOfServices(String endpointUrl, List<Service> services) {
		if (endpointUrl != null && endpointUrl.length() > 0 && services != null) {
			Serializable list = (Serializable) services;
			CacheableObject cacheableObject = new CacheableObject(list,
					timeToLive.get(CacheableOperation.GET_SERVICE_LIST));
			saveProperty(CacheableOperation.GET_SERVICE_LIST.toString()
					+ endpointUrl, cacheableObject.serialize());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Service> retrieveCachedServiceList(String endpointUrl) {
		String rawData = getProperty(CacheableOperation.GET_SERVICE_LIST
				+ endpointUrl);
		CacheableObject deserializedObject = new CacheableObject(rawData);
		return (List<Service>) deserializedObject.getObject();
	}

	@Override
	public void saveServiceDefinition(String endpointUrl, String serviceCode,
			ServiceDefinition serviceDefinition) {
		if (endpointUrl != null && endpointUrl.length() > 0
				&& serviceCode != null && serviceCode.length() > 0
				&& serviceDefinition != null) {
			Serializable definition = (Serializable) serviceDefinition;
			CacheableObject cacheableObject = new CacheableObject(definition,
					timeToLive.get(CacheableOperation.GET_SERVICE_DEFINITION));
			saveProperty(CacheableOperation.GET_SERVICE_DEFINITION.toString()
					+ endpointUrl + serviceCode, cacheableObject.serialize());
		}
	}

	@Override
	public ServiceDefinition retrieveCachedServiceDefinition(
			String endpointUrl, String serviceCode) {
		String rawData = getProperty(CacheableOperation.GET_SERVICE_DEFINITION
				+ endpointUrl + serviceCode);
		CacheableObject deserializedObject = new CacheableObject(rawData);
		return (ServiceDefinition) deserializedObject.getObject();
	}

	@Override
	public void saveServiceRequestList(String endpointUrl,
			GETServiceRequestsFilter filter, List<ServiceRequest> requests) {
		if (endpointUrl != null && endpointUrl.length() > 0 && filter != null
				&& requests != null) {
			Serializable list = (Serializable) requests;
			CacheableObject cacheableObject = new CacheableObject(list,
					timeToLive.get(CacheableOperation.GET_SERVICE_REQUEST_LIST));
			saveProperty(CacheableOperation.GET_SERVICE_REQUEST_LIST
					+ endpointUrl + CacheableObject.serialize(filter),
					cacheableObject.serialize());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ServiceRequest> retrieveCachedServiceRequests(
			String endpointUrl, GETServiceRequestsFilter filter) {
		String rawData = getProperty(CacheableOperation.GET_SERVICE_REQUEST_LIST
				+ endpointUrl + CacheableObject.serialize(filter));
		CacheableObject deserializedObject = new CacheableObject(rawData);
		return (List<ServiceRequest>) deserializedObject.getObject();
	}

	@Override
	public void saveSingleServiceRequest(String endpointUrl,
			String serviceRequestId, ServiceRequest request) {
		if (endpointUrl != null && endpointUrl.length() > 0
				&& serviceRequestId != null && serviceRequestId.length() > 0
				&& request != null) {
			CacheableObject cacheableServiceRequest = new CacheableObject(
					request,
					timeToLive
							.get(CacheableOperation.GET_SINGLE_SERVICE_REQUEST));
			saveProperty(CacheableOperation.GET_SINGLE_SERVICE_REQUEST
					+ endpointUrl + serviceRequestId,
					cacheableServiceRequest.serialize());
		}
	}

	@Override
	public ServiceRequest retrieveCachedServiceRequest(String endpointUrl,
			String serviceRequestId) {
		String rawData = getProperty(CacheableOperation.GET_SINGLE_SERVICE_REQUEST
				+ endpointUrl + serviceRequestId);
		CacheableObject deserializedObject = new CacheableObject(rawData);
		return (ServiceRequest) deserializedObject.getObject();
	}

	public void setCustomTimeToLive(CacheableOperation operation,
			int timeToLiveInHours) {
		if (operation != null && timeToLiveInHours >= 1) {
			timeToLive.put(operation, timeToLiveInHours);
		}
	}

	/**
	 * Saves a property. The given parameters must be valid strings with content
	 * (empty or <code>null</code> strings are not allowed).
	 * 
	 * @param key
	 *            Key of the property.
	 * @param value
	 *            Value of the property.
	 */
	protected abstract void saveProperty(String key, String value);

	/**
	 * Retrieves a property.
	 * 
	 * @param key
	 *            Key of the property (empty or <code>null</code> strings are
	 *            not allowed).
	 * @return The property value of <code>null</code> if the key doesn't exist.
	 */
	protected abstract String getProperty(String key);
}
