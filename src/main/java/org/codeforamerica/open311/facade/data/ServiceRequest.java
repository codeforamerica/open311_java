package org.codeforamerica.open311.facade.data;

import java.net.URL;
import java.util.Date;

/**
 * Represents a Service Request.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class ServiceRequest {
	private String serviceRequestId;
	private Status status;
	private String statusNotes;
	private String serviceName;
	private String serviceCode;
	private String description;
	private String agencyResponsible;
	private String serviceNotice;
	private Date requestedDatetime;
	private Date updatedDatetime;
	private Date expectedDatetime;
	private String address;
	private String addressId;
	private int zipCode;
	private float latitude;
	private float longitude;
	private URL mediaUrl;

	public ServiceRequest(String serviceRequestId, Status status,
			String statusNotes, String serviceName, String serviceCode,
			String description, String agencyResponsible, String serviceNotice,
			Date requestedDatetime, Date updatedDatetime,
			Date expectedDatetime, String address, String addressId,
			int zipCode, float latitude, float longitude, URL mediaUrl) {
		super();
		this.serviceRequestId = serviceRequestId;
		this.status = status;
		this.statusNotes = statusNotes;
		this.serviceName = serviceName;
		this.serviceCode = serviceCode;
		this.description = description;
		this.agencyResponsible = agencyResponsible;
		this.serviceNotice = serviceNotice;
		this.requestedDatetime = requestedDatetime;
		this.updatedDatetime = updatedDatetime;
		this.expectedDatetime = expectedDatetime;
		this.address = address;
		this.addressId = addressId;
		this.zipCode = zipCode;
		this.latitude = latitude;
		this.longitude = longitude;
		this.mediaUrl = mediaUrl;
	}

	public String getServiceRequestId() {
		return serviceRequestId;
	}

	public Status getStatus() {
		return status;
	}

	public String getStatusNotes() {
		return statusNotes;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public String getDescription() {
		return description;
	}

	public String getAgencyResponsible() {
		return agencyResponsible;
	}

	public String getServiceNotice() {
		return serviceNotice;
	}

	public Date getRequestedDatetime() {
		return requestedDatetime;
	}

	public Date getUpdatedDatetime() {
		return updatedDatetime;
	}

	public Date getExpectedDatetime() {
		return expectedDatetime;
	}

	public String getAddress() {
		return address;
	}

	public String getAddressId() {
		return addressId;
	}

	public int getZipCode() {
		return zipCode;
	}

	public float getLatitude() {
		return latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public URL getMediaUrl() {
		return mediaUrl;
	}

	public static enum Status {
		OPEN, CLOSED;

		public static Status getFromString(String status) {
			status = status.toLowerCase();
			if (status.equals("open")) {
				return OPEN;
			}
			if (status.equals("closed")) {
				return CLOSED;
			}
			return null;
		}

		public String toString() {
			return this.name().toLowerCase();
		}
	}
}
