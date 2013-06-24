package org.codeforamerica.open311.facade.data;

import java.util.Date;
import java.util.List;

public class ServiceDiscoveryInfo {
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

	public void setChangeset(Date changeset) {
		this.changeset = changeset;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getKeyService() {
		return keyService;
	}

	public void setKeyService(String keyService) {
		this.keyService = keyService;
	}

	public List<Endpoint> getEndpoints() {
		return endpoints;
	}
}
