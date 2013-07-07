package org.codeforamerica.open311.facade.data;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.codeforamerica.open311.facade.EndpointType;

/**
 * Contains information regarding to a city (endpoints, allowed formats...).
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class ServiceDiscoveryInfo implements Serializable {

	private static final long serialVersionUID = 5804902138488110319L;
	private final static String GEO_REPORT_V2_SPECIFICATION_URL = "http://wiki.open311.org/GeoReport_v2";
	private Date changeset;
	private String contact;
	private String keyService;
	List<Endpoint> endpoints;

	public ServiceDiscoveryInfo(Date changeset, String contact,
			String keyService, List<Endpoint> endpoints) {
		super();
		this.changeset = changeset;
		this.contact = contact;
		this.keyService = keyService;
		this.endpoints = endpoints;
	}

	public Date getChangeset() {
		return changeset;
	}

	public String getContact() {
		return contact;
	}

	public String getKeyService() {
		return keyService;
	}

	public List<Endpoint> getEndpoints() {
		return endpoints;
	}

	/**
	 * Searches through the list of endpoints and tries to find a valid endpoint
	 * of the given type. Tries to get the GeoReport latest version (currently
	 * v2).
	 * 
	 * @param endpointType
	 *            Type of the desired endpoint.
	 * @return <code>null</code> if a suitable endpoint couldn't be found.
	 */
	public Endpoint getMoreSuitableEndpoint(EndpointType endpointType) {
		List<Endpoint> typeFilteredEndpoints = new LinkedList<Endpoint>();
		for (Endpoint endpoint : endpoints) {
			if (endpoint.getType() == endpointType) {
				typeFilteredEndpoints.add(endpoint);
			}
		}
		for (Endpoint endpoint : typeFilteredEndpoints) {
			if (endpoint.getSpecificationUrl() == GEO_REPORT_V2_SPECIFICATION_URL) {
				return endpoint;
			}
		}
		return typeFilteredEndpoints.size() == 0 ? null : typeFilteredEndpoints
				.get(0);
	}
}
