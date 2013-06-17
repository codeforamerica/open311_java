package org.codeforamerica.open311.facade.data;

/**
 * Represents an answer to the <a href=
 * "http://wiki.open311.org/GeoReport_v2#GET_service_request_id_from_a_token"
 * >GET service id from a token</a>.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class ServiceRequestIdResponse {
	private String serviceRequestId;
	private String token;

	public ServiceRequestIdResponse(String serviceRequestId, String token) {
		super();
		this.serviceRequestId = serviceRequestId;
		this.token = token;
	}

	public String getServiceRequestId() {
		return serviceRequestId;
	}

	public String getToken() {
		return token;
	}

}
