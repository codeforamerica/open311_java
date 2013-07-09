package org.codeforamerica.open311.facade.data;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a <a
 * href="http://wiki.open311.org/GeoReport_v2#GET_Service_Definition">service
 * definition</a>.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class ServiceDefinition implements Serializable {

	private static final long serialVersionUID = -2002090161736619870L;
	private String serviceCode;
	private List<Attribute> attributes;

	public ServiceDefinition(String serviceCode, List<Attribute> attributes) {
		super();
		this.serviceCode = serviceCode;
		this.attributes = attributes;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

}
