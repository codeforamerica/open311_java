package org.codeforamerica.open311.internals.caching;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.codeforamerica.open311.facade.City;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.data.ServiceDiscoveryInfo;
import org.codeforamerica.open311.facade.data.ServiceRequest;
import org.codeforamerica.open311.facade.data.operations.GETServiceRequestsFilter;
import org.joda.time.DateTime;

public abstract class AbstractCache implements Cache {

	private final static String SERVICE_DISCOVERY = "service_discovery";
	private final static String SERVICE_LIST = "service_list";
	private final Map<String, Integer> timeToLive;

	public AbstractCache() {
		timeToLive = new HashMap<String, Integer>();
		timeToLive.put(SERVICE_DISCOVERY, 720);
		timeToLive.put(SERVICE_LIST, 24);
	}

	@Override
	public void saveEndpointsInfo(String data) {

	}

	@Override
	public String loadEndpointsInfo() {
		return null;
	}

	@Override
	public void saveServiceDiscovery(City city,
			ServiceDiscoveryInfo serviceDiscovery) {
		if (city != null && serviceDiscovery != null) {
			CacheableObject cacheableObject = new CacheableObject(
					serviceDiscovery, timeToLive.get(SERVICE_DISCOVERY));
			saveProperty(SERVICE_DISCOVERY + city.toString(),
					cacheableObject.serialize());
		}
	}

	@Override
	public ServiceDiscoveryInfo retrieveCachedServiceDiscoveryInfo(City city) {
		String rawData = getProperty(SERVICE_DISCOVERY + city.toString());
		CacheableObject deserializedObject = new CacheableObject(rawData);
		return (ServiceDiscoveryInfo) deserializedObject.getObject();
	}

	@Override
	public void saveListOfServices(String endpointUrl, List<Service> services) {
		if (endpointUrl != null && endpointUrl.length() > 0 && services != null) {
			Serializable list = (Serializable) services;
			CacheableObject cacheableObject = new CacheableObject(list,
					timeToLive.get(SERVICE_LIST));
			saveProperty(SERVICE_LIST + endpointUrl,
					cacheableObject.serialize());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Service> retrieveCachedServiceList(String endpointUrl) {
		String rawData = getProperty(SERVICE_LIST + endpointUrl);
		CacheableObject deserializedObject = new CacheableObject(rawData);
		return (List<Service>) deserializedObject.getObject();
	}

	@Override
	public void saveServiceDefinition(String endpointUrl, String serviceCode,
			ServiceDefinition serviceDefinition) {
		// TODO Auto-generated method stub

	}

	@Override
	public ServiceDefinition retrieveCachedServiceDefinition(
			String endpointUrl, String serviceCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveServiceRequestList(String endpointUrl,
			GETServiceRequestsFilter filter, List<ServiceRequest> requests) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ServiceRequest> retrieveCachedServiceRequests(
			GETServiceRequestsFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveSingleServiceRequest(String endpointUrl,
			String serviceRequestId, ServiceRequest request) {
		// TODO Auto-generated method stub

	}

	@Override
	public ServiceRequest retrieveCachedServiceRequest(String serviceRequestId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public abstract void deleteCache();

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

	/**
	 * Defines a simple "pair" class useful to define the time to live of each
	 * cache operation.
	 * 
	 * @author Santiago Munín <santimunin@gmail.com>
	 * 
	 */
	private static class TimeToLivePair {
		private String operation;
		private int timeToLiveInHours;

		public TimeToLivePair(String operation, int timeToLiveInHours) {
			super();
			this.operation = operation;
			this.timeToLiveInHours = timeToLiveInHours;
		}

		public String getOperation() {
			return operation;
		}

		public int getTimeToLiveInDays() {
			return timeToLiveInHours;
		}

	}
}
