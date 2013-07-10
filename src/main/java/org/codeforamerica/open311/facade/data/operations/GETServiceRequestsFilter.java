package org.codeforamerica.open311.facade.data.operations;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codeforamerica.open311.facade.APIWrapper;
import org.codeforamerica.open311.facade.data.ServiceRequest.Status;
import org.codeforamerica.open311.internals.parsing.DataParser;
import org.codeforamerica.open311.internals.parsing.DateParser;

/**
 * Useful to pass optional parameters to the
 * {@link APIWrapper#getServiceRequests(GETServiceRequestsFilter)} operation.
 * 
 * Use the method chaining to add optional values to the object.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class GETServiceRequestsFilter implements Serializable {

	private static final long serialVersionUID = 3037178220547056225L;
	private Map<String, String> parameters = new HashMap<String, String>();

	/**
	 * Adds one or more service request ids to the filtering parameters.
	 * 
	 * @param serviceRequestId
	 *            A comma delimited list of service request ids. Example:
	 *            <code>1</code> or </code>1,2,3</code>.
	 * @return The same object with the given parameter added as an argument.
	 */
	public GETServiceRequestsFilter setServiceRequestId(String serviceRequestId) {
		if (serviceRequestId != null && serviceRequestId.length() > 0) {
			parameters.put(DataParser.SERVICE_REQUEST_ID_TAG, serviceRequestId);
		}
		return this;
	}

	public GETServiceRequestsFilter setServiceCode(String serviceCode) {
		if (serviceCode != null && serviceCode.length() > 0) {
			parameters.put(DataParser.SERVICE_CODE_TAG, serviceCode);
		}
		return this;
	}

	public GETServiceRequestsFilter setStartDate(Date startDate) {
		if (startDate != null) {
			parameters.put(DataParser.START_DATE_TAG,
					new DateParser().printDate(startDate));
		}
		return this;
	}

	public GETServiceRequestsFilter setEndDate(Date endDate) {
		if (endDate != null) {
			parameters.put(DataParser.END_DATE_TAG,
					new DateParser().printDate(endDate));
		}
		return this;
	}

	public GETServiceRequestsFilter setStatus(Status status) {
		if (status != null) {
			parameters.put(DataParser.STATUS_TAG, status.toString());
		}
		return this;
	}

	/**
	 * Builds a map containing all the set arguments.
	 * 
	 * @return List of pairs (key, value) with the required arguments.
	 */
	public Map<String, String> getOptionalParametersMap() {
		Map<String, String> result = new HashMap<String, String>();
		result.putAll(parameters);
		return result;
	}
}
