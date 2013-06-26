package org.codeforamerica.open311.facade.data;

/**
 * Wraps a POST service request response. <a
 * href="http://wiki.open311.org/GeoReport_v2#POST_Service_Request">More
 * info.</a>.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class POSTServiceRequestResponse {
	private String serviceRequestId;
	private String token;
	private String serviceNotice;
	private String accountId;

	public POSTServiceRequestResponse(String serviceRequestId, String token,
			String serviceNotice, String accountId) {
		super();
		this.serviceRequestId = serviceRequestId;
		this.token = token;
		this.serviceNotice = serviceNotice;
		this.accountId = accountId;
	}

	public String getServiceRequestId() {
		return serviceRequestId;
	}

	public String getToken() {
		return token;
	}

	public String getServiceNotice() {
		return serviceNotice;
	}

	public String getAccountId() {
		return accountId;
	}

}
